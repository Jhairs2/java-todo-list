package io.github.jhairs2.todo_list.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhairs2.todo_list.todo.controller.ProjectListController;
import io.github.jhairs2.todo_list.todo.dto.ProjectListDTO;
import io.github.jhairs2.todo_list.todo.exceptions.ProjectListNotFoundException;
import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.service.JWTService;
import io.github.jhairs2.todo_list.todo.service.ProjectListService;

@WebMvcTest(ProjectListController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjectListControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private ProjectListService projectListService;

        @MockitoBean
        private JWTService jwtService;

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
        void Get_IfProjectsExist_ReturnAllProjects() throws Exception {

                when(this.projectListService.getAllProjectLists()).thenReturn(this.projectDTOs);

                this.mockMvc.perform(get("/api/v1/projects"))
                                .andDo(print())
                                .andExpect(jsonPath("$[0].listTitle").value("Test1"))
                                .andExpect(jsonPath("$[1].listTitle").value("Test2"))
                                .andExpect(status().isOk());
        }

        @DisplayName("Test should return a empty list")
        @Test
        void Get_IfProjectsDoNotExist_ReturnEmptyList() throws Exception {
                when(this.projectListService.getAllProjectLists()).thenReturn(List.of());

                this.mockMvc.perform(get("/api/v1/projects"))
                                .andDo(print())
                                .andExpect(jsonPath("$.length()").value(0))
                                .andExpect(status().isOk());
        }

        @DisplayName("Test should return a single ProjectList")
        @Test
        void Get_WithValidProjectId_ReturnSingleProject() throws Exception {
                when(this.projectListService.getProjectList(1L)).thenReturn(this.projectListDTO1);

                this.mockMvc.perform(get("/api/v1/projects/1"))
                                .andDo(print())
                                .andExpect(jsonPath("$.listTitle").value("Test1"))
                                .andExpect(status().isOk());
        }

        @DisplayName("Test should return a ProjectListNotFoundException")
        @Test
        void Get_WithInvalidProjectId_ThrowException() throws Exception {
                when(this.projectListService.getProjectList(1L))
                                .thenThrow(new ProjectListNotFoundException("Project with that id cannot be found"));

                this.mockMvc.perform(get("/api/v1/projects/1"))
                                .andDo(print())
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Project with that id cannot be found"));
        }

        @DisplayName("Test should return the created projectList")
        @Test
        void Create_WithValidArgs_ReturnCreatedProject() throws Exception {
                String requestBody = this.objectMapper.writeValueAsString(this.projectListDTO1);

                when(this.projectListService.createNewProjectList(any(ProjectListDTO.class)))
                                .thenReturn(new ProjectListDTO(3L, this.projectListDTO1.listTitle()));

                this.mockMvc.perform(post("/api/v1/projects")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.listTitle").value(this.projectListDTO1.listTitle()));

        }

        @DisplayName("Test should return the updated project")
        @Test
        void Update_WithValidArgs_ReturnUpdatedProject() throws Exception {

                ProjectList newProject = new ProjectList("New Test");
                newProject.setId(this.projectListDTO1.id());

                ProjectListDTO updatedProject = new ProjectListDTO(newProject.getId(), newProject.getListTitle());
                String requestBody = this.objectMapper.writeValueAsString(updatedProject);

                when(this.projectListService.updateProjectList(eq(1L), any(ProjectListDTO.class)))
                                .thenReturn(updatedProject);

                this.mockMvc.perform(put("/api/v1/projects/{projectListId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(updatedProject.id()))
                                .andExpect(jsonPath("$.listTitle").value(updatedProject.listTitle()));

        }

        @DisplayName("Test should throw a ProjectNotFoundException if List to update is not found")
        @Test
        void Update_WithInvalidProjectId_ThrowException() throws Exception {
                ProjectList emptyProjectList = new ProjectList("New Test");

                when(this.projectListService.updateProjectList(anyLong(), any(ProjectListDTO.class)))
                                .thenThrow(new ProjectListNotFoundException("Project with that id does not exist"));

                this.mockMvc.perform(put("/api/v1/projects/{projectListId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(emptyProjectList)))
                                .andDo(print())
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Project with that id does not exist"));
        }

        @DisplayName("Test should return deleted project")
        @Test
        void Delete_WithValidProjectId_ReturnDeletedProject() throws Exception {

                when(this.projectListService.deleteProjectList(1L))
                                .thenReturn(this.projectListDTO1);

                this.mockMvc.perform(delete("/api/v1/projects/{projectListId}", 1L))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(this.projectListDTO1.id()))
                                .andExpect(jsonPath("$.listTitle").value(this.projectListDTO1.listTitle()));

        }

        @DisplayName("Test should throw a ProjectNotFOundException if List to delete is not found")
        @Test
        void Delete_WithInvalidProjectId_ThrowException() throws Exception {

                when(this.projectListService.deleteProjectList(1L))
                                .thenThrow(new ProjectListNotFoundException("Project with that id does not exist"));

                this.mockMvc.perform(delete("/api/v1/projects/{projectListId}", 1L))
                                .andDo(print())
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Project with that id does not exist"));
        }

}
