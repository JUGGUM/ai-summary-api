package com.example.ai_summary_api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("summaries")
public class Summary {

    @Id
    private Long id;
    private String originalText;
    private String summary;
    private LocalDateTime createdAt;
}
