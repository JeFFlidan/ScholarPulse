package com.example.scholarpulse.infrastructure.persistence;

import com.example.scholarpulse.domain.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Article> findByTopicId(Long topicId, Pageable pageable);

    Optional<Article> findByExternalIdAndTopicId(String externalId, Long topicId);

    Optional<Article> findByDoiIgnoreCaseAndTopicId(String doi, Long topicId);
}
