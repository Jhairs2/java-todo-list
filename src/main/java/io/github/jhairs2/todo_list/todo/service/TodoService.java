package io.github.jhairs2.todo_list.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.jhairs2.todo_list.todo.dto.TodoItemDTO;
import io.github.jhairs2.todo_list.todo.exceptions.ProjectListNotFoundException;
import io.github.jhairs2.todo_list.todo.exceptions.TodoItemNotFoundException;
import io.github.jhairs2.todo_list.todo.mapper.TodoItemDTOMapper;
import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.model.TodoItem;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;
import io.github.jhairs2.todo_list.todo.repository.TodoRepository;
import jakarta.transaction.Transactional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoItemDTOMapper todoItemDTOMapper;
    private final ProjectListRepository projectListRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository, TodoItemDTOMapper todoItemDTOMapper,
            ProjectListRepository projectListRepository) {
        this.todoRepository = todoRepository;
        this.todoItemDTOMapper = todoItemDTOMapper;
        this.projectListRepository = projectListRepository;
    }

    public List<TodoItemDTO> getAllTodosFromList(Long id) {
        ProjectList project = this.projectListRepository.findById(id)
                .orElseThrow(() -> new ProjectListNotFoundException("Project with that id can not be found."));

        return this.todoItemDTOMapper.convertTodoItemsToDTOList(project.getTasks());

    }

    public TodoItemDTO addTodoToList(Long id, TodoItem task) {

        if (task == null || task.getTask().length() == 0) {
            throw new IllegalArgumentException("Task cannot be blank or null");
        }

        ProjectList project = this.projectListRepository.findById(id)
                .orElseThrow(() -> new ProjectListNotFoundException("Project with that id can not be found."));
        task.setList(project);
        project.getTasks().add(task);

        return this.todoItemDTOMapper.convertTodoItemToDTO(this.todoRepository.save(task));

    }

    @Transactional
    public TodoItemDTO updateTodoById(Long id, TodoItem updatedTask) {
        TodoItem oldTask = this.todoRepository.findById(id)
                .orElseThrow(() -> new TodoItemNotFoundException("Task with that id does not exist."));

        oldTask.setTask(updatedTask.getTask());
        oldTask.setCompleted(updatedTask.isCompleted());

        return this.todoItemDTOMapper.convertTodoItemToDTO(this.todoRepository.save(oldTask));

    }

    public TodoItemDTO deleteTodoFromList(Long id) {
        TodoItem deletedTask = this.todoRepository.findById(id)
                .orElseThrow(() -> new TodoItemNotFoundException("Task with that id does not exist."));

        this.todoRepository.delete(deletedTask);

        return this.todoItemDTOMapper.convertTodoItemToDTO(deletedTask);
    }

}
