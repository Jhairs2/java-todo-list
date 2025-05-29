package io.github.jhairs2.todo_list.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.jhairs2.todo_list.todo.model.ProjectList;

@Repository
public interface ProjectListRepository extends JpaRepository<ProjectList, Long> {

}
