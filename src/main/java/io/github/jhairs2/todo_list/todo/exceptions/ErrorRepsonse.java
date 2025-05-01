package io.github.jhairs2.todo_list.todo.exceptions;

import org.springframework.http.HttpStatus;

public record ErrorRepsonse(String message, Throwable throwable, HttpStatus httpStatus) {

}
