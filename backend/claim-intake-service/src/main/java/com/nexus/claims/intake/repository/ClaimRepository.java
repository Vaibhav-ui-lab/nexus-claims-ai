package com.nexus.claims.intake.repository;

import com.nexus.claims.intake.model.ClaimEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRepository extends JpaRepository<ClaimEntity, String> {
}
