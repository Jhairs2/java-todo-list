package io.github.jhairs2.todo_list.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhairs2.todo_list.todo.controller.ProjectListController;
import io.github.jhairs2.todo_list.todo.dto.ProjectListDTO;
import io.github.jhairs2.todo_list.todo.exceptions.ProjectListNotFoundException;
import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.service.ProjectListService;

@WebMvcTest(ProjectListController.class)
public class ProjectListControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProjectListService projectListService;

    private ProjectListDTO projectListDTO1;
    private ProjectListDTO projectListDTO2;
    private List<ProjectListDTO> projectDTOs = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.projectListDTO1 = new ProjectListDTO(1L, "Test1");
        this.projectListDTO2 = new ProjectListDTO(2L, "Test2");

        this.projectDTOs.add(this.projectListDTO1);
        this.projectDTOs.add(this.projectListDTO2);

    }

    @DisplayName("Test should return all available ProjectLists")
    @Test
    void shouldReturnAllProjectLists_IfListsExist() throws Exception {

        when(this.projectListService.getAllProjectLists()).thenReturn(this.projectDTOs);

        this.mockMvc.perform(get("/api/v1/projects"))
                .andDo(print())
                .andExpect(jsonPath("$[0].listTitle").value("Test1"))
                .andExpect(jsonPath("$[1].listTitle").value("Test2"))
                .andExpect(status().isOk());
    }

    @DisplayName("Test should return a empty list")
    @Test
    void shouldReturnEmptyList_IfNoListsExists() throws Exception {
        when(this.projectListService.getAllProjectLists()).thenReturn(List.of());

        this.mockMvc.perform(get("/api/v1/projects"))
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(status().isOk());
    }

    @DisplayName("Test should return a single ProjectList")
    @Test
    void shouldReturnSingleProjectList_ifListExists() throws Exception {
        when(this.projectListService.getProjectList(1L)).thenReturn(this.projectListDTO1);

        this.mockMvc.perform(get("/api/v1/projects/1"))
                .andDo(print())
                .andExpect(jsonPath("$.listTitle").value("Test1"))
                .andExpect(status().isOk());
    }

    @DisplayName("Test should return a ProjectListNotFoundException")
    @Test
    void shouldThrowException_IfListIsNotFound() throws Exception {
        when(this.projectListService.getProjectList(1L))
                .thenThrow(new ProjectListNotFoundException("Project with that id cannot be found"));

        this.mockMvc.perform(get("/api/v1/projects/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Project with that id cannot be found"));
    }

    @DisplayName("Test should return the updated project")
    @Test
    void shouldReturnUpdatedProjectList_IfListExists() throws Exception {

        ProjectList newProject = new ProjectList("New Test");
        newProject.setId(this.projectListDTO1.id());

        ProjectListDTO updatedProject = new ProjectListDTO(newProject.getId(), newProject.getListTitle());
        String requestBody = this.objectMapper.writeValueAsString(updatedProject);

        when(this.projectListService.updateProjectList(eq(1L), any(ProjectList.class)))
                .thenReturn(updatedProject);

        this.mockMvc.perform(put("/api/v1/projects/{projectListId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedProject.id()))
                .andExpect(jsonPath("$.listTitle").value(updatedProject.listTitle()));

    }

}
