package io.github.jhairs2.todo_list.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.jhairs2.todo_list.todo.dto.ProjectListDTO;
import io.github.jhairs2.todo_list.todo.exceptions.ProjectListNotFoundException;
import io.github.jhairs2.todo_list.todo.mapper.ProjectListDTOMapper;
import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;
import jakarta.transaction.Transactional;

@Service
public class ProjectListService {

    private final ProjectListRepository projectListRepository;
    private final ProjectListDTOMapper projectListDTOMapper;

    @Autowired
    public ProjectListService(ProjectListRepository projectListRepository, ProjectListDTOMapper projectListDTOMapper) {
        this.projectListRepository = projectListRepository;
        this.projectListDTOMapper = projectListDTOMapper;
    }

    public List<ProjectListDTO> getAllProjectLists() {

        return this.projectListDTOMapper.convertProjectsToDTOList(this.projectListRepository.findAll());

    }

    public ProjectListDTO getProjectList(Long id) {
        return this.projectListDTOMapper.convertProjectToDTO(
                this.projectListRepository.findById(id)
                        .orElseThrow(() -> new ProjectListNotFoundException("Project with that id can not be found.")));
    }

    public ProjectListDTO createNewProjectList(ProjectList projectList) {
        if (projectList == null) {
            throw new IllegalArgumentException();
        }
        return this.projectListDTOMapper.convertProjectToDTO(this.projectListRepository.save(projectList));
    }

    @Transactional
    public ProjectListDTO updateProjectList(Long id, ProjectList projectList) {
        ProjectList newProject = this.projectListRepository.findById(id)
                .orElseThrow(() -> new ProjectListNotFoundException("Project with that id can not be found."));

        newProject.setListTitle(projectList.getListTitle());

        return this.projectListDTOMapper.convertProjectToDTO(this.projectListRepository.save(newProject));
    }

    public ProjectListDTO deleteProjectList(Long id) {
        ProjectList deletedProject = this.projectListRepository.findById(id)
                .orElseThrow(() -> new ProjectListNotFoundException("Project with that id can not be found."));

        this.projectListRepository.delete(deletedProject);

        return this.projectListDTOMapper.convertProjectToDTO(deletedProject);
    }

}
