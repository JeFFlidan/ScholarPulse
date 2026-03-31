package com.example.scholarpulse.application.service;

import com.example.scholarpulse.application.dto.article.ArticleResponse;
import com.example.scholarpulse.application.dto.article.ArticleUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    Page<ArticleResponse> findAll(String keyword, Pageable pageable);

    Page<ArticleResponse> findByTopic(Long topicId, Pageable pageable);

    ArticleResponse findById(Long id);

    ArticleResponse update(Long id, ArticleUpdateRequest request);

    void delete(Long id);
}
