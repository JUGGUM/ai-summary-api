package com.example.ai_summary_api.service;

import com.example.ai_summary_api.domain.Summary;
import com.example.ai_summary_api.dto.SummaryRequest;
import com.example.ai_summary_api.repository.SummaryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
public class SummaryService {

    private final ChatClient chatClient;
    private final SummaryRepository summaryRepository;

    public SummaryService(ChatClient.Builder builder, SummaryRepository summaryRepository) {
        this.chatClient = builder.build();
        this.summaryRepository = summaryRepository;
    }

    public Mono<Summary> summarize(SummaryRequest request) {
        String prompt = buildPrompt(request.text());
        return Mono.fromCallable(() ->
                        chatClient.prompt()
                                .user(prompt)
                                .call()
                                .content()
                )
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(summaryText -> {
                    Summary summary = new Summary(request.text(), summaryText, LocalDateTime.now());
                    return summaryRepository.save(summary);
                });
    }

    public Flux<String> summarizeStream(SummaryRequest request) {
        String prompt = buildPrompt(request.text());
        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content();
    }

    public Mono<Summary> findById(Long id) {
        return summaryRepository.findById(id);
    }

    public Flux<Summary> findAll() {
        return summaryRepository.findAll();
    }

    private String buildPrompt(String text) {
        return String.format("""
                다음 텍스트를 핵심 내용 위주로 간결하게 요약해주세요.
                요약은 3~5문장 이내로 작성하고, 한국어로 답변해주세요.

                [텍스트]
                %s
                """, text);
    }
}
