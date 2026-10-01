package com.nexus.claims.contracts.events;

import java.time.Instant;
import java.util.UUID;

public record ClaimSubmittedEvent(
    UUID eventId,
    String claimId,
    String providerId,
    String patientIdHash,
    Double totalAmount,
    String primaryDiagnosisCode,
    String clinicalNotes,
    Instant timestamp,
    String version
) {
    public static ClaimSubmittedEvent create(
        String claimId, 
        String providerId, 
        String patientIdHash, 
        Double totalAmount, 
        String primaryDiagnosisCode, 
        String clinicalNotes
    ) {
        return new ClaimSubmittedEvent(
            UUID.randomUUID(),
            claimId,
            providerId,
            patientIdHash,
            totalAmount,
            primaryDiagnosisCode,
            clinicalNotes,
            Instant.now(),
            "1.0"
        );
    }
}
