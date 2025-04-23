package io.github.jhairs2.todo_list.todo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.jhairs2.todo_list.todo.model.TodoItem;

@RestController
@RequestMapping(path = "/api/v1/todos")
public class TodoListController {

    @GetMapping
    public List<TodoItem> getAllTodoList() {

        return List.of(new TodoItem(1L, "Say Hi!"));

    }

}
