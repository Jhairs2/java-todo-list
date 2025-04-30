package io.github.jhairs2.todo_list.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.model.TodoItem;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;
import io.github.jhairs2.todo_list.todo.repository.TodoRepository;
import jakarta.transaction.Transactional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final ProjectListRepository projectListRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository, ProjectListRepository projectListRepository) {
        this.todoRepository = todoRepository;
        this.projectListRepository = projectListRepository;
    }

    public List<TodoItem> getAllTodosFromList(Long id) {
        ProjectList project = this.projectListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no project with this id"));

        return project.getTasks();

    }

    public TodoItem addTodoToList(Long id, TodoItem task) {

        if (task == null || task.getTask().length() == 0) {
            throw new IllegalArgumentException("Task cannot be blank or null");
        }

        ProjectList project = this.projectListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no project with this id"));

        task.setList(project);
        project.getTasks().add(task);

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
