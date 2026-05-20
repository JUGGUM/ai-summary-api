package com.example.ai_summary_api.controller;

import com.example.ai_summary_api.dto.SummaryRequest;
import com.example.ai_summary_api.dto.SummaryResponse;
import com.example.ai_summary_api.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping
    public Mono<SummaryResponse> summarize(@RequestBody SummaryRequest request) {
        return summaryService.summarize(request);
    }

    @PostMapping(value = "/stream",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> summarizeStream(@RequestBody SummaryRequest request) {
        return summaryService.summarizeStream(request);
    }

    @GetMapping("/{id}")
    public Mono<SummaryResponse> findById(@PathVariable Long id) {
        return summaryService.findById(id);
    }

    @GetMapping
    public Flux<SummaryResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return summaryService.findAll(page, size);
    }
}
