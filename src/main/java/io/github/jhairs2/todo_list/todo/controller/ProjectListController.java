package io.github.jhairs2.todo_list.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.service.ProjectListService;

@RestController
@RequestMapping(path = "/api/v1/projects")
public class ProjectListController {

    private final ProjectListService projectListService;

    @Autowired
    public ProjectListController(ProjectListService projectListService) {
        this.projectListService = projectListService;
    }

    @GetMapping
    public List<ProjectList> getAllProjectLists() {
        return this.projectListService.getAllProjectLists();
    }
}
