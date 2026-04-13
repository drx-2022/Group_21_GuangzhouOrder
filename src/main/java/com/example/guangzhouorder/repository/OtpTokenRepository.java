package com.example.guangzhouorder.repository;

import com.example.guangzhouorder.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findTopByEmailAndPurposeAndUsedFalseOrderByIdDesc(String email, String purpose);
    void deleteAllByEmail(String email);
}

