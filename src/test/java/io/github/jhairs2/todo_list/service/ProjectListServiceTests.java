package io.github.jhairs2.todo_list.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.jhairs2.todo_list.todo.dto.ProjectListDTO;
import io.github.jhairs2.todo_list.todo.exceptions.ProjectListNotFoundException;
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
        void Get_IfProjectsExist_ReturnAllProjects() {

                when(this.projectListRepository.findAll()).thenReturn(this.testProjectList);

                when(projectListDTOMapper.convertProjectsToDTOList(this.testProjectList))
                                .thenReturn(this.testProjectListDTO);

                List<ProjectListDTO> results = this.projectListService.getAllProjectLists();

                Assertions.assertThat(results)
                                .hasSize(2)
                                .extracting(ProjectListDTO::listTitle)
                                .containsExactly(this.projectList1.getListTitle(), this.projectList2.getListTitle());

        }

        @DisplayName("Test should return an empty list")
        @Test
        void Get_IfProjectsDoNotExist_ReturnEmptyList() {
                when(this.projectListRepository.findAll()).thenReturn(List.of());

                when(projectListDTOMapper.convertProjectsToDTOList(List.of()))
                                .thenReturn(List.of());

                List<ProjectListDTO> results = this.projectListService.getAllProjectLists();

                Assertions.assertThat(results)
                                .hasSize(0)
                                .isEmpty();
        }

        @DisplayName("Test should return a single ProjectList that is requested")
        @Test
        void Get_WithValidId_ReturnSingleProject() {

                when(this.projectListRepository.findById(1L)).thenReturn(Optional.of(this.projectList1));
                when(this.projectListDTOMapper.convertProjectToDTO(this.projectList1)).thenReturn(this.projectList1DTO);

                ProjectListDTO results = this.projectListService.getProjectList(1L);

                Assertions.assertThat(results)
                                .isNotNull()
                                .isEqualTo(this.projectList1DTO);
        }

        @DisplayName("Test should throw exception when a requested list cannot be found")
        @Test
        void Get_WithInvalidId_ThrowException() {
                when(this.projectListRepository.findById(1L)).thenReturn(Optional.empty());

                Assertions.assertThatThrownBy(() -> this.projectListService.getProjectList(1L))
                                .isInstanceOf(ProjectListNotFoundException.class)
                                .hasMessage("Project with that id can not be found.");

        }

        @DisplayName("Test should return an updated ProjectList")
        @Test
        void Update_WithValidArgs_ReturnUpdatedProject() {
                when(this.projectListRepository.findById(1L)).thenReturn(Optional.of(this.projectList1));

                this.projectList1.setListTitle(this.projectList2.getListTitle());

                ProjectListDTO updatedProject = new ProjectListDTO(this.projectList1.getId(),
                                this.projectList1.getListTitle());

                when(this.projectListRepository.save(this.projectList1)).thenReturn(this.projectList1);
                when(this.projectListDTOMapper.convertProjectToDTO(this.projectList1))
                                .thenReturn(updatedProject);

                ProjectListDTO results = this.projectListService.updateProjectList(1L, projectList2);

                Assertions.assertThat(results)
                                .isNotNull()
                                .extracting("id")
                                .isEqualTo(this.projectList1.getId());

                Assertions.assertThat(results)
                                .extracting("listTitle")
                                .isEqualTo(projectList2DTO.listTitle());

                verify(this.projectListRepository).save(this.projectList1);

        }

        @DisplayName("Test should throw exception when a requested list cannot be found")
        @Test
        void Update_WithInvalidId_ThrowException() {
                when(this.projectListRepository.findById(1L)).thenReturn(Optional.empty());

                Assertions.assertThatThrownBy(() -> this.projectListService.updateProjectList(1L, this.projectList2))
                                .isInstanceOf(ProjectListNotFoundException.class)
                                .hasMessage("Project with that id can not be found.");

        }

        @DisplayName("Test should return the deleted list")
        @Test
        void Delete_WithValidId_ReturnDeletedProject() {
                when(this.projectListRepository.findById(1L)).thenReturn(Optional.of(this.projectList1));

                when(this.projectListDTOMapper.convertProjectToDTO(this.projectList1)).thenReturn(this.projectList1DTO);

                ProjectListDTO results = this.projectListService.deleteProjectList(1L);

                Assertions.assertThat(results)
                                .isNotNull()
                                .isEqualTo(this.projectList1DTO);

                verify(this.projectListRepository).delete(this.projectList1);
        }

        @DisplayName("Test should throw exception when a requested list cannot be found")
        @Test
        void Delete_WithInvalidId_ThrowException() {
                when(this.projectListRepository.findById(1L)).thenReturn(Optional.empty());

                Assertions.assertThatThrownBy(() -> this.projectListService.deleteProjectList(1L))
                                .isInstanceOf(ProjectListNotFoundException.class)
                                .hasMessage("Project with that id can not be found.");

        }

}
