package com.vic.caloriestracker.service;

import com.vic.caloriestracker.api.auth.LoginRequest;
import com.vic.caloriestracker.api.auth.RegisterRequest;
import com.vic.caloriestracker.entity.user;
import com.vic.caloriestracker.repository.userRepository;
import com.vic.caloriestracker.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private userRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil = new JwtUtil("test-calories-tracker-secret-key-for-jwt-tests", 86400000);

    @Test
    void registerHashesPasswordSavesUserAndReturnsToken() {
        RegisterRequest request = registerRequest();
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(user.class))).thenAnswer(invocation -> {
            user savedUser = invocation.getArgument(0);
            savedUser.setId(5L);
            return savedUser;
        });

        var response = newService().register(request);

        ArgumentCaptor<user> userCaptor = ArgumentCaptor.forClass(user.class);
        verify(userRepository).save(userCaptor.capture());
        user savedUser = userCaptor.getValue();

        assertThat(savedUser.getPasswordHash()).isNotEqualTo("password123");
        assertThat(passwordEncoder.matches("password123", savedUser.getPasswordHash())).isTrue();
        assertThat(response.getUserId()).isEqualTo(5L);
        assertThat(jwtUtil.validateToken(response.getToken())).isTrue();
        assertThat(jwtUtil.extractUserId(response.getToken())).isEqualTo(5L);
    }

    @Test
    void registerRejectsDuplicateEmail() {
        RegisterRequest request = registerRequest();
        when(userRepository.findByEmail("new@example.com"))
                .thenReturn(Optional.of(new user("Existing", "new@example.com", "hash", 2000, LocalDateTime.now())));

        assertThatThrownBy(() -> newService().register(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("409 CONFLICT");
    }

    @Test
    void loginReturnsTokenForValidPassword() {
        user foundUser = new user("Demo User", "demo@example.com", passwordEncoder.encode("password123"), 2000, LocalDateTime.now());
        foundUser.setId(1L);
        when(userRepository.findByEmail("demo@example.com")).thenReturn(Optional.of(foundUser));

        var response = newService().login(loginRequest("password123"));

        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(jwtUtil.validateToken(response.getToken())).isTrue();
    }

    @Test
    void loginRejectsBadPassword() {
        user foundUser = new user("Demo User", "demo@example.com", passwordEncoder.encode("password123"), 2000, LocalDateTime.now());
        foundUser.setId(1L);
        when(userRepository.findByEmail("demo@example.com")).thenReturn(Optional.of(foundUser));

        assertThatThrownBy(() -> newService().login(loginRequest("wrong-password")))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("401 UNAUTHORIZED");
    }

    private AuthService newService() {
        return new AuthService(userRepository, passwordEncoder, jwtUtil);
    }

    private RegisterRequest registerRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setName("New User");
        request.setEmail("new@example.com");
        request.setPassword("password123");
        request.setCaloriesGoal(2000);
        return request;
    }

    private LoginRequest loginRequest(String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail("demo@example.com");
        request.setPassword(password);
        return request;
    }
}
