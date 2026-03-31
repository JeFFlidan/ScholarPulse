package com.example.scholarpulse.application.port.out;

import java.util.List;

public interface ArticleImportClient {

    List<ImportedArticle> searchWorks(String query, int limit);

    record ImportedArticle(
            String externalId,
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
}
