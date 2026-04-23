# 🚀 Smart Quant Agent: AI-Driven Market Sentinel

Spring Boot와 Google Gemini를 결합하여 실시간 시장 데이터를 분석하고 전략적 투자 시그널을 제공하는 지능형 금융 에이전트입니다. 단순한 가격 알림을 넘어, MA200(200일 이동평균선) 돌파 및 거시 경제 상황을 고려한 정교한 리포트를 발송합니다.

---

## 🛠 Tech Stack

- **Framework:** Spring Boot 3.5 (Java 17)
- **AI Engine:** Google Gemini 2.5 Flash / Pro (Spring AI)
- **Database:** PostgreSQL (Primary), Redis (Caching)
- **External API:** AlphaVantage (Stock & Forex)
- **Communication:** Telegram Bot API
- **Infrastructure:** Docker, Docker Compose

## ✨ Key Features

- **Real-time Data Sync:** AlphaVantage API를 통해 미국 주요 주식 및 USD/KRW 환율 데이터를 실시간으로 동기화합니다.
- **AI-Based Technical Analysis:** Gemini 모델이 수집된 데이터를 바탕으로 200일 이동평균선(MA200) 돌파 여부와 기술적 지표를 분석합니다.
- **Strategic Insights:** 단순 가격 변동 외에도 현재 환율과 지정학적 리스크(전쟁 등)를 고려한 복합적인 투자 인사이트를 생성합니다.
- **Intelligent Notification:** 분석 결과를 텔레그램 메신저를 통해 가독성 높은 MarkdownV2 형식의 리포트로 자동 발송합니다.
- **Resilient Caching:** API 할당량 최적화 및 시스템 성능 향상을 위해 Redis 기반의 캐싱 레이어를 구축했습니다.

## 🏗 System Architecture

1. **Data Fetcher:** 외부 API로부터 실시간 주가 및 환율 지표를 수집합니다.
2. **Storage Layer:** PostgreSQL에 모든 시장 지표 이력을 저장하고, 최신 스냅샷은 Redis에 캐싱하여 AI 분석의 컨텍스트로 활용합니다.
3. **Analysis Engine (Gemini):** Spring AI를 통해 마켓 데이터를 모델에게 전달하고, 사전에 정의된 퀀트 전략 프롬프트를 바탕으로 분석 결과를 생성합니다.
4. **Notification Adapter:** 생성된 리포트를 Telegram Bot API를 통해 사용자에게 즉시 전달합니다.

## 🚀 Getting Started

### 1. Prerequisites
- Docker & Docker Compose
- Google AI Studio API Key (Gemini)
- AlphaVantage API Key
- Telegram Bot Token & Chat ID

### 2. Environment Variables
프로젝트 루트 폴더에 `.env` 파일을 생성하고 아래와 같이 환경 변수를 설정합니다.
```env
GEMINI_API_KEY=your_google_gemini_key
ALPHAVANTAGE_API_KEY=your_alphavantage_key
TELEGRAM_BOT_TOKEN=your_telegram_bot_token
TELEGRAM_CHAT_ID=your_chat_id
POSTGRES_DB=finance_db
POSTGRES_USER=myuser
POSTGRES_PASSWORD=mypassword

# Infrastructure (DB, Redis) 실행
docker-compose up -d

# Application 빌드 및 실행
./gradlew bootRun
