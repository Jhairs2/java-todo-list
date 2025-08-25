package io.github.jhairs2.todo_list.todo.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.jhairs2.todo_list.todo.model.TodoUser;
import io.github.jhairs2.todo_list.todo.repository.TodoUserRepository;

@Service
public class HelperService {
    private final TodoUserRepository todoUserRepository;

    public HelperService(TodoUserRepository todoUserRepository) {
        this.todoUserRepository = todoUserRepository;
    }

    public TodoUser getActiveUser() {

        return todoUserRepository.findById(getActiveUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User could not be found"));

    }

    public Long getActiveUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.valueOf(userId);
    }

    public String getActiveUsername() {
        return getActiveUser().getUsername();
    }

}