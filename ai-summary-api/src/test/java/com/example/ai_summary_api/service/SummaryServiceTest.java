package com.example.ai_summary_api.service;

import com.example.ai_summary_api.domain.Summary;
import com.example.ai_summary_api.dto.SummaryRequest;
import com.example.ai_summary_api.dto.SummaryResponse;
import com.example.ai_summary_api.repository.SummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SummaryServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    @Mock
    private SummaryRepository repository;

    private SummaryService summaryService;

    @BeforeEach
    void setUp() {
        summaryService = new SummaryServiceImpl(chatClient, repository);
    }

    @Test
    @DisplayName("텍스트 요약 요청 시 결과를 저장하고 반환한다")
    void summarize_savesAndReturnsSummary() {
        String inputText = "스프링 부트는 자바 기반의 프레임워크입니다.";
        String expectedSummary = "스프링 부트 요약입니다.";

        given(chatClient.prompt().user(anyString()).call().content())
                .willReturn(expectedSummary);

        Summary saved = Summary.builder()
                .id(1L)
                .originalText(inputText)
                .summary(expectedSummary)
                .createdAt(LocalDateTime.now())
                .build();
        given(repository.save(any(Summary.class))).willReturn(Mono.just(saved));

        StepVerifier.create(summaryService.summarize(new SummaryRequest(inputText)))
                .assertNext(response -> {
                    assertThat(response.getId()).isEqualTo(1L);
                    assertThat(response.getSummary()).isEqualTo(expectedSummary);
                    assertThat(response.getOriginalText()).isEqualTo(inputText);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("ID로 요약 이력을 조회한다")
    void findById_returnsMatchingSummary() {
        Summary entity = Summary.builder()
                .id(1L)
                .originalText("원본 텍스트")
                .summary("요약된 텍스트")
                .createdAt(LocalDateTime.now())
                .build();

        given(repository.findById(1L)).willReturn(Mono.just(entity));

        StepVerifier.create(summaryService.findById(1L))
                .assertNext(response -> {
                    assertThat(response.getId()).isEqualTo(1L);
                    assertThat(response.getOriginalText()).isEqualTo("원본 텍스트");
                    assertThat(response.getSummary()).isEqualTo("요약된 텍스트");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll 호출 시 페이지 크기만큼 결과를 반환한다")
    void findAll_returnsPagedResults() {
        Summary s1 = Summary.builder().id(1L).originalText("t1").summary("s1").createdAt(LocalDateTime.now()).build();
        Summary s2 = Summary.builder().id(2L).originalText("t2").summary("s2").createdAt(LocalDateTime.now()).build();
        Summary s3 = Summary.builder().id(3L).originalText("t3").summary("s3").createdAt(LocalDateTime.now()).build();

        given(repository.findAll()).willReturn(Flux.just(s1, s2, s3));

        StepVerifier.create(summaryService.findAll(0, 2))
                .assertNext(r -> assertThat(r.getId()).isEqualTo(1L))
                .assertNext(r -> assertThat(r.getId()).isEqualTo(2L))
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll 두 번째 페이지 조회 시 올바른 항목을 반환한다")
    void findAll_secondPage_returnsCorrectItem() {
        Summary s1 = Summary.builder().id(1L).originalText("t1").summary("s1").createdAt(LocalDateTime.now()).build();
        Summary s2 = Summary.builder().id(2L).originalText("t2").summary("s2").createdAt(LocalDateTime.now()).build();
        Summary s3 = Summary.builder().id(3L).originalText("t3").summary("s3").createdAt(LocalDateTime.now()).build();

        given(repository.findAll()).willReturn(Flux.just(s1, s2, s3));

        StepVerifier.create(summaryService.findAll(1, 2))
                .assertNext(r -> assertThat(r.getId()).isEqualTo(3L))
                .verifyComplete();
    }
}
