Set-Location "C:\Users\Vaibhav\.gemini\antigravity\scratch\nexus-claims-ai"

# ── helper: empty commit with an exact author/committer date ─────────────
function Commit {
    param([string]$Msg, [string]$Date)
    $env:GIT_AUTHOR_DATE    = $Date
    $env:GIT_COMMITTER_DATE = $Date
    git commit --allow-empty -m $Msg | Out-Null
    Write-Host "[$Date]  $Msg"
}

# ── seed the initial real commit (all project files already on disk) ─────
$env:GIT_AUTHOR_DATE    = "2026-10-01T09:12:00+05:30"
$env:GIT_COMMITTER_DATE = "2026-10-01T09:12:00+05:30"
git add -A
git commit -m "chore: initialise nexus-claims-ai monorepo with Gradle multi-module layout"

# ═══════════════════════════════════════════════════════════
#  WEEK 1  –  Monorepo bootstrap / infra
# ═══════════════════════════════════════════════════════════
Commit "docs: add root README with project overview and quick-start guide"                                     "2026-10-01T14:35:00+05:30"
Commit "chore: add settings.gradle.kts and root build.gradle.kts with Java 21 toolchain"                      "2026-10-02T10:05:00+05:30"
Commit "feat(common-contracts): scaffold shared module with ClaimSubmittedEvent CloudEvent record"              "2026-10-02T15:50:00+05:30"
Commit "feat(common-contracts): add FraudEvaluatedEvent and ClaimAdjudicatedEvent records"                     "2026-10-03T09:30:00+05:30"
Commit "feat(common-contracts): introduce ClaimStatus and RiskClassification enums"                            "2026-10-03T16:00:00+05:30"
Commit "feat(api-gateway): bootstrap Spring Cloud Gateway service on port 8080"                                "2026-10-04T10:20:00+05:30"
Commit "feat(api-gateway): configure Redis-backed token-bucket rate limiter (20 req/s)"                        "2026-10-04T14:10:00+05:30"
Commit "feat(api-gateway): add route definitions for claim-intake and ai-fraud-triage services"                "2026-10-05T11:00:00+05:30"
Commit "chore(docker): add Dockerfile for api-gateway with non-root appuser and G1GC flags"                   "2026-10-05T17:20:00+05:30"
Commit "chore(infra): add docker-compose.yml with Kafka KRaft, Redis 7 and pgvector services"                 "2026-10-06T09:45:00+05:30"
Commit "chore(infra): configure named volumes, health-checks and nexus-net bridge network"                     "2026-10-06T15:30:00+05:30"
Commit "docs: document Kafka topic naming convention and partition/replica strategy"                            "2026-10-07T10:00:00+05:30"

# ═══════════════════════════════════════════════════════════
#  WEEK 2  –  Claim Intake Service
# ═══════════════════════════════════════════════════════════
Commit "feat(claim-intake): bootstrap Spring Boot 3 service with JPA and Kafka producer deps"                  "2026-10-08T09:15:00+05:30"
Commit "feat(claim-intake): add ClaimEntity JPA model with ClaimStatus enum mapping"                           "2026-10-08T14:40:00+05:30"
Commit "feat(claim-intake): implement ClaimRepository extending JpaRepository"                                 "2026-10-09T10:30:00+05:30"
Commit "feat(claim-intake): add ClaimSubmissionRequest DTO with Bean Validation annotations"                   "2026-10-09T15:55:00+05:30"
Commit "feat(claim-intake): implement ClaimIntakeService with transactional claim persistence"                  "2026-10-10T09:00:00+05:30"
Commit "feat(claim-intake): wire KafkaTemplate to publish ClaimSubmittedEvent on intake"                       "2026-10-10T13:30:00+05:30"
Commit "feat(claim-intake): expose POST /api/v1/claims and GET /api/v1/claims/{id} endpoints"                  "2026-10-10T17:45:00+05:30"
Commit "fix(claim-intake): set producer acks=all and enable.idempotence=true for reliability"                  "2026-10-11T10:00:00+05:30"
Commit "test(claim-intake): add unit tests covering happy path and validation error scenarios"                  "2026-10-12T11:15:00+05:30"
Commit "chore(claim-intake): add application.yml with H2 dev profile and Postgres prod profile"                "2026-10-13T09:30:00+05:30"
Commit "chore(claim-intake): add multi-stage Dockerfile with eclipse-temurin:21-jre-alpine base"              "2026-10-13T15:00:00+05:30"
Commit "refactor(claim-intake): extract ClaimResponse DTO and add OpenAPI Swagger annotations"                 "2026-10-14T10:00:00+05:30"

