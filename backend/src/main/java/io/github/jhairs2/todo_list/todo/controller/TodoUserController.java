package io.github.jhairs2.todo_list.todo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhairs2.todo_list.todo.dto.LoginRequest;
import io.github.jhairs2.todo_list.todo.dto.RegisterRequest;
import io.github.jhairs2.todo_list.todo.dto.TodoUserDTO;
import io.github.jhairs2.todo_list.todo.service.TodoUserService;

@RestController
@RequestMapping(path = "/api/v1/accounts")
public class TodoUserController {

    private final TodoUserService todoUserService;

    public TodoUserController(TodoUserService todoUserService) {
        this.todoUserService = todoUserService;
    }

    @PostMapping(path = "/register")
    public TodoUserDTO registerUser(@RequestBody RegisterRequest registerRequest) {
        return this.todoUserService.registerUser(registerRequest);
    }

    @PostMapping(path = "/login")
    public String loginUser(@RequestBody LoginRequest loginRequest) {
        return this.todoUserService.loginUser(loginRequest);
    }

}
