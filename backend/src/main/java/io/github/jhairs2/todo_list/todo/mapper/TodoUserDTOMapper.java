package io.github.jhairs2.todo_list.todo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.github.jhairs2.todo_list.todo.dto.TodoUserDTO;
import io.github.jhairs2.todo_list.todo.model.TodoUser;

@Component
public class TodoUserDTOMapper {

    public TodoUserDTO convertToDTO(TodoUser todoUser) {
        TodoUserDTO todoUserDTO = new TodoUserDTO(todoUser.getId(), todoUser.getUsername());
        return todoUserDTO;
    }

    public List<TodoUserDTO> convertToDTOList(List<TodoUser> todoUsers) {
        return todoUsers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
