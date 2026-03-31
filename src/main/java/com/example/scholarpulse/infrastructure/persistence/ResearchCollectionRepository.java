package com.example.scholarpulse.infrastructure.persistence;

import com.example.scholarpulse.domain.entity.ResearchCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResearchCollectionRepository extends JpaRepository<ResearchCollection, Long> {
}
