package io.github.jhairs2.todo_list.todo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.model.TodoItem;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;
import io.github.jhairs2.todo_list.todo.repository.TodoRepository;

@Configuration
public class TodoConfig {

    @Bean
    CommandLineRunner commandLineRunner(TodoRepository todoRepository, ProjectListRepository projectListRepository) {
        return args -> {
            TodoItem item = new TodoItem("Hello!");
            ProjectList list = new ProjectList("Todos");
            ProjectList list2 = new ProjectList("Todossss");
            item.setList(list);
            list.getTasks().add(item);

            projectListRepository.save(list);
            projectListRepository.save(list2);

        };

    }

}
