package com.roster.planner.v0.service;

import com.roster.planner.v0.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class TokenCleanupService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    /**
     * This task runs every 24 hours to remove expired tokens.
     * fixedRate is in milliseconds (86400000 ms = 24 hours).
     */
    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void removeExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        // You'll need to add a method in your Repository for this
        tokenRepository.deleteByExpiryDateBefore(now);
        System.out.println("Scheduled Task: Cleaned up expired password reset tokens at " + now);
    }
}