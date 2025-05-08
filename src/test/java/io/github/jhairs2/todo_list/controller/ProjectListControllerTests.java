package io.github.jhairs2.todo_list.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import io.github.jhairs2.todo_list.todo.controller.ProjectListController;
import io.github.jhairs2.todo_list.todo.dto.ProjectListDTO;
import io.github.jhairs2.todo_list.todo.service.ProjectListService;

@WebMvcTest(ProjectListController.class)
public class ProjectListControllerTests {

    @Autowired
    private MockMvc mockMvc;

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
                .andDo(print()).andExpect(status().isOk());
    }

}
