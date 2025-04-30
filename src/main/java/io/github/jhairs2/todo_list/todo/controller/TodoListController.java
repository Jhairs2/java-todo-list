package io.github.jhairs2.todo_list.todo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.jhairs2.todo_list.todo.model.TodoItem;
import io.github.jhairs2.todo_list.todo.service.TodoService;

@RestController
@RequestMapping(path = "/api/v1/{projectId}/todos")
public class TodoListController {

    private final TodoService todoService;

    @Autowired
    public TodoListController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoItem> getAllTodosFromList(@PathVariable("projectId") Long id) {
        return this.todoService.getAllTodoList(id);

    }

    @PostMapping
    public TodoItem addTodoToList(@RequestBody TodoItem task) {
        return this.todoService.addTodoToList(task);
    }

    @PutMapping(path = "/{taskId}")
    public TodoItem updateTodoById(@PathVariable("taskId") Long id, @RequestBody TodoItem task) {
        return this.todoService.updateTodoById(id, task);
    }

    @DeleteMapping(path = "/{taskId}")
    public TodoItem deleteTodoFromList(@PathVariable("taskId") Long id) {
        return this.todoService.deleteTodoFromList(id);
    }

}
