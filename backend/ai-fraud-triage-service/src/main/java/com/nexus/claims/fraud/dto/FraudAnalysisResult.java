package com.nexus.claims.fraud.dto;

import com.nexus.claims.contracts.enums.RiskClassification;

public record FraudAnalysisResult(
    String claimId,
    Double riskScore,
    RiskClassification classification,
    String reasoning,
    boolean cached
) {}
