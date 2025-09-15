package io.github.jhairs2.todo_list.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.model.TodoItem;

import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;
import io.github.jhairs2.todo_list.todo.repository.TodoRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TodoRepositoryTests {

    @Autowired
    ProjectListRepository projectListRepository;

    @Autowired
    TodoRepository todoRepository;

    ProjectList project1;
    ProjectList project2;
    TodoItem todoItem1;

    @BeforeEach
    void setUp() {
        this.project1 = new ProjectList("TEST1");
        this.project2 = new ProjectList("TEST2");

        this.todoItem1 = new TodoItem("Test Task");

        this.todoItem1.setList(this.project1);

        this.projectListRepository.saveAll(List.of(this.project1, this.project2));
        this.todoRepository.save(this.todoItem1);

    }

    @DisplayName("Should return the tasks of projectList with correct id")
    @Test
    void Query_IfValidData_ReturnTasks() throws Exception {

        Optional<TodoItem> results = this.todoRepository.findByListIdAndId(this.project1.getId(),
                this.todoItem1.getId());

        Assertions.assertThat(results)
                .isNotEmpty()
                .isEqualTo(Optional.of(this.todoItem1));
    }

    @DisplayName("Should return empty optional whenever no results are found")
    @Test
    void Query_IfInvalidData_ReturnEmptyOptional() throws Exception {

        Optional<TodoItem> results = this.todoRepository.findByListIdAndId(2L,
                this.todoItem1.getId());

        Assertions.assertThat(results)
                .isEmpty();
    }

}
