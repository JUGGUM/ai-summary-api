# ai-summary-api

![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.4-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring WebFlux](https://img.shields.io/badge/Spring_WebFlux-Reactive-6DB33F?style=flat-square&logo=spring&logoColor=white)
![Spring AI](https://img.shields.io/badge/Spring_AI-2.0.0--M3-6DB33F?style=flat-square&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-Kotlin_DSL-02303A?style=flat-square&logo=gradle&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)

> **학습용 실습 프로젝트** — Spring WebFlux + TDD + AI Native 개발 패턴을 익히기 위한 프로젝트입니다.

---

## 프로젝트 소개

`ai-summary-api`는 사용자가 텍스트를 입력하면 **Anthropic Claude API**를 통해 AI가 내용을 요약하여 반환하는 REST API 서비스입니다.

### 학습 목적

- **Spring WebFlux**: 비동기 논블로킹(Reactive) 프로그래밍 모델 이해
- **TDD (Test-Driven Development)**: 테스트를 먼저 작성하고 구현하는 개발 방법론 실습
- **AI Native**: Spring AI를 활용하여 LLM을 애플리케이션에 자연스럽게 통합하는 방법 학습

---

## 기술 스택

| 분류 | 기술 | 버전 |
|------|------|------|
| Language | Java | 21 |
| Framework | Spring Boot | 4.0.4 |
| Reactive Web | Spring WebFlux | - |
| AI Integration | Spring AI (Anthropic) | 2.0.0-M3 |
| Reactive DB | Spring Data R2DBC | - |
| Database | H2 (In-Memory) | - |
| R2DBC Driver | r2dbc-h2 | - |
| Utility | Lombok | - |
| Build Tool | Gradle (Kotlin DSL) | - |
| Test | JUnit 5, Reactor Test (StepVerifier) | - |

---

## 프로젝트 구조

```
ai-summary-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/ai_summary_api/
│   │   │   ├── AiSummaryApiApplication.java     # 애플리케이션 진입점
│   │   │   ├── controller/                      # REST 컨트롤러 (예정)
│   │   │   ├── service/                         # 비즈니스 로직, AI 연동 (예정)
│   │   │   ├── domain/                          # 도메인 모델, 엔티티 (예정)
│   │   │   └── repository/                      # R2DBC 리포지토리 (예정)
│   │   └── resources/
│   │       ├── application.yaml                 # 애플리케이션 설정
│   │       └── schema.sql                       # DB 스키마 (예정)
│   └── test/
│       └── java/com/example/ai_summary_api/
│           ├── AiSummaryApiApplicationTests.java
│           ├── controller/                      # 컨트롤러 테스트 (예정)
│           └── service/                         # 서비스 단위 테스트 (예정)
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 실행 방법

### 사전 요구사항

- Java 21 이상
- Anthropic API 키 ([Anthropic Console](https://console.anthropic.com)에서 발급)

### 환경변수 설정

Anthropic Claude API 키를 환경변수로 설정합니다.

**macOS / Linux**
```bash
export SPRING_AI_ANTHROPIC_API_KEY=your-api-key-here
```

**Windows (PowerShell)**
```powershell
$env:SPRING_AI_ANTHROPIC_API_KEY="your-api-key-here"
```

또는 `application.yaml`에 직접 설정할 수 있습니다. (API 키를 직접 코드에 포함하지 마세요)

```yaml
spring:
  ai:
    anthropic:
      api-key: ${SPRING_AI_ANTHROPIC_API_KEY}
```

### 애플리케이션 실행

```bash
# 프로젝트 루트에서 실행
./gradlew bootRun
```

기본 포트: `http://localhost:8080`

---

## API 명세

> 현재 구현 진행 중인 엔드포인트입니다.

### 텍스트 요약 (동기 응답)

```
POST /api/summary
Content-Type: application/json

{
  "text": "요약할 텍스트 내용을 입력하세요..."
}
```

**응답 예시**
```json
{
  "id": "1",
  "originalText": "요약할 텍스트 내용을 입력하세요...",
  "summary": "AI가 생성한 요약 내용",
  "createdAt": "2026-03-26T10:00:00"
}
```

---

### 텍스트 요약 (스트리밍 응답 - SSE) *(예정)*

```
POST /api/summary/stream
Content-Type: application/json
Accept: text/event-stream

{
  "text": "요약할 텍스트 내용을 입력하세요..."
}
```

**응답 형식** — `text/event-stream`
```
data: AI가 생성한

data:  요약 내용이

data:  스트리밍으로 전달됩니다.
```

---

### 요약 이력 조회 *(예정)*

```
GET /api/summary/{id}
```

```
GET /api/summary?page=0&size=10
```

---

## 테스트 실행 방법

```bash
# 전체 테스트 실행
./gradlew test

# 테스트 결과 리포트 확인
open build/reports/tests/test/index.html
```

### 테스트 전략

```
단위 테스트 (Unit Test)
├── Service 레이어: Mockito로 AI 클라이언트 모킹
└── Repository 레이어: R2DBC 리포지토리 테스트

통합 테스트 (Integration Test)
└── @SpringBootTest + WebTestClient로 엔드포인트 검증

리액티브 테스트
└── reactor-test의 StepVerifier로 Mono/Flux 검증
```

---

## 학습 포인트

### Spring WebFlux

- `Mono<T>` / `Flux<T>` 리액티브 타입 이해 및 활용
- 논블로킹 I/O와 이벤트 루프 기반 처리 모델
- R2DBC를 통한 리액티브 데이터베이스 접근
- Server-Sent Events (SSE)를 활용한 스트리밍 응답 구현

### TDD (Test-Driven Development)

- Red-Green-Refactor 사이클 실습
- `StepVerifier`를 활용한 리액티브 스트림 테스트
- `WebTestClient`를 활용한 HTTP 레이어 테스트
- Mockito로 외부 의존성(AI API) 격리 후 테스트

### AI Native

- Spring AI를 통한 Anthropic Claude API 연동
- `ChatClient` / `StreamingChatClient` 활용 방법
- 프롬프트 템플릿 설계 및 관리
- 스트리밍 응답(SSE)과 리액티브 파이프라인 연결

---

## 참고 자료

- [Spring WebFlux 공식 문서](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Spring AI 공식 문서](https://docs.spring.io/spring-ai/reference/)
- [Anthropic Claude API 문서](https://docs.anthropic.com)
- [Project Reactor 공식 문서](https://projectreactor.io/docs)
- [R2DBC 공식 문서](https://r2dbc.io)
