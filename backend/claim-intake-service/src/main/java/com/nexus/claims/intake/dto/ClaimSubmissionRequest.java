package com.nexus.claims.intake.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClaimSubmissionRequest(
    @NotBlank(message = "Provider ID is required")
    String providerId,

    @NotBlank(message = "Patient ID Hash is required")
    String patientIdHash,

    @NotNull(message = "Total amount is required")
    @Min(value = 1, message = "Total amount must be greater than zero")
    Double totalAmount,

    @NotBlank(message = "Primary diagnosis code is required")
    String primaryDiagnosisCode,

    String clinicalNotes
) {}
