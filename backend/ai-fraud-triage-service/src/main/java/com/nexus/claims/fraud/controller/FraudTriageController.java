package com.nexus.claims.fraud.controller;

import com.nexus.claims.fraud.dto.FraudAnalysisRequest;
import com.nexus.claims.fraud.dto.FraudAnalysisResult;
import com.nexus.claims.fraud.service.FraudTriageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fraud")
public class FraudTriageController {

    private final FraudTriageService fraudTriageService;

    public FraudTriageController(FraudTriageService fraudTriageService) {
        this.fraudTriageService = fraudTriageService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<FraudAnalysisResult> evaluateClaim(@RequestBody FraudAnalysisRequest request) {
        FraudAnalysisResult result = fraudTriageService.evaluateClaim(
            request.claimId(),
            request.providerId(),
            request.clinicalNotes(),
            request.amount()
        );
        return ResponseEntity.ok(result);
    }
}
