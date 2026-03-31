package com.example.scholarpulse.web.rest;

import com.example.scholarpulse.application.dto.article.ArticleResponse;
import com.example.scholarpulse.application.dto.article.ArticleUpdateRequest;
import com.example.scholarpulse.application.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleService articleService;

    @GetMapping
    public Page<ArticleResponse> findAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return articleService.findAll(keyword, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ArticleResponse findById(@PathVariable Long id) {
        return articleService.findById(id);
    }

    @GetMapping("/topic/{topicId}")
    public Page<ArticleResponse> findByTopic(
            @PathVariable Long topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return articleService.findByTopic(topicId, PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    public ArticleResponse update(@PathVariable Long id, @Valid @RequestBody ArticleUpdateRequest request) {
        return articleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        articleService.delete(id);
    }
}
