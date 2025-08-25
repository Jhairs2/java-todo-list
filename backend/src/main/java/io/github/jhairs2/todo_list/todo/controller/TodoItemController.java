package io.github.jhairs2.todo_list.todo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhairs2.todo_list.todo.dto.TodoItemDTO;
import io.github.jhairs2.todo_list.todo.service.TodoService;

@RestController
@RequestMapping(path = "/api/v1/projects/{projectId}/todos")
public class TodoItemController {

    private final TodoService todoService;

    @Autowired
    public TodoItemController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoItemDTO> getAllTodosFromList(@PathVariable("projectId") Long projectId) {
        return this.todoService.getAllTodosFromList(projectId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoItemDTO addTodoToList(@PathVariable("projectId") Long projectId, @RequestBody TodoItemDTO todoItemDTO) {
        return this.todoService.addTodoToList(projectId, todoItemDTO);
    }

    @PutMapping(path = "/{todoItemId}")
    public TodoItemDTO updateTodoById(@PathVariable("projectId") Long projectId,
            @PathVariable("todoItemId") Long todoItemId, @RequestBody TodoItemDTO todoItemDTO) {
        return this.todoService.updateTodoById(projectId, todoItemId, todoItemDTO);
    }

    @DeleteMapping(path = "/{todoItemId}")
    public TodoItemDTO deleteTodoFromList(@PathVariable("projectId") Long projectId,
            @PathVariable("todoItemId") Long todoItemId) {
        return this.todoService.deleteTodoFromList(projectId, todoItemId);
    }

}
