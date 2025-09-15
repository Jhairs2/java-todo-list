package io.github.jhairs2.todo_list.todo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.jhairs2.todo_list.todo.model.CustomUserDetails;
import io.github.jhairs2.todo_list.todo.model.TodoUser;
import io.github.jhairs2.todo_list.todo.repository.TodoUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final TodoUserRepository todoUserRepository;

    public CustomUserDetailsService(TodoUserRepository todoUserRepository) {
        this.todoUserRepository = todoUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TodoUser user = this.todoUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User could not be found in database"));
        return new CustomUserDetails(user);
    }

}
