package com.example.scholarpulse.infrastructure.persistence;

import com.example.scholarpulse.domain.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
