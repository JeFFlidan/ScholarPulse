package com.example.scholarpulse.application.mapper;

import com.example.scholarpulse.application.dto.collection.CollectionResponse;
import com.example.scholarpulse.domain.entity.ResearchCollection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CollectionMapper {

    public CollectionResponse toResponse(ResearchCollection collection) {
        List<Long> articleIds = collection.getArticles()
                .stream()
                .map(article -> article.getId())
                .sorted()
                .toList();

        return new CollectionResponse(
                collection.getId(),
                collection.getName(),
                collection.getDescription(),
                collection.getCreatedAt(),
                collection.getArticles().size(),
                articleIds
        );
    }
}
