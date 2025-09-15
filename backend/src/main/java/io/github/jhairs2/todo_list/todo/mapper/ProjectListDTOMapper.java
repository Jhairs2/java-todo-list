package io.github.jhairs2.todo_list.todo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.github.jhairs2.todo_list.todo.dto.ProjectListDTO;
import io.github.jhairs2.todo_list.todo.model.ProjectList;

@Component
public class ProjectListDTOMapper {

    public ProjectListDTO convertToDTO(ProjectList projectList) {
        ProjectListDTO projectListDTO = new ProjectListDTO(projectList.getId(), projectList.getListTitle());
        return projectListDTO;
    }

    public List<ProjectListDTO> convertToDTOList(List<ProjectList> projectLists) {
        return projectLists.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
