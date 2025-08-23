package io.github.jhairs2.todo_list.todo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.jhairs2.todo_list.todo.model.TodoUser;

@Repository
public interface TodoUserRepository extends JpaRepository<TodoUser, Long> {

    Optional<TodoUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
