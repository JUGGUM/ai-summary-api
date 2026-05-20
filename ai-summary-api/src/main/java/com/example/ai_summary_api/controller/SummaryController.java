package com.example.ai_summary_api.controller;

import com.example.ai_summary_api.dto.SummaryRequest;
import com.example.ai_summary_api.dto.SummaryResponse;
import com.example.ai_summary_api.service.SummaryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @PostMapping
    public Mono<SummaryResponse> summarize(@RequestBody SummaryRequest request) {
        return summaryService.summarize(request)
                .map(SummaryResponse::from);
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> summarizeStream(@RequestBody SummaryRequest request) {
        return summaryService.summarizeStream(request);
    }

    @GetMapping("/{id}")
    public Mono<SummaryResponse> findById(@PathVariable Long id) {
        return summaryService.findById(id)
                .map(SummaryResponse::from);
    }

    @GetMapping
    public Flux<SummaryResponse> findAll() {
        return summaryService.findAll()
                .map(SummaryResponse::from);
    }
}
