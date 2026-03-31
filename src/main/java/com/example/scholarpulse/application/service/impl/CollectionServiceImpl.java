package com.example.scholarpulse.application.service.impl;

import com.example.scholarpulse.application.dto.collection.CollectionCreateRequest;
import com.example.scholarpulse.application.dto.collection.CollectionResponse;
import com.example.scholarpulse.application.dto.collection.CollectionUpdateRequest;
import com.example.scholarpulse.application.mapper.CollectionMapper;
import com.example.scholarpulse.application.service.CollectionService;
import com.example.scholarpulse.domain.entity.Article;
import com.example.scholarpulse.domain.entity.ResearchCollection;
import com.example.scholarpulse.exception.ResourceNotFoundException;
import com.example.scholarpulse.infrastructure.persistence.ArticleRepository;
import com.example.scholarpulse.infrastructure.persistence.ResearchCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionServiceImpl implements CollectionService {

    private final ResearchCollectionRepository collectionRepository;
    private final ArticleRepository articleRepository;
    private final CollectionMapper collectionMapper;

    @Override
    public CollectionResponse create(CollectionCreateRequest request) {
        ResearchCollection collection = new ResearchCollection(
                request.name().trim(),
                request.description()
        );

        return collectionMapper.toResponse(collectionRepository.save(collection));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollectionResponse> findAll() {
        return collectionRepository.findAll()
                .stream()
                .map(collectionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionResponse findById(Long id) {
        return collectionMapper.toResponse(getCollectionOrThrow(id));
    }

    @Override
    public CollectionResponse update(Long id, CollectionUpdateRequest request) {
        ResearchCollection collection = getCollectionOrThrow(id);
        collection.setName(request.name().trim());
        collection.setDescription(request.description());

        return collectionMapper.toResponse(collection);
    }

    @Override
    public void delete(Long id) {
        ResearchCollection collection = getCollectionOrThrow(id);
        collectionRepository.delete(collection);
    }

    @Override
    public CollectionResponse addArticle(Long collectionId, Long articleId) {
        ResearchCollection collection = getCollectionOrThrow(collectionId);
        Article article = getArticleOrThrow(articleId);

        collection.addArticle(article);
        return collectionMapper.toResponse(collection);
    }

    @Override
    public CollectionResponse removeArticle(Long collectionId, Long articleId) {
        ResearchCollection collection = getCollectionOrThrow(collectionId);
        Article article = getArticleOrThrow(articleId);

        collection.removeArticle(article);
        return collectionMapper.toResponse(collection);
    }

    private ResearchCollection getCollectionOrThrow(Long id) {
        return collectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection with id=%d was not found".formatted(id)));
    }

    private Article getArticleOrThrow(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article with id=%d was not found".formatted(id)));
    }
}
