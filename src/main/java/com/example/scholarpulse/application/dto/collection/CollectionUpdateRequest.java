package com.example.scholarpulse.application.dto.collection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CollectionUpdateRequest(
        @NotBlank(message = "Collection name is required")
        @Size(max = 150, message = "Collection name must be at most 150 characters")
        String name,

        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description
) {
}
