package com.nexus.claims.intake.dto;

import com.nexus.claims.contracts.enums.ClaimStatus;
import java.time.Instant;

public record ClaimResponse(
    String claimId,
    String providerId,
    Double totalAmount,
    ClaimStatus status,
    Instant createdAt
) {}
