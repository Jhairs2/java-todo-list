package io.github.jhairs2.todo_list.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.jhairs2.todo_list.todo.dto.ProjectListDTO;
import io.github.jhairs2.todo_list.todo.mapper.ProjectListDTOMapper;
import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;
import io.github.jhairs2.todo_list.todo.service.ProjectListService;

@ExtendWith(MockitoExtension.class)
public class ProjectListServiceTests {

    @Mock
    private ProjectListRepository projectListRepository;

    @Mock
    private ProjectListDTOMapper projectListDTOMapper;

    private ProjectList projectList1;
    private ProjectList projectList2;
    private ProjectListDTO projectList1DTO;
    private ProjectListDTO projectList2DTO;
    private List<ProjectList> testProjectList;
    private List<ProjectListDTO> testProjectListDTO;

    @InjectMocks
    private ProjectListService projectListService;

    @BeforeEach
    void setUp() {
        this.projectList1 = new ProjectList("TestProject1");
        this.projectList1.setId(1L);

        this.projectList2 = new ProjectList("TestProject2");
        this.projectList2.setId(2L);

        this.projectList1DTO = new ProjectListDTO(this.projectList1.getId(), this.projectList1.getListTitle());
        this.projectList2DTO = new ProjectListDTO(this.projectList2.getId(), this.projectList2.getListTitle());

        this.testProjectList = List.of(this.projectList1, this.projectList2);
        this.testProjectListDTO = List.of(this.projectList1DTO, this.projectList2DTO);

    }

    @DisplayName("Test should return all ProjectLists")
    @Test
    void shouldReturnAllProjectLists_IfListsExist() {

        when(this.projectListRepository.findAll()).thenReturn(this.testProjectList);

        when(projectListDTOMapper.convertProjectsToDTOList(this.testProjectList))
                .thenReturn(this.testProjectListDTO);

        List<ProjectListDTO> results = this.projectListService.getAllProjectLists();

        Assertions.assertThat(results)
                .hasSize(2)
                .extracting(ProjectListDTO::listTitle)
                .containsExactly(this.projectList1.getListTitle(), this.projectList2.getListTitle());

    }

}
