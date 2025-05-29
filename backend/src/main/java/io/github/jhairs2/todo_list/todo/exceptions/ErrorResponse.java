package io.github.jhairs2.todo_list.todo.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, HttpStatus httpStatus, LocalDateTime timestamp) {

}
