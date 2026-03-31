package com.example.scholarpulse.application.mapper;

import com.example.scholarpulse.application.dto.article.ArticleResponse;
import com.example.scholarpulse.domain.entity.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public ArticleResponse toResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getExternalId(),
                article.getTitle(),
                article.getAuthors(),
                article.getAbstractText(),
                article.getPublicationYear(),
                article.getDoi(),
                article.getUrl(),
                article.getSourceName(),
                article.getCitationCount(),
                article.getTopic().getId(),
                article.getTopic().getName(),
                article.getCreatedAt()
        );
    }
}
