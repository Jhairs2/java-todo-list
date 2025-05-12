package io.github.jhairs2.todo_list.service;

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
import io.github.jhairs2.todo_list.todo.mapper.ProjectListDTOMapper;
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
    void shouldRetuenAllTasksFromProjectList_IfProjectListsExists() {
        // Arrange
        when(this.projectListRepository.findById(1L)).thenReturn(Optional.of(this.projectList));
        when(this.todoItemDTOMapper.convertTodoItemsToDTOList(this.projectList.getTasks()))
                .thenReturn(List.of(this.todoItemDTO));

        // Act
        List<TodoItemDTO> results = this.todoService.getAllTodosFromList(1L);

        // Assert
        Assertions.assertThat(results)
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(results)
                .extracting("id")
                .containsExactly(this.todoItemDTO.id());

        Assertions.assertThat(results)
                .extracting("task")
                .containsExactly(this.todoItemDTO.task());

        Assertions.assertThat(results)
                .extracting("listTitle")
                .containsExactly(this.todoItemDTO.listTitle());

    }

}
