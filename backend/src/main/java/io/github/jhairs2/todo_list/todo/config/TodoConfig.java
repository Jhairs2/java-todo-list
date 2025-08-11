package io.github.jhairs2.todo_list.todo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.model.TodoItem;
import io.github.jhairs2.todo_list.todo.model.TodoUser;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;
import io.github.jhairs2.todo_list.todo.repository.TodoRepository;
import io.github.jhairs2.todo_list.todo.repository.TodoUserRepository;

@Configuration
public class TodoConfig {

    @Bean
    CommandLineRunner commandLineRunner(TodoRepository todoRepository, ProjectListRepository projectListRepository,
            TodoUserRepository todoUserRepository, PasswordEncoder encoder) {
        return args -> {
            TodoUser user = new TodoUser("Justin", "pass");
            TodoItem item = new TodoItem("Hello!");
            ProjectList list = new ProjectList("Todos");
            ProjectList list2 = new ProjectList("Todossss");

            item.setList(list);
            list.getTasks().add(item);
            user.getProjects().add(list);
            list.setUser(user);

            user.setPassword(encoder.encode(user.getPassword()));

            projectListRepository.save(list2);
            todoUserRepository.save(user);

        };

    }

}
