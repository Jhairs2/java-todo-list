package io.github.jhairs2.todo_list.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.github.jhairs2.todo_list.todo.model.TodoUser;
import io.github.jhairs2.todo_list.todo.repository.TodoUserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TodoUserRepositoryTests {

    @Autowired
    TodoUserRepository todoUserRepository;

    TodoUser user;

    @BeforeEach
    void setUp() {
        this.user = new TodoUser("Test", "Pass12345678");
        this.todoUserRepository.save(this.user);
    }

    @DisplayName("Should return user with correct username")
    @Test
    void Query_IfValidData_ReturnUsername() throws Exception {

        Optional<TodoUser> results = this.todoUserRepository.findByUsername(this.user.getUsername());

        Assertions.assertThat(results)
                .isNotEmpty()
                .isEqualTo(Optional.of(this.user));
    }

    @DisplayName("Should return empty optional when user can't be found")
    @Test
    void Query_IfInvalidData_ReturnEmptyOptional() throws Exception {

        Optional<TodoUser> results = this.todoUserRepository.findByUsername("USERDOESNTEXIST");

        Assertions.assertThat(results)
                .isEmpty();
    }

    @DisplayName("Should return true if user with correct username exists")
    @Test
    void Query_IfUserExists_ReturnTrue() throws Exception {

        boolean results = this.todoUserRepository.existsByUsername(this.user.getUsername());

        Assertions.assertThat(results)
                .isTrue();
    }

    @DisplayName("Should return false when user can't be found")
    @Test
    void Query_IfUserDoesNotExist_ReturnFalse() throws Exception {

        boolean results = this.todoUserRepository.existsByUsername("USERDOESNTEXIST");

        Assertions.assertThat(results)
                .isFalse();
    }

}
