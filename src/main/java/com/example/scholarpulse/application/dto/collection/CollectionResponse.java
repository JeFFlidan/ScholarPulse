package com.example.scholarpulse.application.dto.collection;

import java.time.LocalDateTime;
import java.util.List;

public record CollectionResponse(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        int articleCount,
        List<Long> articleIds
) {
}
