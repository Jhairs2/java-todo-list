package io.github.jhairs2.todo_list.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhairs2.todo_list.todo.controller.TodoUserController;
import io.github.jhairs2.todo_list.todo.dto.LoginRequest;
import io.github.jhairs2.todo_list.todo.dto.RegisterRequest;
import io.github.jhairs2.todo_list.todo.dto.TodoUserDTO;
import io.github.jhairs2.todo_list.todo.exceptions.InvalidUserNamePasswordException;
import io.github.jhairs2.todo_list.todo.service.TodoUserService;
import io.github.jhairs2.todo_list.todo.dto.Token;

@WebMvcTest(TodoUserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TodoUserControllerTests {

        @Autowired
        MockMvc mockMvc;

        @Autowired
        ObjectMapper objectMapper;

        @MockitoBean
        TodoUserService todoUserService;

        LoginRequest loginRequest;
        RegisterRequest registerRequest;
        Token token;

        @BeforeEach
        void setUp() {

                this.loginRequest = new LoginRequest("test", "pas12345678");
                this.registerRequest = new RegisterRequest("test", "pas12345678");
                this.token = new Token("TOKEN");
        }

        @DisplayName("Should return a TodoUserDTO when user registers with valid data")
        @Test
        void Register_IfValidData_RegisterAndReturnTodoUserDTO() throws Exception {

                String requestBody = this.objectMapper.writeValueAsString(registerRequest);
                when(this.todoUserService.registerUser(this.registerRequest))
                                .thenReturn(new TodoUserDTO(1L, this.registerRequest.username()));

                this.mockMvc.perform(post("/api/v1/accounts/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.username").value(this.registerRequest.username()));
        }

        @DisplayName("Should return server error if register request is missing username")
        @Test
        void Register_IfNullUsername_ReturnStatusInternalServerError() throws Exception {

                String requestBody = this.objectMapper.writeValueAsString(new RegisterRequest(null, "aadfaa314"));

                this.mockMvc.perform(post("/api/v1/accounts/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isInternalServerError());

        }

        @DisplayName("Should return server error if register request is missing password")
        @Test
        void Register_IfNullPassword_ReturnStatusInternalServerError() throws Exception {

                String requestBody = this.objectMapper.writeValueAsString(new RegisterRequest("test", null));

                this.mockMvc.perform(post("/api/v1/accounts/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isInternalServerError());

        }

        @DisplayName("Should return exception if register request has invalid password")
        @Test
        void Register_IfInvalidPassword_ReturnStatusBadRequest() throws Exception {

                String requestBody = this.objectMapper.writeValueAsString(new RegisterRequest("test", "afafafa"));

                when(this.todoUserService.registerUser(any(RegisterRequest.class)))
                                .thenThrow(new InvalidUserNamePasswordException("password does not meet requirements"));

                this.mockMvc.perform(post("/api/v1/accounts/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isBadRequest());

        }

        @DisplayName("Should return exception if register request has invalid username")
        @Test
        void Register_IfInvalidUsername_ReturnStatusBadRequest() throws Exception {

                String requestBody = this.objectMapper.writeValueAsString(new RegisterRequest("test", "afafafa"));

                when(this.todoUserService.registerUser(any(RegisterRequest.class)))
                                .thenThrow(new InvalidUserNamePasswordException("Username already exists"));

                this.mockMvc.perform(post("/api/v1/accounts/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isBadRequest());

        }

        @DisplayName("Should return a Token when user logs in with valid data")
        @Test
        void Login_IfValidData_LoginAndReturnToken() throws Exception {

                String requestBody = this.objectMapper.writeValueAsString(this.loginRequest);
                when(this.todoUserService.loginUser(any(LoginRequest.class)))
                                .thenReturn(this.token);

                this.mockMvc.perform(post("/api/v1/accounts/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").value(this.token.token()));
        }

        @DisplayName("Should return server error if login request is missing username")
        @Test
        void Login_IfNullUsername_ReturnStatusInternalServerError() throws Exception {

                String requestBody = this.objectMapper.writeValueAsString(new LoginRequest(null, "aadfaa314"));

                this.mockMvc.perform(post("/api/v1/accounts/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isInternalServerError());

        }

        @DisplayName("Should return server error if login request is missing password")
        @Test
        void Login_IfNullPassword_ReturnStatusInternalServerError() throws Exception {

                String requestBody = this.objectMapper.writeValueAsString(new LoginRequest("test", null));

                this.mockMvc.perform(post("/api/v1/accounts/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isInternalServerError());

        }

        @DisplayName("Should return exception if login request has invalid password")
        @Test
        void Login_IfInvalidPassword_ReturnStatusBadRequest() throws Exception {

                String requestBody = this.objectMapper.writeValueAsString(new LoginRequest("test", "afafafa"));

                when(this.todoUserService.loginUser(any(LoginRequest.class)))
                                .thenThrow(new BadCredentialsException("Bad Credentials"));

                this.mockMvc.perform(post("/api/v1/accounts/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isUnauthorized());

        }

        @DisplayName("Should return exception if login request has invalid username")
        @Test
        void Login_IfInvalidUsername_ReturnStatusBadRequest() throws Exception {

                String requestBody = this.objectMapper.writeValueAsString(new RegisterRequest("test", "afafafa"));

                when(this.todoUserService.loginUser(any(LoginRequest.class)))
                                .thenThrow(new BadCredentialsException("Bad Credentials"));

                this.mockMvc.perform(post("/api/v1/accounts/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isUnauthorized());

        }

}
