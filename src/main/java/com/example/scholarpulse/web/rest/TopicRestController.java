package com.example.scholarpulse.web.rest;

import com.example.scholarpulse.application.dto.topic.TopicCreateRequest;
import com.example.scholarpulse.application.dto.topic.TopicResponse;
import com.example.scholarpulse.application.dto.topic.TopicUpdateRequest;
import com.example.scholarpulse.application.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicRestController {

    private final TopicService topicService;

    @PostMapping
    public TopicResponse create(@Valid @RequestBody TopicCreateRequest request) {
        return topicService.create(request);
    }

    @GetMapping
    public List<TopicResponse> findAll() {
        return topicService.findAll();
    }

    @GetMapping("/{id}")
    public TopicResponse findById(@PathVariable Long id) {
        return topicService.findById(id);
    }

    @PutMapping("/{id}")
    public TopicResponse update(@PathVariable Long id, @Valid @RequestBody TopicUpdateRequest request) {
        return topicService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        topicService.delete(id);
    }
}
