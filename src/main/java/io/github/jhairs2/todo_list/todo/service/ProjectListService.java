package io.github.jhairs2.todo_list.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.jhairs2.todo_list.todo.dto.ProjectListDTO;
import io.github.jhairs2.todo_list.todo.mapper.ProjectListDTOMapper;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;

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
                        .orElseThrow(() -> new IllegalArgumentException("Project with that id does not exist")));
    }

}
