package com.nexus.claims.orchestrator.saga;

import com.nexus.claims.contracts.enums.ClaimStatus;
import com.nexus.claims.contracts.events.ClaimAdjudicatedEvent;
import com.nexus.claims.contracts.events.ClaimSubmittedEvent;
import com.nexus.claims.contracts.events.FraudEvaluatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SagaExecutionManager {

    private static final Logger log = LoggerFactory.getLogger(SagaExecutionManager.class);
    private static final String ADJUDICATED_TOPIC = "claim.saga.adjudicated.v1";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public SagaExecutionManager(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void handleClaimSubmitted(ClaimSubmittedEvent event) {
        log.info("Saga Step 1: Validated claim {}. Waiting for AI Fraud Triage evaluation...", event.claimId());
    }

    public void handleFraudEvaluated(FraudEvaluatedEvent event) {
        ClaimStatus finalStatus;
        String decisionNotes;

        if (event.riskScore() > 0.70) {
            finalStatus = ClaimStatus.FLAGGED_FOR_MANUAL_REVIEW;
            decisionNotes = "Flagged by Spring AI Fraud Engine for high risk score: " + event.riskScore() + ". Reason: " + event.reasoning();
        } else {
            finalStatus = ClaimStatus.AUTO_APPROVED;
            decisionNotes = "Auto-approved by Saga Orchestrator. AI Risk Score: " + event.riskScore();
        }

        log.info("Saga Final Step: Claim {} adjudicated to {}", event.claimId(), finalStatus);

        ClaimAdjudicatedEvent adjudicatedEvent = ClaimAdjudicatedEvent.create(
            event.claimId(),
            event.providerId(),
            1000.00, // Dummy approved amount for demo workflow
            finalStatus,
            decisionNotes
        );

        kafkaTemplate.send(ADJUDICATED_TOPIC, event.claimId(), adjudicatedEvent);
    }
}
