package com.example.scholarpulse.application.dto.topic;

import java.time.LocalDateTime;

public record TopicResponse(
        Long id,
        String name,
        String description,
        String searchQuery,
        LocalDateTime createdAt,
        int articleCount
) {
}
