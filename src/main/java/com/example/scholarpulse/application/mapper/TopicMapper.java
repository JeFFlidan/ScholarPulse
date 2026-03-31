package com.example.scholarpulse.application.mapper;

import com.example.scholarpulse.application.dto.topic.TopicResponse;
import com.example.scholarpulse.domain.entity.Topic;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {

    public TopicResponse toResponse(Topic topic) {
        return new TopicResponse(
                topic.getId(),
                topic.getName(),
                topic.getDescription(),
                topic.getSearchQuery(),
                topic.getCreatedAt(),
                topic.getArticles().size()
        );
    }
}
