package com.example.task_api.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class TaskCreateRequest {
    @NotBlank(message = "Title is required")
    @Size(
            min = 3,
            max = 100,
            message = "Title must be between 3 and 100 characters"
    )
    private String title;
    @Size(
            max = 500,
            message = "Description cannot exceed 500 characters"
    )
    private String description;

    public TaskCreateRequest() {
    }

    public TaskCreateRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
