package io.github.jhairs2.todo_list.todo.exceptions;

public class InvalidUserNamePasswordException extends IllegalArgumentException {

    public InvalidUserNamePasswordException(String message) {
        super(message);
    }

    public InvalidUserNamePasswordException(String message, Throwable cause) {
        super(message, cause);
    }

}
