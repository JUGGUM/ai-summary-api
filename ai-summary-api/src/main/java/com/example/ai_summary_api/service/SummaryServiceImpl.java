package com.example.ai_summary_api.service;

import com.example.ai_summary_api.domain.Summary;
import com.example.ai_summary_api.dto.SummaryRequest;
import com.example.ai_summary_api.dto.SummaryResponse;
import com.example.ai_summary_api.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final ChatClient chatClient;
    private final SummaryRepository repository;

    private static final String PROMPT_TEMPLATE =
            "다음 텍스트를 핵심 내용 중심으로 3~5문장으로 간결하게 요약해주세요:\n\n%s";

    @Override
    public Mono<SummaryResponse> summarize(SummaryRequest request) {
        String prompt = PROMPT_TEMPLATE.formatted(request.getText());
        return Mono.fromCallable(() ->
                        chatClient.prompt()
                                .user(prompt)
                                .call()
                                .content())
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(summaryText -> {
                    Summary entity = Summary.builder()
                            .originalText(request.getText())
                            .summary(summaryText)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return repository.save(entity);
                })
                .map(SummaryResponse::from);
    }

    @Override
    public Flux<String> summarizeStream(SummaryRequest request) {
        String prompt = PROMPT_TEMPLATE.formatted(request.getText());
        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content();
    }

    @Override
    public Mono<SummaryResponse> findById(Long id) {
        return repository.findById(id)
                .map(SummaryResponse::from);
    }

    @Override
    public Flux<SummaryResponse> findAll(int page, int size) {
        return repository.findAll()
                .skip((long) page * size)
                .take(size)
                .map(SummaryResponse::from);
    }
}
