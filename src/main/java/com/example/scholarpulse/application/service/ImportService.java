package com.example.scholarpulse.application.service;

import com.example.scholarpulse.application.dto.importing.ImportResponse;

public interface ImportService {

    ImportResponse importArticlesForTopic(Long topicId, int limit);
}
