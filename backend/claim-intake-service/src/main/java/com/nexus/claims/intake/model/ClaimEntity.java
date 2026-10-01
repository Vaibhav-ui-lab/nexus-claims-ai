package com.nexus.claims.intake.model;

import com.nexus.claims.contracts.enums.ClaimStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "claims")
public class ClaimEntity {

    @Id
    private String claimId;
    
    private String providerId;
    private String patientIdHash;
    private Double totalAmount;
    private String primaryDiagnosisCode;
    
    @Column(length = 2000)
    private String clinicalNotes;
    
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;
    
    private Instant createdAt;

    public ClaimEntity() {}

    public ClaimEntity(String claimId, String providerId, String patientIdHash, Double totalAmount, String primaryDiagnosisCode, String clinicalNotes, ClaimStatus status, Instant createdAt) {
        this.claimId = claimId;
        this.providerId = providerId;
        this.patientIdHash = patientIdHash;
        this.totalAmount = totalAmount;
        this.primaryDiagnosisCode = primaryDiagnosisCode;
        this.clinicalNotes = clinicalNotes;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getClaimId() { return claimId; }
    public void setClaimId(String claimId) { this.claimId = claimId; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public String getPatientIdHash() { return patientIdHash; }
    public void setPatientIdHash(String patientIdHash) { this.patientIdHash = patientIdHash; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getPrimaryDiagnosisCode() { return primaryDiagnosisCode; }
    public void setPrimaryDiagnosisCode(String primaryDiagnosisCode) { this.primaryDiagnosisCode = primaryDiagnosisCode; }

    public String getClinicalNotes() { return clinicalNotes; }
    public void setClinicalNotes(String clinicalNotes) { this.clinicalNotes = clinicalNotes; }

    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
