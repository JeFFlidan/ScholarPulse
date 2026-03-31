package com.example.scholarpulse.application.dto.article;

import jakarta.validation.constraints.Size;

public record ArticleUpdateRequest(
        @Size(min = 1, max = 500, message = "Title must be between 1 and 500 characters")
        String title,
        String authors,
        String abstractText,
        Integer publicationYear,
        String doi,
        String url,
        String sourceName,
        Integer citationCount
) {
}
