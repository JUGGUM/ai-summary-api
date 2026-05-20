package com.example.ai_summary_api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("summary")
public class Summary {

    @Id
    private Long id;

    @Column("original_text")
    private String originalText;

    @Column("summary_text")
    private String summaryText;

    @Column("created_at")
    private LocalDateTime createdAt;

    protected Summary() {}

    public Summary(String originalText, String summaryText, LocalDateTime createdAt) {
        this.originalText = originalText;
        this.summaryText = summaryText;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getOriginalText() { return originalText; }
    public String getSummaryText() { return summaryText; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
