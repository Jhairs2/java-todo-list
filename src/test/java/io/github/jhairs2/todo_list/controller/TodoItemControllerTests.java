package io.github.jhairs2.todo_list.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhairs2.todo_list.todo.controller.TodoItemController;
import io.github.jhairs2.todo_list.todo.dto.TodoItemDTO;
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

}
