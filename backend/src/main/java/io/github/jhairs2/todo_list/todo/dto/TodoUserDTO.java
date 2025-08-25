package io.github.jhairs2.todo_list.todo.dto;

import jakarta.validation.constraints.NotBlank;

public record TodoUserDTO(Long id, @NotBlank String username) {

}
