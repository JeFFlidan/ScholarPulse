package com.example.scholarpulse.application.service.impl;

import com.example.scholarpulse.application.dto.article.ArticleResponse;
import com.example.scholarpulse.application.dto.importing.ImportResponse;
import com.example.scholarpulse.application.mapper.ArticleMapper;
import com.example.scholarpulse.application.port.out.ArticleImportClient;
import com.example.scholarpulse.application.service.ImportService;
import com.example.scholarpulse.domain.entity.Article;
import com.example.scholarpulse.domain.entity.Topic;
import com.example.scholarpulse.exception.ResourceNotFoundException;
import com.example.scholarpulse.infrastructure.persistence.ArticleRepository;
import com.example.scholarpulse.infrastructure.persistence.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImportServiceImpl implements ImportService {

    private final TopicRepository topicRepository;
    private final ArticleRepository articleRepository;
    private final ArticleImportClient articleImportClient;
    private final ArticleMapper articleMapper;

    @Override
    public ImportResponse importArticlesForTopic(Long topicId, int limit) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic with id=%d was not found".formatted(topicId)));
        System.out.println("Start importing!");
        List<ArticleImportClient.ImportedArticle> imported = articleImportClient.searchWorks(topic.getSearchQuery(), limit);
        System.out.println("After imported");
        List<ArticleResponse> savedArticles = new ArrayList<>();

        for (ArticleImportClient.ImportedArticle importedArticle : imported) {
            Article article = resolveArticleForTopic(topicId, importedArticle);

            article.setTitle(importedArticle.title());
            article.setAuthors(importedArticle.authors());
            article.setAbstractText(importedArticle.abstractText());
            article.setPublicationYear(importedArticle.publicationYear());
            article.setDoi(importedArticle.doi());
            article.setUrl(importedArticle.url());
            article.setSourceName(importedArticle.sourceName());
            article.setCitationCount(importedArticle.citationCount());
            article.setExternalId(importedArticle.externalId());
            article.setTopic(topic);

            Article saved = articleRepository.save(article);
            savedArticles.add(articleMapper.toResponse(saved));
        }
        System.out.println("AFTER FOR!");
        return new ImportResponse(savedArticles.size(), savedArticles);
    }

    private Article resolveArticleForTopic(Long topicId, ArticleImportClient.ImportedArticle importedArticle) {
        if (importedArticle.externalId() != null && !importedArticle.externalId().isBlank()) {
            return articleRepository.findByExternalIdAndTopicId(importedArticle.externalId(), topicId)
                    .orElseGet(() -> newArticle(importedArticle));
        }

        if (importedArticle.doi() != null && !importedArticle.doi().isBlank()) {
            return articleRepository.findByDoiIgnoreCaseAndTopicId(importedArticle.doi(), topicId)
                    .orElseGet(() -> newArticle(importedArticle));
        }

        return newArticle(importedArticle);
    }

    private Article newArticle(ArticleImportClient.ImportedArticle importedArticle) {
        return new Article(
                importedArticle.externalId(),
                importedArticle.title(),
                importedArticle.authors(),
                importedArticle.abstractText(),
                importedArticle.publicationYear(),
                importedArticle.doi(),
                importedArticle.url(),
                importedArticle.sourceName(),
                importedArticle.citationCount()
        );
    }
}
