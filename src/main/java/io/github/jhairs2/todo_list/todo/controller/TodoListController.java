package io.github.jhairs2.todo_list.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.jhairs2.todo_list.todo.model.TodoItem;

import io.github.jhairs2.todo_list.todo.service.TodoService;

@RestController
@RequestMapping(path = "/api/v1/todos")
public class TodoListController {

    private final TodoService todoService;

    @Autowired
    public TodoListController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoItem> getAllTodoList() {
        return this.todoService.getAllTodoList();

    }

    @PostMapping
    public TodoItem addTodoToList(@RequestBody TodoItem task) {
        return this.todoService.addTodoToList(task);
    }

}
