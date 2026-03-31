package com.example.scholarpulse.config;

import com.example.scholarpulse.application.dto.collection.CollectionCreateRequest;
import com.example.scholarpulse.application.dto.topic.TopicCreateRequest;
import com.example.scholarpulse.application.service.CollectionService;
import com.example.scholarpulse.application.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TopicService topicService;
    private final CollectionService collectionService;

    @Override
    public void run(String... args) {
        if (topicService.findAll().isEmpty()) {
            topicService.create(new TopicCreateRequest(
                    "Applied Mathematics",
                    "Articles related to numerical methods, PDEs, and mathematical modeling.",
                    "applied mathematics numerical methods"
            ));

            topicService.create(new TopicCreateRequest(
                    "Computer Vision",
                    "Papers about face tracking, landmark extraction, and 3D reconstruction.",
                    "computer vision 3d face reconstruction"
            ));
        }

        if (collectionService.findAll().isEmpty()) {
            collectionService.create(new CollectionCreateRequest(
                    "Initial Reading List",
                    "Starter collection for demonstrating many-to-many relations."
            ));
        }
    }
}
