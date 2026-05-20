package com.example.ai_summary_api.service;

import com.example.ai_summary_api.dto.SummaryRequest;
import com.example.ai_summary_api.dto.SummaryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SummaryService {

    Mono<SummaryResponse> summarize(SummaryRequest request);

    Flux<String> summarizeStream(SummaryRequest request);

    Mono<SummaryResponse> findById(Long id);

    Flux<SummaryResponse> findAll(int page, int size);
}
