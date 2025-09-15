package io.github.jhairs2.todo_list.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.jhairs2.todo_list.todo.dto.LoginRequest;
import io.github.jhairs2.todo_list.todo.dto.RegisterRequest;
import io.github.jhairs2.todo_list.todo.dto.TodoUserDTO;
import io.github.jhairs2.todo_list.todo.dto.Token;
import io.github.jhairs2.todo_list.todo.exceptions.InvalidUserNamePasswordException;
import io.github.jhairs2.todo_list.todo.mapper.TodoUserDTOMapper;
import io.github.jhairs2.todo_list.todo.model.TodoUser;
import io.github.jhairs2.todo_list.todo.repository.TodoUserRepository;
import io.github.jhairs2.todo_list.todo.service.JWTService;
import io.github.jhairs2.todo_list.todo.service.TodoUserService;

@ExtendWith(MockitoExtension.class)
public class TodoUserServiceTests {

    @Mock
    private TodoUserRepository todoUserRepository;

    @Mock
    private TodoUserDTOMapper todoUserDTOMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private TodoUserService todoUserService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private Authentication auth;

    @BeforeEach
    void setup() {

        this.registerRequest = new RegisterRequest("Test", "testPass1234");
        this.loginRequest = new LoginRequest("Test", "testpass1234");

        this.auth = new UsernamePasswordAuthenticationToken(
                this.loginRequest.username(),
                this.loginRequest.password(),
                List.of(new SimpleGrantedAuthority("USER")));

    }

    @DisplayName("Should save and return newly registered user")
    @Test
    void Register_IfValidRegisterRequest_ReturnRegisteredUser() throws Exception {

        when(this.passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(this.todoUserRepository.save(any(TodoUser.class)))
                .thenReturn(new TodoUser(this.registerRequest.username(), "encodedPassword"));
        when(this.todoUserDTOMapper.convertToDTO(any(TodoUser.class)))
                .thenReturn(new TodoUserDTO(1L, this.registerRequest.username()));

        TodoUserDTO results = this.todoUserService.registerUser(this.registerRequest);

        Assertions.assertThat(results)
                .isNotNull()
                .extracting("username")
                .isEqualTo(this.registerRequest.username());

        verify(this.todoUserRepository).save(any(TodoUser.class));

    }

    @DisplayName("Should return exception if username is already taken")
    @Test
    void Register_IfInvalidUsername_ReturnException() throws Exception {

        when(this.todoUserRepository.existsByUsername("Test")).thenReturn(true);

        Assertions.assertThatThrownBy(() -> this.todoUserService.registerUser(this.registerRequest))
                .isInstanceOf(InvalidUserNamePasswordException.class);

    }

    @DisplayName("Should return exception if password is invalid")
    @ParameterizedTest()
    @ValueSource(strings = { "", "ade", "ad12", "Ad23", "testpass1234" })
    void Register_IfInvalidPassword_ReturnException(String password) throws Exception {

        Assertions
                .assertThatThrownBy(() -> this.todoUserService.registerUser(new RegisterRequest("Test", password)))
                .isInstanceOf(InvalidUserNamePasswordException.class);

    }

    @DisplayName("Should save and return token when user is authenticated")
    @Test
    void Login_IfValidLoginRequest_ReturnToken() throws Exception {

        when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(this.auth);

        when(this.jwtService.generateToken(this.loginRequest.username(), this.auth.getAuthorities()))
                .thenReturn("token");

        Token results = this.todoUserService.loginUser(this.loginRequest);

        Assertions.assertThat(results)
                .isNotNull()
                .extracting("token")
                .isEqualTo("token");

    }

    @DisplayName("Should return Exception when user cannot be authenticated")
    @Test
    void Login_IfInvalidLoginRequest_ReturnException() throws Exception {

        when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        Assertions
                .assertThatThrownBy(() -> this.todoUserService.loginUser(this.loginRequest))
                .isInstanceOf(AuthenticationException.class);

    }

}
