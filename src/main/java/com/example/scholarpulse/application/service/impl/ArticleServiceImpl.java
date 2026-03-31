package com.example.scholarpulse.application.service.impl;

import com.example.scholarpulse.application.dto.article.ArticleResponse;
import com.example.scholarpulse.application.dto.article.ArticleUpdateRequest;
import com.example.scholarpulse.application.mapper.ArticleMapper;
import com.example.scholarpulse.application.service.ArticleService;
import com.example.scholarpulse.domain.entity.Article;
import com.example.scholarpulse.exception.ResourceNotFoundException;
import com.example.scholarpulse.infrastructure.persistence.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleResponse> findAll(String keyword, Pageable pageable) {
        Page<Article> articles = (keyword == null || keyword.isBlank())
                ? articleRepository.findAll(pageable)
                : articleRepository.findByTitleContainingIgnoreCase(keyword.trim(), pageable);

        return articles.map(articleMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleResponse> findByTopic(Long topicId, Pageable pageable) {
        return articleRepository.findByTopicId(topicId, pageable)
                .map(articleMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleResponse findById(Long id) {
        return articleMapper.toResponse(getArticleOrThrow(id));
    }

    @Override
    public ArticleResponse update(Long id, ArticleUpdateRequest request) {
        Article article = getArticleOrThrow(id);

        if (request.title() != null && !request.title().isBlank()) {
            article.setTitle(request.title().trim());
        }
        if (request.authors() != null) {
            article.setAuthors(request.authors());
        }
        if (request.abstractText() != null) {
            article.setAbstractText(request.abstractText());
        }
        if (request.publicationYear() != null) {
            article.setPublicationYear(request.publicationYear());
        }
        if (request.doi() != null) {
            article.setDoi(request.doi());
        }
        if (request.url() != null) {
            article.setUrl(request.url());
        }
        if (request.sourceName() != null) {
            article.setSourceName(request.sourceName());
        }
        if (request.citationCount() != null) {
            article.setCitationCount(request.citationCount());
        }

        return articleMapper.toResponse(article);
    }

    @Override
    public void delete(Long id) {
        Article article = getArticleOrThrow(id);
        articleRepository.delete(article);
    }

    private Article getArticleOrThrow(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article with id=%d was not found".formatted(id)));
    }
}
