package io.github.jhairs2.todo_list.todo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.jhairs2.todo_list.todo.model.TodoItem;
import io.github.jhairs2.todo_list.todo.repository.TodoRepository;

@Configuration
public class TodoConfig {

    @Bean
    CommandLineRunner commandLineRunner(TodoRepository todoRepository) {
        return args -> {
            TodoItem item = new TodoItem("Hello!");

            todoRepository.save(item);
        };

    }

}
