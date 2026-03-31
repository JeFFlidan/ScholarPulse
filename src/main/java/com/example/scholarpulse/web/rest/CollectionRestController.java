package com.example.scholarpulse.web.rest;

import com.example.scholarpulse.application.dto.collection.CollectionCreateRequest;
import com.example.scholarpulse.application.dto.collection.CollectionResponse;
import com.example.scholarpulse.application.dto.collection.CollectionUpdateRequest;
import com.example.scholarpulse.application.service.CollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionRestController {

    private final CollectionService collectionService;

    @PostMapping
    public CollectionResponse create(@Valid @RequestBody CollectionCreateRequest request) {
        return collectionService.create(request);
    }

    @GetMapping
    public List<CollectionResponse> findAll() {
        return collectionService.findAll();
    }

    @GetMapping("/{id}")
    public CollectionResponse findById(@PathVariable Long id) {
        return collectionService.findById(id);
    }

    @PutMapping("/{id}")
    public CollectionResponse update(@PathVariable Long id, @Valid @RequestBody CollectionUpdateRequest request) {
        return collectionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        collectionService.delete(id);
    }

    @PostMapping("/{collectionId}/articles/{articleId}")
    public CollectionResponse addArticle(@PathVariable Long collectionId, @PathVariable Long articleId) {
        return collectionService.addArticle(collectionId, articleId);
    }

    @DeleteMapping("/{collectionId}/articles/{articleId}")
    public CollectionResponse removeArticle(@PathVariable Long collectionId, @PathVariable Long articleId) {
        return collectionService.removeArticle(collectionId, articleId);
    }
}
