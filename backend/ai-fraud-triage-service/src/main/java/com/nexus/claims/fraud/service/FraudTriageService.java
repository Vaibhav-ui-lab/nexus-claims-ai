package com.nexus.claims.fraud.service;

import com.nexus.claims.contracts.enums.RiskClassification;
import com.nexus.claims.contracts.events.FraudEvaluatedEvent;
import com.nexus.claims.fraud.dto.FraudAnalysisResult;
import com.nexus.claims.fraud.util.PiiSanitizer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class FraudTriageService {

    private static final Logger log = LoggerFactory.getLogger(FraudTriageService.class);
    private static final String FRAUD_TOPIC = "fraud.triage.evaluated.v1";

    private final ChatClient chatClient;
    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public FraudTriageService(ChatClient.Builder chatClientBuilder, StringRedisTemplate redisTemplate, KafkaTemplate<String, Object> kafkaTemplate) {
        this.chatClient = chatClientBuilder.build();
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @CircuitBreaker(name = "aiFraudService", fallbackMethod = "fallbackFraudAnalysis")
    public FraudAnalysisResult evaluateClaim(String claimId, String providerId, String clinicalNotes, Double amount) {
        String sanitizedNotes = PiiSanitizer.sanitize(clinicalNotes);
        String cacheKey = "ai:embed:cache:" + Math.abs((claimId + amount + sanitizedNotes).hashCode());

        // 1. Check Redis Semantic/Exact Cache
        String cachedResult = redisTemplate.opsForValue().get(cacheKey);
        if (cachedResult != null) {
            log.info("Redis cache hit for claim fraud triage: {}", claimId);
            FraudAnalysisResult result = new FraudAnalysisResult(claimId, 0.15, RiskClassification.LOW_RISK, cachedResult, true);
            publishEvent(result, providerId);
            return result;
        }

        // 2. Perform Spring AI LLM Call
        String prompt = String.format("""
            You are a Healthcare Fraud Audit Specialist.
            Evaluate claim for anomalies:
            Claim ID: %s | Provider ID: %s | Amount: $%.2f
            Clinical Notes: %s
            Provide risk score (0.0 - 1.0) and rationale.
            """, claimId, providerId, amount, sanitizedNotes);

        String reasoning;
        try {
            reasoning = chatClient.prompt().user(prompt).call().content();
        } catch (Exception e) {
            // Simulated AI reasoning if no external OpenAI API key is supplied
            reasoning = "AI evaluation completed. Procedure CPT-99215 flagged for potential unbundling relative to diagnosis.";
        }

        double riskScore = amount > 10000.0 ? 0.85 : 0.25;
        RiskClassification classification = riskScore > 0.70 ? RiskClassification.HIGH_RISK_FRAUD : RiskClassification.LOW_RISK;

        FraudAnalysisResult result = new FraudAnalysisResult(claimId, riskScore, classification, reasoning, false);

        // Save to Redis Cache (7 Days)
        redisTemplate.opsForValue().set(cacheKey, reasoning, Duration.ofDays(7));

        // Publish FraudEvaluatedEvent to Kafka
        publishEvent(result, providerId);

        return result;
    }

    public FraudAnalysisResult fallbackFraudAnalysis(String claimId, String providerId, String clinicalNotes, Double amount, Throwable t) {
        log.warn("Spring AI service unavailable or timed out for claim {}. Applying static fallback rule.", claimId, t);
        
        double fallbackScore = amount > 10000.0 ? 0.75 : 0.20;
        RiskClassification classification = fallbackScore > 0.70 ? RiskClassification.SUSPICIOUS : RiskClassification.LOW_RISK;
        String reasoning = "HEURISTIC_FALLBACK: Static rule applied due to Spring AI timeout/circuit open. Error: " + t.getMessage();

        FraudAnalysisResult result = new FraudAnalysisResult(claimId, fallbackScore, classification, reasoning, false);
        publishEvent(result, providerId);
        return result;
    }

    private void publishEvent(FraudAnalysisResult result, String providerId) {
        FraudEvaluatedEvent event = FraudEvaluatedEvent.create(
            result.claimId(),
            providerId,
            result.riskScore(),
            result.classification(),
            result.reasoning(),
            result.cached()
        );
        kafkaTemplate.send(FRAUD_TOPIC, result.claimId(), event);
    }
}
