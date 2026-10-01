package com.nexus.claims.contracts.events;

import com.nexus.claims.contracts.enums.RiskClassification;
import java.time.Instant;
import java.util.UUID;

public record FraudEvaluatedEvent(
    UUID eventId,
    String claimId,
    String providerId,
    Double riskScore,
    RiskClassification classification,
    String reasoning,
    boolean cachedResponse,
    Instant timestamp,
    String version
) {
    public static FraudEvaluatedEvent create(
        String claimId,
        String providerId,
        Double riskScore,
        RiskClassification classification,
        String reasoning,
        boolean cachedResponse
    ) {
        return new FraudEvaluatedEvent(
            UUID.randomUUID(),
            claimId,
            providerId,
            riskScore,
            classification,
            reasoning,
            cachedResponse,
            Instant.now(),
            "1.0"
        );
    }
}
