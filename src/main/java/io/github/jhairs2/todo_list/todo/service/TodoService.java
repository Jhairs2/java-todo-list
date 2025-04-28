package io.github.jhairs2.todo_list.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import io.github.jhairs2.todo_list.todo.model.TodoItem;
import io.github.jhairs2.todo_list.todo.repository.TodoRepository;
import jakarta.transaction.Transactional;

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

    @Transactional
    public TodoItem updateTodoById(Long id, TodoItem updatedTask) {
        TodoItem oldTask = this.todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        oldTask.setTask(updatedTask.getTask());
        oldTask.setCompleted(updatedTask.isCompleted());

        return this.todoRepository.save(oldTask);

    }

    public TodoItem deleteTodoFromList(Long id) {
        TodoItem deletedTask = this.todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        this.todoRepository.delete(deletedTask);

        return deletedTask;
    }

}
