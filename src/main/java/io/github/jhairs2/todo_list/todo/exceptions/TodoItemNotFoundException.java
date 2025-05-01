package io.github.jhairs2.todo_list.todo.exceptions;

public class TodoItemNotFoundException extends RuntimeException {

    public TodoItemNotFoundException(String message) {
        super(message);
    }

    public TodoItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
