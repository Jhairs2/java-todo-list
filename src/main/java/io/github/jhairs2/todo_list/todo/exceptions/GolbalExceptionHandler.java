package io.github.jhairs2.todo_list.todo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GolbalExceptionHandler {

    @ExceptionHandler(value = ProjectListNotFoundException.class)
    public ResponseEntity<Object> handleProjectListNotFoundException(
            ProjectListNotFoundException projectListNotFoundException) {
        ErrorResponse errorResponse = new ErrorResponse(
                projectListNotFoundException.getMessage(),
                projectListNotFoundException.getCause(),
                HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
