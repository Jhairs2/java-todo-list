package io.github.jhairs2.todo_list.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhairs2.todo_list.todo.controller.TodoItemController;
import io.github.jhairs2.todo_list.todo.dto.TodoItemDTO;
import io.github.jhairs2.todo_list.todo.exceptions.ProjectListNotFoundException;
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
    void shouldReturnAllTasksFromProjectList_IfProjectListsExists() throws Exception {
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
    void shouldReturnEmptyList_IfProjectList_HasNoTasks() throws Exception {
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
    void shouldReturnProjectNotFoundException_IfNotFound() throws Exception {
        // Arrange
        when(this.todoService.getAllTodosFromList(1L))
                .thenThrow(new ProjectListNotFoundException("Project with that id can not be found."));

        // Act / Assert
        this.mockMvc.perform(get("/api/v1/projects/{projectId}/todos", 1L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Project with that id can not be found."));

    }

}
