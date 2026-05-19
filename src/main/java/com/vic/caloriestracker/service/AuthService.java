package com.vic.caloriestracker.service;

import com.vic.caloriestracker.api.auth.AuthResponse;
import com.vic.caloriestracker.api.auth.LoginRequest;
import com.vic.caloriestracker.api.auth.RegisterRequest;
import com.vic.caloriestracker.entity.user;
import com.vic.caloriestracker.repository.userRepository;
import com.vic.caloriestracker.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(userRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(existingUser -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Email is already registered: " + request.getEmail());
                });

        user newUser = new user(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getCaloriesGoal(),
                LocalDateTime.now()
        );
        user savedUser = userRepository.save(newUser);
        return new AuthResponse(jwtUtil.generateToken(savedUser), savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        user foundUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> invalidCredentials());

        if (passwordEncoder.matches(request.getPassword(), foundUser.getPasswordHash())) {
            return new AuthResponse(jwtUtil.generateToken(foundUser), foundUser);
        }

        if (request.getPassword().equals(foundUser.getPasswordHash())) {
            foundUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            user upgradedUser = userRepository.save(foundUser);
            return new AuthResponse(jwtUtil.generateToken(upgradedUser), upgradedUser);
        }

        throw invalidCredentials();
    }

    private ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
}
