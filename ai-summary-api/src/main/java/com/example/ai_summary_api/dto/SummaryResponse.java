package com.example.ai_summary_api.dto;

import com.example.ai_summary_api.domain.Summary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SummaryResponse {

    private Long id;
    private String originalText;
    private String summary;
    private LocalDateTime createdAt;

    public static SummaryResponse from(Summary entity) {
        return SummaryResponse.builder()
                .id(entity.getId())
                .originalText(entity.getOriginalText())
                .summary(entity.getSummary())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
