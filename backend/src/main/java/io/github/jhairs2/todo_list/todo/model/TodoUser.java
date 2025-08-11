package io.github.jhairs2.todo_list.todo.model;

public class TodoUser {
    private Long id;
    private String username;
    private String password;
    private String authority;

    public TodoUser() {
    }

    public TodoUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.authority = "ROLE_USER";
    }

    public TodoUser(String username, String password, String authority) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    public TodoUser(Long id, String username, String password, String authority) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getAuthority() {
        return this.authority;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
