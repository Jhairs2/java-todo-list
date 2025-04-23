package io.github.jhairs2.todo_list.todo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Tasks")
public class TodoItem {
    @Id
    @SequenceGenerator(name = "task_sequence", sequenceName = "task_sequence", allocationSize = 1)

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_sequence")

    private Long id;
    private String task;
    private boolean completed;

    public TodoItem() {
    }

    public TodoItem(Long id, String task) {
        this.id = id;
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void toggleCompleted() {
        if (this.completed) {
            this.completed = false;

        } else {
            this.completed = true;
        }
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
