# NexusClaims AI: Enterprise Insurance Claims Orchestration & AI Fraud Triage Platform

Production-grade, event-driven health insurance claims processing and automated fraud detection platform built with **Java 21, Spring Boot 3.3+, Spring AI, Apache Kafka (KRaft mode), Redis 7, Angular 18, and Docker**.

---

## Workspace Directory Structure

```
nexus-claims-ai/
├── backend/
│   ├── common-contracts/               # Shared CloudEvents DTOs & Schemas
│   ├── api-gateway/                    # Spring Cloud Gateway (8080)
│   ├── claim-intake-service/           # Ingestion API & Producer (8081)
│   ├── claims-orchestrator-service/    # Saga Execution & Kafka Consumer (8082)
│   ├── ai-fraud-triage-service/        # Spring AI Analytics & Guardrails (8083)
│   └── billing-payment-service/        # Disbursement Service (8084)
├── frontend/                           # Angular 18 Signals-driven SPA (4200)
├── docker/
│   ├── docker-compose.yml              # Unified full-stack docker-compose
│   └── prometheus.yml                  # Prometheus metric target rules
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Quick Start (One Command Startup)

### 1. Launch Entire Infrastructure via Docker Compose
From the project root:

```bash
cd docker
docker-compose up -d --build
```

This starts:
- **Apache Kafka (KRaft mode)** on port `9092`
- **Redis 7** on port `6379`
- **PostgreSQL (PgVector 16)** on port `5432`
- **Spring Cloud Gateway** on port `8080`
- **Claim Intake Service** on port `8081`
- **Claims Orchestrator Service** on port `8082`
- **Spring AI Fraud Triage Service** on port `8083`
- **Billing Payment Service** on port `8084`
- **Angular 18 Frontend UI** on port `4200`

---

## Local Development (Build & Run Services Individually)

### Backend Build (Gradle Wrapper)
```bash
# From project root
./gradlew build -x test
```

### Run Angular App Locally
```bash
cd frontend
npm install
npm start
```
Access UI at `http://localhost:4200`

---

## Core Features & Testing Endpoints

### 1. Submit Claim via API Gateway (Port 8080)
```bash
curl -X POST http://localhost:8080/api/v1/claims \
  -H "Content-Type: application/json" \
  -H "X-User-ID: PROVIDER-99" \
  -d '{
    "providerId": "PRV-8831",
    "patientIdHash": "PAT-HASH-9921",
    "totalAmount": 14500.00,
    "primaryDiagnosisCode": "ICD10-I25.10",
    "clinicalNotes": "Complex evaluation procedure CPT-99215 billed with diagnostic lab scans."
  }'
```

### 2. Spring AI Fraud Triage Evaluation (Port 8083)
```bash
curl -X POST http://localhost:8080/api/v1/fraud/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "claimId": "CLM-99482",
    "providerId": "PRV-8831",
    "amount": 14500.00,
    "clinicalNotes": "Unbundled lab testing billed alongside routine checkup."
  }'
```

---

## Key Architecture Highlights

1. **Event-Driven Saga Pattern**: Choreography & orchestration hybrid handling claim intake -> AI fraud evaluation -> Saga approval -> Billing payout.
2. **Spring AI Guardrails**: Automatic PII masking, token bucket rate limiting, Redis semantic result caching, and Resilience4j heuristic fallbacks.
3. **Redis Idempotency**: Atomic set-if-absent keys (`idempotency:consumer:{group}:{eventId}`) preventing duplicate message processing.
4. **Angular Signals UI**: Signal-driven state management, JWT HTTP interceptors, route guards, and Adjuster Fraud Triage Workbench.
# NexusClaims AI
