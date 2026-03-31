package com.example.scholarpulse.application.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TopicCreateRequest(
        @NotBlank(message = "Topic name is required")
        @Size(max = 150, message = "Topic name must be at most 150 characters")
        String name,

        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description,

        @NotBlank(message = "Search query is required")
        @Size(max = 255, message = "Search query must be at most 255 characters")
        String searchQuery
) {
}
