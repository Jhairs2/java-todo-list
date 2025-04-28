package io.github.jhairs2.todo_list.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;

@Service
public class ProjectListService {

    private final ProjectListRepository projectListRepository;

    @Autowired
    public ProjectListService(ProjectListRepository projectListRepository) {
        this.projectListRepository = projectListRepository;
    }

    public List<ProjectList> getAllProjectLists() {

        return this.projectListRepository.findAll();

    }

}
