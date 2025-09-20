package io.github.jhairs2.todo_list.service;

import static org.mockito.ArgumentMatchers.any;
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
import io.github.jhairs2.todo_list.todo.model.TodoUser;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;
import io.github.jhairs2.todo_list.todo.service.HelperService;
import io.github.jhairs2.todo_list.todo.service.ProjectListService;

@ExtendWith(MockitoExtension.class)
public class ProjectListServiceTests {

        @Mock
        private ProjectListRepository projectListRepository;

        @Mock
        private ProjectListDTOMapper projectListDTOMapper;

        @Mock
        private HelperService helperService;

        private ProjectList projectList1;
        private ProjectList projectList2;
        private ProjectListDTO projectList1DTO;
        private ProjectListDTO projectList2DTO;
        private List<ProjectList> testProjectList;
        private List<ProjectListDTO> testProjectListDTO;
        private TodoUser user;

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

                this.user = new TodoUser("Test", "pass");

        }

        @DisplayName("Test should return all ProjectLists")
        @Test
        void Get_IfProjectsExist_ReturnAllProjects() {

                when(this.helperService.getActiveUserId()).thenReturn(1L);
                when(this.projectListRepository.findAllByUserId(1L)).thenReturn(this.testProjectList);
                when(projectListDTOMapper.convertToDTOList(this.testProjectList))
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

                when(this.helperService.getActiveUserId()).thenReturn(1L);
                when(this.projectListRepository.findAllByUserId(1L)).thenReturn(List.of());
                when(projectListDTOMapper.convertToDTOList(List.of()))
                                .thenReturn(List.of());

                List<ProjectListDTO> results = this.projectListService.getAllProjectLists();

                Assertions.assertThat(results)
                                .hasSize(0)
                                .isEmpty();
        }

        @DisplayName("Test should return a single ProjectList that is requested")
        @Test
        void Get_WithValidProjectId_ReturnSingleProject() {

                when(this.helperService.getActiveUserId()).thenReturn(1L);
                when(this.projectListRepository.findByUserIdAndId(1L, 1L))
                                .thenReturn(Optional.of(this.projectList1));
                when(this.projectListDTOMapper.convertToDTO(this.projectList1)).thenReturn(this.projectList1DTO);

                ProjectListDTO results = this.projectListService.getProjectList(1L);

                Assertions.assertThat(results)
                                .isNotNull()
                                .isEqualTo(this.projectList1DTO);
        }

        @DisplayName("Test should throw exception when a requested list cannot be found")
        @Test
        void Get_WithInvalidProjectId_ThrowException() {

                when(this.helperService.getActiveUserId()).thenReturn(1L);
                when(this.projectListRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.empty());

                Assertions.assertThatThrownBy(() -> this.projectListService.getProjectList(1L))
                                .isInstanceOf(ProjectListNotFoundException.class);

        }

        @DisplayName("Test should add and return Project")
        @Test
        void Create_WithValidArgs_ReturnCreatedProject() {

                when(this.helperService.getActiveUser()).thenReturn(this.user);
                when(this.projectListRepository.save(any(ProjectList.class))).thenReturn(this.projectList1);
                when(this.projectListDTOMapper.convertToDTO(this.projectList1))
                                .thenReturn(this.projectList1DTO);

                ProjectListDTO results = this.projectListService.createNewProjectList(this.projectList1DTO);

                Assertions.assertThat(results)
                                .isNotNull()
                                .isEqualTo(this.projectList1DTO);

                verify(this.projectListRepository).save(any(ProjectList.class));

        }

        @DisplayName("Test should return an updated ProjectList")
        @Test
        void Update_WithValidArgs_ReturnUpdatedProject() {

                when(this.helperService.getActiveUserId()).thenReturn(1L);
                when(this.projectListRepository.findByUserIdAndId(1L, 1L))
                                .thenReturn(Optional.of(this.projectList1));

                when(this.projectListRepository.save(this.projectList1)).thenReturn(this.projectList1);
                when(this.projectListDTOMapper.convertToDTO(this.projectList1))
                                .thenReturn(new ProjectListDTO(this.projectList1.getId(),
                                                this.projectList2.getListTitle()));

                ProjectListDTO results = this.projectListService.updateProjectList(1L, projectList2DTO);

                Assertions.assertThat(results)
                                .isNotNull()
                                .extracting("id", "listTitle")
                                .containsExactly(this.projectList1.getId(), this.projectList1.getListTitle());

                verify(this.projectListRepository).save(this.projectList1);

        }

        @DisplayName("Test should throw exception when a requested list cannot be found")
        @Test
        void Update_WithInvalidProjectId_ThrowException() {

                when(this.helperService.getActiveUserId()).thenReturn(1L);
                when(this.projectListRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.empty());

                Assertions.assertThatThrownBy(() -> this.projectListService.updateProjectList(1L, this.projectList2DTO))
                                .isInstanceOf(ProjectListNotFoundException.class);

        }

        @DisplayName("Test should return the deleted list")
        @Test
        void Delete_WithValidProjectId_ReturnDeletedProject() {

                when(this.helperService.getActiveUserId()).thenReturn(1L);
                when(this.projectListRepository.findByUserIdAndId(1L, 1L))
                                .thenReturn(Optional.of(this.projectList1));
                when(this.projectListDTOMapper.convertToDTO(this.projectList1)).thenReturn(this.projectList1DTO);

                ProjectListDTO results = this.projectListService.deleteProjectList(1L);

                Assertions.assertThat(results)
                                .isNotNull()
                                .isEqualTo(this.projectList1DTO);

                verify(this.projectListRepository).delete(this.projectList1);
        }

        @DisplayName("Test should throw exception when a requested list cannot be found")
        @Test
        void Delete_WithInvalidProjectId_ThrowException() {

                when(this.helperService.getActiveUserId()).thenReturn(1L);
                when(this.projectListRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.empty());

                Assertions.assertThatThrownBy(() -> this.projectListService.deleteProjectList(1L))
                                .isInstanceOf(ProjectListNotFoundException.class);

        }

}
