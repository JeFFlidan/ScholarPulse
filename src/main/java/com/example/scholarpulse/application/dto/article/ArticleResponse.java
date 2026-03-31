package com.example.scholarpulse.application.dto.article;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        String externalId,
        String title,
        String authors,
        String abstractText,
        Integer publicationYear,
        String doi,
        String url,
        String sourceName,
        Integer citationCount,
        Long topicId,
        String topicName,
        LocalDateTime createdAt
) {
}
