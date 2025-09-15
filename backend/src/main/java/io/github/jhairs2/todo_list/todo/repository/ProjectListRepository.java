package io.github.jhairs2.todo_list.todo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.jhairs2.todo_list.todo.model.ProjectList;

@Repository
public interface ProjectListRepository extends JpaRepository<ProjectList, Long> {

    List<ProjectList> findAllByUserId(Long id);

    Optional<ProjectList> findByUserIdAndId(Long userId, Long id);

    boolean existsByUserIdAndId(Long userId, Long id);

}
