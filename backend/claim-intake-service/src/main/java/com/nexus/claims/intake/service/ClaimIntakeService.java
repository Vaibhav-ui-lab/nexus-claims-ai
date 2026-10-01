package com.nexus.claims.intake.service;

import com.nexus.claims.contracts.enums.ClaimStatus;
import com.nexus.claims.contracts.events.ClaimSubmittedEvent;
import com.nexus.claims.intake.dto.ClaimResponse;
import com.nexus.claims.intake.dto.ClaimSubmissionRequest;
import com.nexus.claims.intake.model.ClaimEntity;
import com.nexus.claims.intake.repository.ClaimRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class ClaimIntakeService {

    private static final Logger log = LoggerFactory.getLogger(ClaimIntakeService.class);
    private static final String INTAKE_TOPIC = "claim.intake.submitted.v1";

    private final ClaimRepository claimRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ClaimIntakeService(ClaimRepository claimRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.claimRepository = claimRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public ClaimResponse submitClaim(ClaimSubmissionRequest request) {
        String claimId = "CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ClaimEntity entity = new ClaimEntity(
            claimId,
            request.providerId(),
            request.patientIdHash(),
            request.totalAmount(),
            request.primaryDiagnosisCode(),
            request.clinicalNotes(),
            ClaimStatus.SUBMITTED,
            Instant.now()
        );

        claimRepository.save(entity);

        // Build CloudEvent record
        ClaimSubmittedEvent event = ClaimSubmittedEvent.create(
            claimId,
            request.providerId(),
            request.patientIdHash(),
            request.totalAmount(),
            request.primaryDiagnosisCode(),
            request.clinicalNotes()
        );

        // Publish to Kafka KRaft topic
        kafkaTemplate.send(INTAKE_TOPIC, claimId, event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("ClaimSubmittedEvent published to Kafka for claimId: {}", claimId);
                } else {
                    log.error("Failed to publish ClaimSubmittedEvent for claimId: {}", claimId, ex);
                }
            });

        return new ClaimResponse(claimId, entity.getProviderId(), entity.getTotalAmount(), entity.getStatus(), entity.getCreatedAt());
    }

    public ClaimResponse getClaim(String claimId) {
        return claimRepository.findById(claimId)
            .map(c -> new ClaimResponse(c.getClaimId(), c.getProviderId(), c.getTotalAmount(), c.getStatus(), c.getCreatedAt()))
            .orElseThrow(() -> new RuntimeException("Claim not found: " + claimId));
    }
}
