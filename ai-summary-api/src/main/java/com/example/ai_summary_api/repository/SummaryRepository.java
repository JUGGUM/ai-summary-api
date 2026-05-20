package com.example.ai_summary_api.repository;

import com.example.ai_summary_api.domain.Summary;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SummaryRepository extends ReactiveCrudRepository<Summary, Long> {
}
