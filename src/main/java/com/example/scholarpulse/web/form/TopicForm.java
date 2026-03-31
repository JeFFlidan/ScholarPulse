package com.example.scholarpulse.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TopicForm {

    @NotBlank(message = "Topic name is required")
    @Size(max = 150, message = "Topic name must be at most 150 characters")
    private String name;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotBlank(message = "Search query is required")
    @Size(max = 255, message = "Search query must be at most 255 characters")
    private String searchQuery;

    public TopicForm() {
    }

    public TopicForm(String name, String description, String searchQuery) {
        this.name = name;
        this.description = description;
        this.searchQuery = searchQuery;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
