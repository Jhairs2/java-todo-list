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

    @Mock
    private ProjectList projectList;

    @InjectMocks
    private ProjectListService projectListService;

    @BeforeEach
    void setUp() {
        this.projectList = new ProjectList("TestProject");

    }

    @DisplayName("Test should return all ProjectLists")
    @Test
    void getAllProjectListTest_ShouldReturnAllProjects() {

        when(this.projectListRepository.findAll()).thenReturn(List.of(this.projectList));
        when(projectListDTOMapper.convertProjectsToDTOList(List.of(this.projectList)))
                .thenReturn(List.of(new ProjectListDTO(this.projectList.getId(), this.projectList.getListTitle())));

        List<ProjectListDTO> results = this.projectListService.getAllProjectLists();

        Assertions.assertThat(results.get(0).projectTitle()).isEqualTo(this.projectList.getListTitle());
        assertEquals(1, results.size());

    }

}
