package io.github.jhairs2.todo_list.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhairs2.todo_list.todo.controller.TodoItemController;
import io.github.jhairs2.todo_list.todo.dto.TodoItemDTO;
import io.github.jhairs2.todo_list.todo.exceptions.ProjectListNotFoundException;
import io.github.jhairs2.todo_list.todo.exceptions.TodoItemNotFoundException;
import io.github.jhairs2.todo_list.todo.model.TodoItem;
import io.github.jhairs2.todo_list.todo.service.TodoService;

@WebMvcTest(TodoItemController.class)
public class TodoItemControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private TodoService todoService;

        private TodoItemDTO todoItemDTO;
        private TodoItemDTO todoItemDTO2;
        private List<TodoItemDTO> todoItemList;

        @BeforeEach
        void setUp() {
                this.todoItemDTO = new TodoItemDTO(1L, "Test task", false, "Example");
                this.todoItemDTO2 = new TodoItemDTO(2L, "Test task2", false, "Example");

                this.todoItemList = List.of(this.todoItemDTO, this.todoItemDTO2);

        }

        @DisplayName("Test should return all tasks from project")
        @Test
        void Get_IfProjectAndTasksExist_ReturnAllTasks() throws Exception {
                // Arrange
                when(this.todoService.getAllTodosFromList(1L)).thenReturn(this.todoItemList);

                // Act / Assert
                this.mockMvc.perform(get("/api/v1/projects/{projectId}/todos", 1L))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].task").value(this.todoItemDTO.task()))
                                .andExpect(jsonPath("$[1].task").value(this.todoItemDTO2.task()));

        }

        @DisplayName("Test should return empty list when project has no task")
        @Test
        void Get_IfNoTasksExist_ReturnEmptyList() throws Exception {
                // Arrange
                when(this.todoService.getAllTodosFromList(1L)).thenReturn(List.of());

                // Act / Assert
                this.mockMvc.perform(get("/api/v1/projects/{projectId}/todos", 1L))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(0));

        }

        @DisplayName("Test should return exception if project cannot be found")
        @Test
        void Get_WithInvalidProjectId_ThrowException() throws Exception {
                // Arrange
                when(this.todoService.getAllTodosFromList(1L))
                                .thenThrow(new ProjectListNotFoundException("Project with that id can not be found."));

                // Act / Assert
                this.mockMvc.perform(get("/api/v1/projects/{projectId}/todos", 1L))
                                .andDo(print())
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Project with that id can not be found."));

        }

        @DisplayName("Test should return todo that was added")
        @Test
        void Create_WithValidArgs_ReturnCreatedTodo() throws Exception {
                // Arrange
                TodoItem todo = new TodoItem("New Task");
                when(this.todoService.addTodoToList(eq(1L), any(TodoItem.class)))
                                .thenReturn(new TodoItemDTO(1L, todo.getTask(), todo.isCompleted(), "Example"));

                String requestBody = this.objectMapper.writeValueAsString(todo);

                // Act / Assert
                this.mockMvc.perform(post("/api/v1/projects/{projectId}/todos", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.task").value(todo.getTask()));
        }

        @DisplayName("Test should return exception if project to add todo is not found")
        @Test
        void Create_WithInvalidProjectId_ThrowException() throws Exception {
                // Arrange
                TodoItem todo = new TodoItem("New Task");
                when(this.todoService.addTodoToList(eq(1L), any(TodoItem.class)))
                                .thenThrow(new ProjectListNotFoundException("Project with that id can not be found."));

                String requestBody = this.objectMapper.writeValueAsString(todo);

                // Act / Assert
                this.mockMvc.perform(post("/api/v1/projects/{projectId}/todos", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Project with that id can not be found."));
        }

        @DisplayName("Test should return exception if invalid TodoItem argument")
        @Test
        void Create_WithNoTitle_ThrowException() throws Exception {
                // Arrange
                TodoItem todo = new TodoItem("");
                when(this.todoService.addTodoToList(eq(1L), any(TodoItem.class)))
                                .thenThrow(new IllegalArgumentException("Task cannot be blank or null"));

                String requestBody = this.objectMapper.writeValueAsString(todo);

                // Act / Assert
                this.mockMvc.perform(post("/api/v1/projects/{projectId}/todos", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Task cannot be blank or null"));
        }

        @DisplayName("Test should return exception if TodoItem is null")
        @Test
        void Create_WithNullTodoItem_ReturnException() throws Exception {
                // Arrange
                when(this.todoService.addTodoToList(eq(1L), any(TodoItem.class)))
                                .thenThrow(new IllegalArgumentException("Task cannot be blank or null"));

                String requestBody = "{}";

                // Act / Assert
                this.mockMvc.perform(post("/api/v1/projects/{projectId}/todos", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Task cannot be blank or null"));
        }

        @DisplayName("Test should return updated TodoItem")
        @Test
        void Update_WithValidArgs_ReturnUpdatedTodo() throws Exception {
                // Arrange
                TodoItem updatedTodo = new TodoItem(this.todoItemDTO.task());
                when(this.todoService.updateTodoById(eq(1L), any(TodoItem.class)))
                                .thenReturn(new TodoItemDTO(1L, updatedTodo.getTask(), false, "Example"));

                String requestBody = this.objectMapper.writeValueAsString(updatedTodo);

                // Act / Assert
                this.mockMvc.perform(put("/api/v1/projects/{projectId}/todos/{taskId}", 1L, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.task").value(updatedTodo.getTask()));
        }

        @DisplayName("Test should return exception when todo to update cannot be found")
        @Test
        void Update_WithInvalidTodoId_ThrowException() throws Exception {
                // Arrange
                TodoItem updatedTodo = new TodoItem(this.todoItemDTO.task());
                when(this.todoService.updateTodoById(eq(1L), any(TodoItem.class)))
                                .thenThrow(new TodoItemNotFoundException("Task with that id does not exist."));

                String requestBody = this.objectMapper.writeValueAsString(updatedTodo);

                // Act / Assert
                this.mockMvc.perform(put("/api/v1/projects/{projectId}/todos/{taskId}", 1L, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Task with that id does not exist."));
        }

        @DisplayName("Test should return deleted TodoItem")
        @Test
        void Delete_WithValidTodoId_ReturnDeletedTodo() throws Exception {
                // Arrange
                when(this.todoService.deleteTodoFromList(1L)).thenReturn(this.todoItemDTO);

                // Act / Assert
                this.mockMvc.perform(delete("/api/v1/projects/{projectId}/todos/{taskId}", 1L, 1L))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(this.todoItemDTO.id()))
                                .andExpect(jsonPath("$.task").value(this.todoItemDTO.task()));

        }

        @DisplayName("Test should return exception when todo to delete cannot be found")
        @Test
        void Delete_WithInvalidTodoId_ThrowException() throws Exception {

                // Arrange
                when(this.todoService.deleteTodoFromList(1L))
                                .thenThrow(new TodoItemNotFoundException("Task with that id does not exist."));

                // Act / Assert
                this.mockMvc.perform(delete("/api/v1/projects/{projectId}/todos/{taskId}", 1L, 1L))
                                .andDo(print())
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Task with that id does not exist."));
        }

}
