package com.nexus.claims.orchestrator.kafka;

import com.nexus.claims.contracts.events.ClaimSubmittedEvent;
import com.nexus.claims.contracts.events.FraudEvaluatedEvent;
import com.nexus.claims.orchestrator.saga.SagaExecutionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ClaimEventsConsumer {

    private static final Logger log = LoggerFactory.getLogger(ClaimEventsConsumer.class);
    private final StringRedisTemplate redisTemplate;
    private final SagaExecutionManager sagaExecutionManager;

    public ClaimEventsConsumer(StringRedisTemplate redisTemplate, SagaExecutionManager sagaExecutionManager) {
        this.redisTemplate = redisTemplate;
        this.sagaExecutionManager = sagaExecutionManager;
    }

    @KafkaListener(
        topics = "claim.intake.submitted.v1",
        groupId = "claims-orchestrator-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void onClaimSubmitted(ClaimSubmittedEvent event, Acknowledgment ack) {
        String idempotencyKey = "idempotency:consumer:orchestrator:" + event.eventId();
        Boolean isFirstTime = redisTemplate.opsForValue().setIfAbsent(idempotencyKey, "PROCESSED", Duration.ofHours(24));

        if (Boolean.FALSE.equals(isFirstTime)) {
            log.warn("Duplicate ClaimSubmittedEvent detected: {}. Skipping.", event.eventId());
            ack.acknowledge();
            return;
        }

        try {
            log.info("Starting Saga Workflow for Claim ID: {}", event.claimId());
            sagaExecutionManager.handleClaimSubmitted(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing saga for claim: {}", event.claimId(), e);
            throw e;
        }
    }

    @KafkaListener(
        topics = "fraud.triage.evaluated.v1",
        groupId = "claims-orchestrator-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void onFraudEvaluated(FraudEvaluatedEvent event, Acknowledgment ack) {
        String idempotencyKey = "idempotency:consumer:orchestrator:" + event.eventId();
        Boolean isFirstTime = redisTemplate.opsForValue().setIfAbsent(idempotencyKey, "PROCESSED", Duration.ofHours(24));

        if (Boolean.FALSE.equals(isFirstTime)) {
            log.warn("Duplicate FraudEvaluatedEvent detected: {}. Skipping.", event.eventId());
            ack.acknowledge();
            return;
        }

        try {
            log.info("Saga Workflow receiving AI Fraud result for Claim ID: {} Score: {}", event.claimId(), event.riskScore());
            sagaExecutionManager.handleFraudEvaluated(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing AI Fraud result for claim: {}", event.claimId(), e);
            throw e;
        }
    }
}
