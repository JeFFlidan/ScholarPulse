package com.example.scholarpulse.application.dto.importing;

import com.example.scholarpulse.application.dto.article.ArticleResponse;

import java.util.List;

public record ImportResponse(
        int importedCount,
        List<ArticleResponse> importedArticles
) {
}
