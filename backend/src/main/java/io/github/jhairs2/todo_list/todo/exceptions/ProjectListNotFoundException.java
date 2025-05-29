package io.github.jhairs2.todo_list.todo.exceptions;

public class ProjectListNotFoundException extends RuntimeException {

    public ProjectListNotFoundException(String message) {
        super(message);
    }

    public ProjectListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
