package com.example.scholarpulse.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "topics")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "search_query", nullable = false, length = 255)
    private String searchQuery;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Article> articles = new LinkedHashSet<>();

    public Topic(String name, String description, String searchQuery) {
        this.name = name;
        this.description = description;
        this.searchQuery = searchQuery;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void addArticle(Article article) {
        articles.add(article);
        article.setTopic(this);
    }

    public void removeArticle(Article article) {
        articles.remove(article);
        article.setTopic(null);
    }
}
