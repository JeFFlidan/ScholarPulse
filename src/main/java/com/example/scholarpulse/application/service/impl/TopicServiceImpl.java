package com.example.scholarpulse.application.service.impl;

import com.example.scholarpulse.application.dto.topic.TopicCreateRequest;
import com.example.scholarpulse.application.dto.topic.TopicResponse;
import com.example.scholarpulse.application.dto.topic.TopicUpdateRequest;
import com.example.scholarpulse.application.mapper.TopicMapper;
import com.example.scholarpulse.application.service.TopicService;
import com.example.scholarpulse.domain.entity.Topic;
import com.example.scholarpulse.exception.ResourceNotFoundException;
import com.example.scholarpulse.infrastructure.persistence.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Override
    public TopicResponse create(TopicCreateRequest request) {
        Topic topic = new Topic(
                request.name().trim(),
                request.description(),
                request.searchQuery().trim()
        );

        return topicMapper.toResponse(topicRepository.save(topic));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicResponse> findAll() {
        return topicRepository.findAll()
                .stream()
                .map(topicMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TopicResponse findById(Long id) {
        return topicMapper.toResponse(getTopicOrThrow(id));
    }

    @Override
    public TopicResponse update(Long id, TopicUpdateRequest request) {
        Topic topic = getTopicOrThrow(id);
        topic.setName(request.name().trim());
        topic.setDescription(request.description());
        topic.setSearchQuery(request.searchQuery().trim());

        return topicMapper.toResponse(topic);
    }

    @Override
    public void delete(Long id) {
        Topic topic = getTopicOrThrow(id);
        topicRepository.delete(topic);
    }

    private Topic getTopicOrThrow(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic with id=%d was not found".formatted(id)));
    }
}
