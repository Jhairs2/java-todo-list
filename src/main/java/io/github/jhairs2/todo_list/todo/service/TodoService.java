package io.github.jhairs2.todo_list.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.jhairs2.todo_list.todo.model.TodoItem;
import io.github.jhairs2.todo_list.todo.repository.TodoRepository;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoItem> getAllTodoList() {
        return this.todoRepository.findAll();

    }

    public TodoItem addTodoToList(TodoItem task) {

        if (task == null || task.getTask().length() == 0) {
            throw new IllegalArgumentException("Task cannot be blank or null");
        }

        return this.todoRepository.save(task);

    }

}
