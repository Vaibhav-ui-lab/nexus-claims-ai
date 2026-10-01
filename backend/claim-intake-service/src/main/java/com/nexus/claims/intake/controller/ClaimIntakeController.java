package com.nexus.claims.intake.controller;

import com.nexus.claims.intake.dto.ClaimResponse;
import com.nexus.claims.intake.dto.ClaimSubmissionRequest;
import com.nexus.claims.intake.service.ClaimIntakeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/claims")
public class ClaimIntakeController {

    private final ClaimIntakeService claimIntakeService;

    public ClaimIntakeController(ClaimIntakeService claimIntakeService) {
        this.claimIntakeService = claimIntakeService;
    }

    @PostMapping
    public ResponseEntity<ClaimResponse> submitClaim(@Valid @RequestBody ClaimSubmissionRequest request) {
        ClaimResponse response = claimIntakeService.submitClaim(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<ClaimResponse> getClaim(@PathVariable String claimId) {
        return ResponseEntity.ok(claimIntakeService.getClaim(claimId));
    }
}
