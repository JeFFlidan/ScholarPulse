package com.example.scholarpulse.application.service;

import com.example.scholarpulse.application.dto.topic.TopicCreateRequest;
import com.example.scholarpulse.application.dto.topic.TopicResponse;
import com.example.scholarpulse.application.dto.topic.TopicUpdateRequest;

import java.util.List;

public interface TopicService {

    TopicResponse create(TopicCreateRequest request);

    List<TopicResponse> findAll();

    TopicResponse findById(Long id);

    TopicResponse update(Long id, TopicUpdateRequest request);

    void delete(Long id);
}
