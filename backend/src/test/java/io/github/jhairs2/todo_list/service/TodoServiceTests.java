package io.github.jhairs2.todo_list.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.jhairs2.todo_list.todo.dto.TodoItemDTO;
import io.github.jhairs2.todo_list.todo.exceptions.ProjectListNotFoundException;
import io.github.jhairs2.todo_list.todo.exceptions.TodoItemNotFoundException;
import io.github.jhairs2.todo_list.todo.mapper.TodoItemDTOMapper;
import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.model.TodoItem;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;
import io.github.jhairs2.todo_list.todo.repository.TodoRepository;
import io.github.jhairs2.todo_list.todo.service.TodoService;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTests {

        @Mock
        private TodoRepository todoRepository;

        @Mock
        private ProjectListRepository projectListRepository;

        @Mock
        private TodoItemDTOMapper todoItemDTOMapper;

        @InjectMocks
        private TodoService todoService;

        private TodoItem todoItem;
        private ProjectList projectList;
        private TodoItemDTO todoItemDTO;

        @BeforeEach
        void setUp() {
                this.projectList = new ProjectList("testProject");
                this.todoItem = new TodoItem("Test task");

                this.projectList.getTasks().add(this.todoItem);
                this.todoItem.setList(this.projectList);

                this.todoItemDTO = new TodoItemDTO(1L, this.todoItem.getTask(), false, this.projectList.getListTitle());

        }

        @DisplayName("Test should return all tasks of the project")
        @Test
        void Get_IfProjectAndTasksExist_ReturnAllTasks() {
                // Arrange
                when(this.projectListRepository.findById(1L)).thenReturn(Optional.of(this.projectList));
                when(this.todoItemDTOMapper.convertToDTOList(this.projectList.getTasks()))
                                .thenReturn(List.of(this.todoItemDTO));

                // Act
                List<TodoItemDTO> results = this.todoService.getAllTodosFromList(1L);

                // Assert
                Assertions.assertThat(results)
                                .isNotNull()
                                .hasSize(1)
                                .isEqualTo(List.of(this.todoItemDTO));

        }

        @DisplayName("Test should return an empty list if project has no tasks")
        @Test
        void Get_IfNoTasksExist_ReturnEmptyList() {

                // Arrange
                List<TodoItemDTO> emptyList = List.of();
                when(this.projectListRepository.findById(1L)).thenReturn(Optional.of(this.projectList));
                when(this.todoItemDTOMapper.convertToDTOList(this.projectList.getTasks()))
                                .thenReturn(emptyList);

                // Act
                List<TodoItemDTO> results = this.todoService.getAllTodosFromList(1L);

                // Assert
                Assertions.assertThat(results)
                                .isNotNull()
                                .hasSize(0);

        }

        @DisplayName("Test should return a ProjectNotFoundException if project is not found")
        @Test
        void Get_WithInvalidProjectId_ThrowException() {

                // Arrange
                when(this.projectListRepository.findById(1L)).thenReturn(Optional.empty());

                // Act / Assert
                Assertions.assertThatThrownBy(() -> this.todoService.getAllTodosFromList(1L))
                                .isInstanceOf(ProjectListNotFoundException.class)
                                .hasMessageContaining("Project with that id can not be found.");

        }

        @DisplayName("Test should return the added todoItem")
        @Test
        void Create_WithValidArgs_ReturnCreatedTodo() {

                // Arrange
                when(this.projectListRepository.findById(1L)).thenReturn(Optional.of(this.projectList));
                when(this.todoRepository.save(this.todoItem)).thenReturn(this.todoItem);
                when(this.todoItemDTOMapper.convertToDTO(this.todoItem)).thenReturn(this.todoItemDTO);

                // Act
                TodoItemDTO results = this.todoService.addTodoToList(1L, this.todoItem);

                // Assert
                Assertions.assertThat(results)
                                .isNotNull()
                                .isEqualTo(this.todoItemDTO);

                verify(this.todoRepository).save(this.todoItem);
        }

        @DisplayName("Test should return an exception if todo has empty title")
        @Test
        void Create_WithNoTitle_ThrowException() {
                // Arrange
                TodoItem newTask = new TodoItem("");

                // Act /Assert
                Assertions.assertThatThrownBy(() -> this.todoService.addTodoToList(1L, newTask))
                                .isInstanceOf(IllegalArgumentException.class);

        }

        @DisplayName("Test should return an exception if todo is Null")
        @Test
        void Create_WithNullTodoItem_ReturnException() {
                // Arrange
                TodoItem newTask = null;

                // Act /Assert
                Assertions.assertThatThrownBy(() -> this.todoService.addTodoToList(1L, newTask))
                                .isInstanceOf(IllegalArgumentException.class);

        }

        @DisplayName("Test should return an exception if project cannot be found")
        @Test
        void Create_WithInvalidProjectId_ThrowException() {
                // Arrange
                when(this.projectListRepository.findById(1L)).thenReturn(Optional.empty());

                // Act /Assert
                Assertions.assertThatThrownBy(() -> this.todoService.addTodoToList(1L, this.todoItem))
                                .isInstanceOf(ProjectListNotFoundException.class);

        }

        @DisplayName("Test should return updated todo")
        @Test
        void Update_WithValidArgs_ReturnUpdatedTodo() {
                // Arrange
                TodoItem updatedTask = new TodoItem("Updated Task");
                when(this.todoRepository.findById(1L)).thenReturn(Optional.of(this.todoItem));

                this.todoItem.setTask(updatedTask.getTask());
                this.todoItem.setCompleted(updatedTask.isCompleted());

                TodoItemDTO newTask = new TodoItemDTO(this.todoItem.getId(),
                                this.todoItem.getTask(),
                                this.todoItem.isCompleted(),
                                this.todoItem.getList().getListTitle());

                when(this.todoRepository.save(this.todoItem)).thenReturn(this.todoItem);
                when(this.todoItemDTOMapper.convertToDTO(this.todoItem)).thenReturn(newTask);

                // Act
                TodoItemDTO results = this.todoService.updateTodoById(1L, updatedTask);

                // Assert
                Assertions.assertThat(results)
                                .isNotNull()
                                .isEqualTo(newTask);

        }

        @DisplayName("Test should return exception if Todo to update is not found")
        @Test
        void Update_WithInvalidTodoId_ThrowException() {
                // Arrange
                when(this.todoRepository.findById(1L)).thenReturn(Optional.empty());

                Assertions.assertThatThrownBy(() -> this.todoService.updateTodoById(1L, this.todoItem))
                                .isInstanceOf(TodoItemNotFoundException.class);

        }

        @DisplayName("Test should return deleted Todo")
        @Test
        void Delete_WithValidTodoId_ReturnDeletedTodo() {
                // Arrange
                when(this.todoRepository.findById(1L)).thenReturn(Optional.of(this.todoItem));
                when(this.todoItemDTOMapper.convertToDTO(this.todoItem)).thenReturn(this.todoItemDTO);

                // Act
                TodoItemDTO results = this.todoService.deleteTodoFromList(1L);

                // Assert
                Assertions.assertThat(results)
                                .isNotNull()
                                .isEqualTo(this.todoItemDTO);

                verify(this.todoRepository).delete(this.todoItem);
        }

        @DisplayName("Test should return exception if Todo to delete is not found")
        @Test
        void Delete_WithInvalidTodoId_ThrowException() {
                // Arrange
                when(this.todoRepository.findById(1L)).thenReturn(Optional.empty());

                Assertions.assertThatThrownBy(() -> this.todoService.deleteTodoFromList(1L))
                                .isInstanceOf(TodoItemNotFoundException.class);

        }
}
