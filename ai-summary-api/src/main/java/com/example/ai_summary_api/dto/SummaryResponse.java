package com.example.ai_summary_api.dto;

import com.example.ai_summary_api.domain.Summary;

import java.time.LocalDateTime;

public record SummaryResponse(
        Long id,
        String originalText,
        String summary,
        LocalDateTime createdAt
) {
    public static SummaryResponse from(Summary summary) {
        return new SummaryResponse(
                summary.getId(),
                summary.getOriginalText(),
                summary.getSummaryText(),
                summary.getCreatedAt()
        );
    }
}
