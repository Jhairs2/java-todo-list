package io.github.jhairs2.todo_list.todo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(TodoService.class);
    private final TodoRepository todoRepository;
    private final TodoItemDTOMapper todoItemDTOMapper;
    private final ProjectListRepository projectListRepository;
    private final HelperService helperService;

    @Autowired
    public TodoService(TodoRepository todoRepository, TodoItemDTOMapper todoItemDTOMapper,
            ProjectListRepository projectListRepository, HelperService helperService) {
        this.todoRepository = todoRepository;
        this.todoItemDTOMapper = todoItemDTOMapper;
        this.projectListRepository = projectListRepository;
        this.helperService = helperService;

    }

    public List<TodoItemDTO> getAllTodosFromList(Long projectListId) {
        Long userId = this.helperService.getActiveUserId();
        String username = this.helperService.getActiveUsername();

        logger.info("Retrieving user {}'s tasks from project {}...", username, projectListId);
        ProjectList project = this.projectListRepository
                .findByUserIdAndId(userId, projectListId)
                .orElseThrow(() -> new ProjectListNotFoundException(
                        "Project with id " + projectListId + " could not be found that belongs to the user "
                                + username));

        return this.todoItemDTOMapper.convertToDTOList(project.getTasks());

    }

    public TodoItemDTO addTodoToList(Long projectListId, TodoItemDTO todoItemDTO) {
        Long userId = this.helperService.getActiveUserId();
        String username = this.helperService.getActiveUsername();

        if (todoItemDTO == null || todoItemDTO.task().isBlank()) {
            throw new IllegalArgumentException("TodoItem cannot be null or blank");
        }

        TodoItem todoItem = new TodoItem(todoItemDTO.task());

        logger.info("Retrieving project {} to add task '{}'", projectListId, todoItem.getTask());
        ProjectList project = this.projectListRepository
                .findByUserIdAndId(userId, projectListId)
                .orElseThrow(() -> new ProjectListNotFoundException(
                        "Project with id " + projectListId + " could not be found that belongs to the user "
                                + username));
        todoItem.setList(project);
        project.getTasks().add(todoItem);

        return this.todoItemDTOMapper.convertToDTO(this.todoRepository.save(todoItem));
    }

    @Transactional
    public TodoItemDTO updateTodoById(Long projectListId, Long todoItemId, TodoItemDTO todoItemDTO) {
        Long userId = this.helperService.getActiveUserId();
        String username = this.helperService.getActiveUsername();

        if (todoItemDTO == null) {
            throw new IllegalArgumentException("TodoItem cannot be null");
        }

        logger.info("Retrieving user {}'s project {} to update task...", username, projectListId);
        if (!this.projectListRepository.existsByUserIdAndId(userId, projectListId)) {
            throw new ProjectListNotFoundException(
                    "Project with id " + projectListId + " could not be found that belongs to that user "
                            + username);
        }

        logger.info("Updating task...");
        TodoItem newTodoItem = this.todoRepository.findByListIdAndId(projectListId, todoItemId)
                .orElseThrow(() -> new TodoItemNotFoundException("Task with that " + todoItemId + " does not exist."));

        if (todoItemDTO.task() != null && !todoItemDTO.task().isBlank()) {
            newTodoItem.setTask(todoItemDTO.task());
        }

        newTodoItem.setCompleted(todoItemDTO.completed());

        return this.todoItemDTOMapper.convertToDTO(this.todoRepository.save(newTodoItem));

    }

    public TodoItemDTO deleteTodoFromList(Long projectListId, Long todoItemId) {
        Long userId = this.helperService.getActiveUserId();
        String username = this.helperService.getActiveUsername();
        logger.info("Retrieving user {}'s project {} to delete task...", userId, projectListId);
        if (!this.projectListRepository.existsByUserIdAndId(userId, projectListId)) {
            throw new ProjectListNotFoundException(
                    "Project with id " + projectListId + " could not be found that belongs to that user "
                            + username);
        }

        logger.info("Deleting task...");
        TodoItem deletedTask = this.todoRepository.findByListIdAndId(projectListId, todoItemId)
                .orElseThrow(() -> new TodoItemNotFoundException("Task with that " + todoItemId + " does not exist."));

        this.todoRepository.delete(deletedTask);

        return this.todoItemDTOMapper.convertToDTO(deletedTask);
    }

}
