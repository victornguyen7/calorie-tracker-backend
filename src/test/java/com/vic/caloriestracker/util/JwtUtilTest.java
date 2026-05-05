package com.vic.caloriestracker.util;

import com.vic.caloriestracker.entity.user;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    @Test
    void generatedTokenCanBeValidatedAndExtractUserId() {
        JwtUtil jwtUtil = new JwtUtil("test-calories-tracker-secret-key-for-jwt-tests", 86400000);
        user demoUser = new user("Demo User", "demo@example.com", "hash", 2000, LocalDateTime.now());
        demoUser.setId(1L);

        String token = jwtUtil.generateToken(demoUser);

        assertThat(jwtUtil.validateToken(token)).isTrue();
        assertThat(jwtUtil.extractUserId(token)).isEqualTo(1L);
    }

    @Test
    void validateTokenReturnsFalseForBadToken() {
        JwtUtil jwtUtil = new JwtUtil("test-calories-tracker-secret-key-for-jwt-tests", 86400000);

        assertThat(jwtUtil.validateToken("not-a-real-token")).isFalse();
    }
}
