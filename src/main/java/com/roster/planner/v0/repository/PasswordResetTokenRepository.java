package com.roster.planner.v0.repository;

import com.roster.planner.v0.entity.PasswordResetToken;
import com.roster.planner.v0.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user); // Good for cleaning up old tokens
    void deleteByExpiryDateBefore(LocalDateTime now);
}