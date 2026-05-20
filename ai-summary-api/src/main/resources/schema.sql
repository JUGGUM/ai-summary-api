CREATE TABLE IF NOT EXISTS summaries (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_text TEXT NOT NULL,
    summary    TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);
