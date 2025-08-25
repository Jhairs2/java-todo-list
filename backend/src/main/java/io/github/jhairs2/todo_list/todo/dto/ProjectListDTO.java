package io.github.jhairs2.todo_list.todo.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectListDTO(Long id, @NotBlank String listTitle) {

}
