package io.github.jhairs2.todo_list.todo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "lists")
public class ProjectList {
    @Id
    @SequenceGenerator(name = "list_sequence", sequenceName = "list_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "list_sequence")
    private Long id;

    @Column(nullable = false)
    private String listTitle;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoItem> tasks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "users_id")
    private TodoUser user;

    public ProjectList() {
    }

    public ProjectList(String listTitle) {
        this.listTitle = listTitle;

    }

    public Long getId() {
        return this.id;
    }

    public String getListTitle() {
        return this.listTitle;
    }

    public List<TodoItem> getTasks() {
        return this.tasks;
    }

    public TodoUser getUser() {
        return this.user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public void setUser(TodoUser user) {
        this.user = user;
    }

}
