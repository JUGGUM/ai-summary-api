package com.example.ai_summary_api.controller;

import com.example.ai_summary_api.dto.SummaryResponse;
import com.example.ai_summary_api.service.SummaryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@WebFluxTest(SummaryController.class)
class SummaryControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private SummaryService summaryService;

    @Test
    @DisplayName("POST /api/summary - 요약 요청을 처리하고 저장된 결과를 반환한다")
    void summarize_returns200WithSummaryResponse() {
        SummaryResponse response = SummaryResponse.builder()
                .id(1L)
                .originalText("원본 텍스트입니다.")
                .summary("요약된 내용입니다.")
                .createdAt(LocalDateTime.now())
                .build();

        given(summaryService.summarize(any())).willReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/summary")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"text\": \"원본 텍스트입니다.\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.summary").isEqualTo("요약된 내용입니다.");
    }

    @Test
    @DisplayName("POST /api/summary/stream - SSE 스트리밍 응답을 반환한다")
    void summarizeStream_returnsEventStream() {
        given(summaryService.summarizeStream(any()))
                .willReturn(Flux.just("요약", " 내용", "입니다."));

        webTestClient.post()
                .uri("/api/summary/stream")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .bodyValue("{\"text\": \"스트리밍 테스트\"}")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM);
    }

    @Test
    @DisplayName("GET /api/summary/{id} - ID로 요약 이력을 조회한다")
    void findById_returns200WithResponse() {
        SummaryResponse response = SummaryResponse.builder()
                .id(1L)
                .originalText("원본")
                .summary("요약")
                .createdAt(LocalDateTime.now())
                .build();

        given(summaryService.findById(1L)).willReturn(Mono.just(response));

        webTestClient.get()
                .uri("/api/summary/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.summary").isEqualTo("요약");
    }

    @Test
    @DisplayName("GET /api/summary - 페이지 단위로 요약 이력 목록을 반환한다")
    void findAll_returnsListOfSummaries() {
        SummaryResponse r1 = SummaryResponse.builder().id(1L).originalText("t1").summary("s1").createdAt(LocalDateTime.now()).build();
        SummaryResponse r2 = SummaryResponse.builder().id(2L).originalText("t2").summary("s2").createdAt(LocalDateTime.now()).build();

        given(summaryService.findAll(anyInt(), anyInt())).willReturn(Flux.just(r1, r2));

        webTestClient.get()
                .uri("/api/summary?page=0&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SummaryResponse.class)
                .hasSize(2);
    }
}
