package io.github.jhairs2.todo_list.todo.dto;

public class ProjectListDTO {

    private Long id;
    private String projectTitle;

    public ProjectListDTO(Long id, String projectTitle) {
        this.id = id;
        this.projectTitle = projectTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }
}
