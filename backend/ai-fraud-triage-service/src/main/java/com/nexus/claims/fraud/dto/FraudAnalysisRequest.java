package com.nexus.claims.fraud.dto;

public record FraudAnalysisRequest(
    String claimId,
    String providerId,
    Double amount,
    String clinicalNotes
) {}
