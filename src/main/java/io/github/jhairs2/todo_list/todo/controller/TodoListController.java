package io.github.jhairs2.todo_list.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.jhairs2.todo_list.todo.model.TodoItem;
import io.github.jhairs2.todo_list.todo.repository.TodoRepository;

@RestController
@RequestMapping(path = "/api/v1/todos")
public class TodoListController {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoListController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping
    public List<TodoItem> getAllTodoList() {
        return this.todoRepository.findAll();

    }

}
