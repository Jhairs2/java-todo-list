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
import io.github.jhairs2.todo_list.todo.model.TodoUser;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;
import io.github.jhairs2.todo_list.todo.repository.TodoUserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProjectListRepositoryTests {

        @Autowired
        ProjectListRepository projectListRepository;

        @Autowired
        TodoUserRepository todoUserRepository;

        ProjectList project1;
        ProjectList project2;
        TodoUser todoUser1;
        TodoUser todoUser2;

        @BeforeEach
        void setUp() {
                this.project1 = new ProjectList("TEST1");
                this.project2 = new ProjectList("TEST2");

                this.todoUser1 = new TodoUser("testUser1", "pass1", "USER");
                this.todoUser2 = new TodoUser("testUser2", "pass2", "USER");

                this.project1.setUser(this.todoUser1);
                this.project2.setUser(this.todoUser2);

                this.todoUserRepository.saveAll(List.of(this.todoUser1, this.todoUser2));
                this.projectListRepository.saveAll(List.of(project1, project2));

        }

        @DisplayName("Should return all ProjectLists of the correct user id")
        @Test
        void Query_IfValidId_ReturnProjectLists() throws Exception {

                List<ProjectList> results = this.projectListRepository.findAllByUserId(this.todoUser1.getId());

                Assertions.assertThat(results)
                                .isNotNull()
                                .hasSize(1)
                                .containsExactlyInAnyOrder(this.project1);

        }

        @DisplayName("Should return empty List if can't find user id")
        @Test
        void Query_IfInvalid_ReturnEmptyList() throws Exception {

                List<ProjectList> results = this.projectListRepository.findAllByUserId(3L);

                Assertions.assertThat(results)
                                .isNotNull()
                                .hasSize(0);

        }

        @DisplayName("Should return single ProjectList of the correct user id")
        @Test
        void Query_IfValidData_ReturnSingleProjectList() throws Exception {

                Optional<ProjectList> results = this.projectListRepository.findByUserIdAndId(this.todoUser2.getId(),
                                this.project2.getId());

                Assertions.assertThat(results)
                                .isNotNull()
                                .isEqualTo(Optional.of(this.project2));

        }

        @DisplayName("Should return empty optional if ProjectList is not found")
        @Test
        void Query_IfInvalidData_ReturnEmptyOptional() throws Exception {

                Optional<ProjectList> results = this.projectListRepository.findByUserIdAndId(this.todoUser2.getId(),
                                3L);

                Assertions.assertThat(results)
                                .isNotNull()
                                .isEqualTo(Optional.empty());

        }

        @DisplayName("Should return true if ProjectList is found")
        @Test
        void Query_IfProjectListExists_ReturnTrue() throws Exception {

                boolean results = this.projectListRepository.existsByUserIdAndId(this.todoUser1.getId(),
                                this.project1.getId());

                Assertions.assertThat(results)
                                .isNotNull()
                                .isTrue();

        }

        @DisplayName("Should return false if ProjectList is not found")
        @Test
        void Query_IfProjectListDoesNotExist_ReturnFalse() throws Exception {

                boolean results = this.projectListRepository.existsByUserIdAndId(this.todoUser1.getId(),
                                3L);

                Assertions.assertThat(results)
                                .isNotNull()
                                .isFalse();

        }

}
