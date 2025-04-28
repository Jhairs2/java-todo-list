package io.github.jhairs2.todo_list.todo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String listTitle;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    private List<TodoItem> tasks;

    public ProjectList() {
    }

    public ProjectList(String listTitle) {
        this.listTitle = listTitle;
    }

    public TodoItem addTaskToList(TodoItem todoItem) {
        this.tasks.add(todoItem);
        return todoItem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public List<TodoItem> getTasks() {
        return tasks;
    }

}
