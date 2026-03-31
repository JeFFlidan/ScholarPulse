package com.example.scholarpulse.application.service;

import com.example.scholarpulse.application.dto.collection.CollectionCreateRequest;
import com.example.scholarpulse.application.dto.collection.CollectionResponse;
import com.example.scholarpulse.application.dto.collection.CollectionUpdateRequest;

import java.util.List;

public interface CollectionService {

    CollectionResponse create(CollectionCreateRequest request);

    List<CollectionResponse> findAll();

    CollectionResponse findById(Long id);

    CollectionResponse update(Long id, CollectionUpdateRequest request);

    void delete(Long id);

    CollectionResponse addArticle(Long collectionId, Long articleId);

    CollectionResponse removeArticle(Long collectionId, Long articleId);
}
