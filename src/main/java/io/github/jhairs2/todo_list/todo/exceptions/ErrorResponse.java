package io.github.jhairs2.todo_list.todo.exceptions;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, Throwable throwable, HttpStatus httpStatus) {

}