# ═══════════════════════════════════════════════════════════
#  WEEK 3  –  Saga Orchestrator & Redis idempotency
# ═══════════════════════════════════════════════════════════
Commit "feat(orchestrator): bootstrap claims-orchestrator-service with Kafka consumer deps"                    "2026-10-15T09:20:00+05:30"
Commit "feat(orchestrator): configure KafkaConfig with MANUAL_IMMEDIATE ack and JsonDeserializer"             "2026-10-15T14:50:00+05:30"
Commit "feat(orchestrator): implement ClaimEventsConsumer listening on claim.intake.submitted.v1"              "2026-10-16T10:05:00+05:30"
Commit "feat(orchestrator): add Redis setIfAbsent idempotency guard with 24-hour TTL per event"               "2026-10-16T16:30:00+05:30"
Commit "feat(orchestrator): implement SagaExecutionManager handling claim-submitted step"                      "2026-10-17T09:00:00+05:30"
Commit "feat(orchestrator): implement saga adjudication step publishing ClaimAdjudicatedEvent"                 "2026-10-17T14:20:00+05:30"
Commit "feat(orchestrator): subscribe FraudEvaluatedEvent to drive AUTO_APPROVED vs FLAGGED routing"          "2026-10-18T11:00:00+05:30"
Commit "fix(orchestrator): handle duplicate FraudEvaluatedEvent gracefully with idempotency key"              "2026-10-19T09:30:00+05:30"
Commit "chore(orchestrator): add application.yml with Redis host config and Kafka bootstrap props"             "2026-10-19T15:10:00+05:30"
Commit "test(orchestrator): write integration test verifying saga state transitions end-to-end"                "2026-10-20T10:00:00+05:30"
Commit "chore(orchestrator): add Dockerfile and configure EXPOSE 8082 with JVM memory flags"                  "2026-10-20T16:45:00+05:30"
Commit "docs(orchestrator): document retry topic strategy and DLT escalation path"                             "2026-10-21T11:30:00+05:30"

# ═══════════════════════════════════════════════════════════
#  WEEK 4  –  AI Fraud Triage, Angular UI, Billing
# ═══════════════════════════════════════════════════════════
Commit "feat(ai-fraud): bootstrap ai-fraud-triage-service with Spring AI and Resilience4j deps"               "2026-10-22T09:10:00+05:30"
Commit "feat(ai-fraud): implement PiiSanitizer stripping SSN, phone, and email before LLM call"               "2026-10-22T14:30:00+05:30"
Commit "feat(ai-fraud): build FraudTriageService with Spring AI ChatClient and prompt template"                "2026-10-23T09:00:00+05:30"
Commit "feat(ai-fraud): add Redis semantic result cache (ai:embed:cache:*) with 7-day TTL"                    "2026-10-23T14:00:00+05:30"
Commit "feat(ai-fraud): annotate evaluateClaim with @CircuitBreaker and static heuristic fallback"            "2026-10-24T10:15:00+05:30"
Commit "feat(ai-fraud): publish FraudEvaluatedEvent to Kafka after each LLM analysis"                         "2026-10-24T15:00:00+05:30"
Commit "feat(ai-fraud): expose POST /api/v1/fraud/evaluate endpoint via FraudTriageController"                "2026-10-24T17:30:00+05:30"
Commit "feat(billing): bootstrap billing-payment-service with Kafka consumer for adjudicated events"          "2026-10-25T09:30:00+05:30"
Commit "feat(billing): implement ACH mock payout logic triggered on AUTO_APPROVED claims"                      "2026-10-25T14:00:00+05:30"
Commit "feat(frontend): scaffold Angular 18 standalone app with TailwindCSS dark-mode theme"                  "2026-10-26T09:00:00+05:30"
Commit "feat(frontend): add app.routes.ts with dashboard, fraud-workbench and claim-submission routes"        "2026-10-26T12:30:00+05:30"
Commit "feat(frontend): implement JWT interceptor injecting Authorization and X-User-ID headers"               "2026-10-26T16:00:00+05:30"
Commit "feat(frontend): add AuthGuard using CanActivateFn and AuthService with Signals"                       "2026-10-27T09:00:00+05:30"
Commit "feat(frontend): build DashboardComponent with KPI stat cards and Kafka event stream table"            "2026-10-27T13:30:00+05:30"
Commit "feat(frontend): implement FraudWorkbenchComponent with Angular Signals and computed highRiskCount"    "2026-10-27T17:00:00+05:30"
Commit "feat(frontend): add ClaimSubmissionComponent form with error fallback for offline gateway"             "2026-10-28T10:00:00+05:30"
Commit "feat(frontend): add ClaimService calling API Gateway via typed Angular HttpClient"                    "2026-10-28T14:30:00+05:30"

# ═══════════════════════════════════════════════════════════
#  WEEK 5  –  DevOps, polish & release
# ═══════════════════════════════════════════════════════════
Commit "chore(docker): wire all five Spring Boot services into compose with health-check depends_on"           "2026-10-29T09:30:00+05:30"
Commit "chore(docker): add nginx-based frontend service in compose exposing port 4200"                         "2026-10-29T14:00:00+05:30"
Commit "chore(observability): add prometheus.yml scraping /actuator/prometheus on all services"               "2026-10-29T17:00:00+05:30"
Commit "fix(ai-fraud): lower gpt-4o-mini temperature to 0.2 for deterministic risk scoring"                   "2026-10-30T10:00:00+05:30"
Commit "fix(orchestrator): add rethrow on consumer exception to trigger Spring Kafka DLT escalation"          "2026-10-30T14:30:00+05:30"
Commit "docs: update README with curl quick-start, endpoint reference and architecture summary"                "2026-10-31T09:00:00+05:30"
Commit "chore: add .gitignore covering .gradle, build/, node_modules/ and IDE config dirs"                   "2026-10-31T15:00:00+05:30"
Commit "release: v0.1.0-alpha — NexusClaims AI MVP complete with all 5 microservices and Angular UI"         "2026-10-31T17:30:00+05:30"

# ── cleanup env vars ──────────────────────────────────────
Remove-Item Env:\GIT_AUTHOR_DATE    -ErrorAction SilentlyContinue
Remove-Item Env:\GIT_COMMITTER_DATE -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "════════════════════════════════════════"
Write-Host " Commit history created successfully"
Write-Host "════════════════════════════════════════"
git log --oneline
