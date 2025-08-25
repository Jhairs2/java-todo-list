package io.github.jhairs2.todo_list.todo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.jhairs2.todo_list.todo.dto.LoginRequest;
import io.github.jhairs2.todo_list.todo.dto.RegisterRequest;
import io.github.jhairs2.todo_list.todo.dto.TodoUserDTO;
import io.github.jhairs2.todo_list.todo.exceptions.InvalidUserNamePasswordException;
import io.github.jhairs2.todo_list.todo.mapper.TodoUserDTOMapper;
import io.github.jhairs2.todo_list.todo.model.TodoUser;
import io.github.jhairs2.todo_list.todo.repository.TodoUserRepository;

@Service
public class TodoUserService {

    private static final Logger logger = LoggerFactory.getLogger(TodoUserService.class);

    private final TodoUserRepository todoUserRepository;
    private final TodoUserDTOMapper todoUserDTOMapper;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public TodoUserService(TodoUserRepository todoUserRepository, TodoUserDTOMapper todoUserDTOMapper,
            PasswordEncoder encoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.todoUserRepository = todoUserRepository;
        this.todoUserDTOMapper = todoUserDTOMapper;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public TodoUserDTO registerUser(RegisterRequest registerRequest) {

        logger.info("Making sure username and password are valid...");

        if (!isPasswordValid(registerRequest.password())) {
            logger.error("password is invalid");
            throw new InvalidUserNamePasswordException("password is not at least 12 characters");
        }

        logger.info("Ensuring username is not already taken...");
        if (isUsernameTaken(registerRequest.username())) {
            logger.info("Username is already taken");
            throw new InvalidUserNamePasswordException("Username is already taken");
        }

        TodoUser newUser = new TodoUser(registerRequest.username(), encoder.encode(registerRequest.password()));

        logger.info("Adding user to DB");
        return this.todoUserDTOMapper.convertToDTO(this.todoUserRepository.save(newUser));

    }

    public String loginUser(LoginRequest loginRequest) {
        logger.info("Authenticating user {}...", loginRequest.username());
        Authentication auth = authenticateUser(loginRequest);

        logger.info("User Authenticated issuing token...");
        return jwtService.generateToken(loginRequest.username(), auth.getAuthorities());

    }

    private Authentication authenticateUser(LoginRequest loginRequest) {
        return this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
    }

    private boolean isUsernameTaken(String username) {
        return this.todoUserRepository.existsByUsername(username);
    }

    private boolean isPasswordValid(String password) {

        return password.length() >= 12
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*\\d.*");
    }

}
