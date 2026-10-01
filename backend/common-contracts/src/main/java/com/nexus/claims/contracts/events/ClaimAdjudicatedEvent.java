package com.nexus.claims.contracts.events;

import com.nexus.claims.contracts.enums.ClaimStatus;
import java.time.Instant;
import java.util.UUID;

public record ClaimAdjudicatedEvent(
    UUID eventId,
    String claimId,
    String providerId,
    Double amountApproved,
    ClaimStatus finalStatus,
    String decisionNotes,
    Instant timestamp,
    String version
) {
    public static ClaimAdjudicatedEvent create(
        String claimId,
        String providerId,
        Double amountApproved,
        ClaimStatus finalStatus,
        String decisionNotes
    ) {
        return new ClaimAdjudicatedEvent(
            UUID.randomUUID(),
            claimId,
            providerId,
            amountApproved,
            finalStatus,
            decisionNotes,
            Instant.now(),
            "1.0"
        );
    }
}
