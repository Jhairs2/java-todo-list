package io.github.jhairs2.todo_list.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import io.github.jhairs2.todo_list.todo.service.ProjectListService;
import io.github.jhairs2.todo_list.todo.service.TodoService;
import io.github.jhairs2.todo_list.todo.service.TodoUserService;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectListService projectListService;

    @MockitoBean
    private TodoUserService todoUserService;

    @MockitoBean
    private TodoService todoService;

    // Testing endpoints for unauthorized users

    @DisplayName("Should return unauthorized requests if user is not authorized")
    @Test
    void Get_IfUnauthorizedUser_ReturnUnauthorizedRequest() throws Exception {

        this.mockMvc.perform(get("/api/v1/projects"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Should return unauthorized if user sends post request and is not authenticated")
    @Test
    void Create_IfUnauthorizedUser_ReturnUnauthorizedRequest() throws Exception {

        this.mockMvc.perform(post("/api/v1/projects/{projectListid}/todos", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"task\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Should return unauthorized if user sends update request and is not authenticated")
    @Test
    void Update_IfUnauthorizedUser_ReturnUnauthorizedRequest() throws Exception {

        this.mockMvc.perform(put("/api/v1/projects/{projectListid}/todos/{todoItemId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"task\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Should return unauthorized if user sends delete request and is not authenticated")
    @Test
    void Delete_IfUnauthorizedUser_ReturnUnauthorizedRequest() throws Exception {

        this.mockMvc.perform(delete("/api/v1/projects/{projectListid}", 1L))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // Testing endpoints for authorized users

    @DisplayName("Should return 200 status for if user is authorized")
    @Test
    @WithMockUser(username = "Test", roles = "USER")
    void Get_IfAuthorizedUSER_ReturnStatusOk() throws Exception {

        this.mockMvc.perform(get("/api/v1/projects"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Should return status Created(201) if user sends post request and is authenticated")
    @Test
    @WithMockUser(username = "Test", roles = "USER")
    void Create_IfAuthorizedUser_ReturnStatusCreated() throws Exception {
        this.mockMvc.perform(post("/api/v1/projects/{projectListid}/todos", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"task\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("Should return 200 status if user sends update request and is authenticated")
    @Test
    @WithMockUser(username = "Test", roles = "USER")
    void Update_IfAuthorizedUser_ReturnStatusOk() throws Exception {

        this.mockMvc.perform(put("/api/v1/projects/{projectListid}/todos/{todoItemId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"task\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Should return 200 status if user sends delete request and is authenticated")
    @Test
    @WithMockUser(username = "Test", roles = "USER")
    void Delete_IfAuthorizedUser_ReturnStatusOk() throws Exception {

        this.mockMvc.perform(delete("/api/v1/projects/{projectListid}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // Testing Roles

    @DisplayName("Should return 403 status if user roles are invalid")
    @Test
    @WithMockUser(username = "Test", roles = "BADROLE")
    void Get_IfInvalidRoles_ReturnStatusForbidden() throws Exception {
        this.mockMvc.perform(get("/api/v1/projects"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("Should return 200 status if user role is ADMIN")
    @Test
    @WithMockUser(username = "Test", roles = "ADMIN")
    void Get_IfAuthorizedADMIN_ReturnStatusOk() throws Exception {

        this.mockMvc.perform(get("/api/v1/projects"))
                .andDo(print())

                .andExpect(status().isOk());
    }

    // Testing Public Endpoints

    @DisplayName("Should return 200 status for login request {PUBLIC ENDPOINT}")
    @Test
    void Post_AnyLoginRequest_ReturnStatusOk() throws Exception {
        this.mockMvc.perform(post("/api/v1/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"joe\", \"password\": \"354khkh34DFH\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Should return 200 status for register request {PUBLIC ENDPOINT}")
    @Test
    void Post_AnyRegisterRequest_ReturnStatusOk() throws Exception {

        this.mockMvc.perform(post("/api/v1/accounts/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"joe\", \"password\": \"354khkh34DFH\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
