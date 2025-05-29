package io.github.jhairs2.todo_list.todo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.github.jhairs2.todo_list.todo.dto.TodoItemDTO;
import io.github.jhairs2.todo_list.todo.model.TodoItem;

@Component
public class TodoItemDTOMapper {

    public TodoItemDTO convertTodoItemToDTO(TodoItem todoItem) {
        TodoItemDTO todoDTO = new TodoItemDTO(todoItem.getId(), todoItem.getTask(), todoItem.isCompleted(),
                todoItem.getList().getListTitle());
        return todoDTO;
    }

    public List<TodoItemDTO> convertTodoItemsToDTOList(List<TodoItem> todoItems) {
        return todoItems.stream()
                .map(this::convertTodoItemToDTO)
                .collect(Collectors.toList());
    }
}
