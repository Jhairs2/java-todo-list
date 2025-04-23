package io.github.jhairs2.todo_list.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.jhairs2.todo_list.todo.model.TodoItem;

@Repository
public interface TodoRepository extends JpaRepository<TodoItem, Long> {

}
