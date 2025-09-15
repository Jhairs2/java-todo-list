package io.github.jhairs2.todo_list.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import io.github.jhairs2.todo_list.todo.dto.ProjectListDTO;
import io.github.jhairs2.todo_list.todo.service.ProjectListService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/projects")
public class ProjectListController {

    private final ProjectListService projectListService;

    @Autowired
    public ProjectListController(ProjectListService projectListService) {
        this.projectListService = projectListService;
    }

    @GetMapping
    public List<ProjectListDTO> getAllProjectLists() {
        return this.projectListService.getAllProjectLists();
    }

    @GetMapping(path = "/{projectListId}")
    public ProjectListDTO getProjectList(@PathVariable("projectListId") Long projectListId) {
        return this.projectListService.getProjectList(projectListId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectListDTO createNewProjectList(@Valid @RequestBody ProjectListDTO projectListDTO) {
        return this.projectListService.createNewProjectList(projectListDTO);
    }

    @PutMapping(path = "/{projectListId}")
    public ProjectListDTO updateProjectList(@PathVariable("projectListId") Long projectListId,
            @Valid @RequestBody ProjectListDTO projectListDTO) {
        return this.projectListService.updateProjectList(projectListId, projectListDTO);
    }

    @DeleteMapping(path = "/{projectListId}")
    public ProjectListDTO deleteProjectList(@PathVariable("projectListId") Long projectListId) {
        return this.projectListService.deleteProjectList(projectListId);
    }

}
