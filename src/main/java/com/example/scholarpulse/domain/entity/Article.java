package com.example.scholarpulse.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", length = 255)
    private String externalId;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String authors;

    @Column(name = "abstract_text", columnDefinition = "TEXT")
    private String abstractText;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(length = 255)
    private String doi;

    @Column(length = 700)
    private String url;

    @Column(name = "source_name", length = 255)
    private String sourceName;

    @Column(name = "citation_count")
    private Integer citationCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToMany(mappedBy = "articles")
    private Set<ResearchCollection> collections = new LinkedHashSet<>();

    public Article(
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
        this.externalId = externalId;
        this.title = title;
        this.authors = authors;
        this.abstractText = abstractText;
        this.publicationYear = publicationYear;
        this.doi = doi;
        this.url = url;
        this.sourceName = sourceName;
        this.citationCount = citationCount;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
