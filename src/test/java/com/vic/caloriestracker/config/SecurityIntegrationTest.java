package com.vic.caloriestracker.config;

import com.vic.caloriestracker.entity.user;
import com.vic.caloriestracker.repository.userRepository;
import com.vic.caloriestracker.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private userRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void apiRoutesRequireJwtToken() throws Exception {
        mockMvc.perform(get("/api/meals/today").param("userId", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Authentication token is missing or invalid"));
    }

    @Test
    void apiRoutesRejectInvalidJwtToken() throws Exception {
        mockMvc.perform(get("/api/meals/today")
                        .param("userId", "1")
                        .header("Authorization", "Bearer not-a-real-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void apiRoutesAllowValidJwtToken() throws Exception {
        user demoUser = userRepository.findByEmail("demo@example.com").orElseThrow();
        String token = jwtUtil.generateToken(demoUser);

        mockMvc.perform(get("/api/meals/today")
                        .param("userId", demoUser.getId().toString())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void authRoutesStayPublicAndReturnToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "demo@example.com",
                                  "password": "demo-password"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(blankOrNullString())))
                .andExpect(jsonPath("$.email").value("demo@example.com"));
    }

    @Test
    void userProfileRoutesUseAuthenticatedUser() throws Exception {
        user demoUser = userRepository.findByEmail("demo@example.com").orElseThrow();
        String token = jwtUtil.generateToken(demoUser);

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(demoUser.getId()))
                .andExpect(jsonPath("$.email").value("demo@example.com"));
    }

    @Test
    void updateProfileRecalculatesCalorieGoal() throws Exception {
        user demoUser = userRepository.findByEmail("demo@example.com").orElseThrow();
        String token = jwtUtil.generateToken(demoUser);

        mockMvc.perform(put("/api/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "weight": 70,
                                  "height": 175,
                                  "age": 25,
                                  "activityLevel": "moderate",
                                  "dietaryPreferences": "High protein"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(70))
                .andExpect(jsonPath("$.height").value(175))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.activityLevel").value("moderate"))
                .andExpect(jsonPath("$.dietaryPreferences").value("High protein"))
                .andExpect(jsonPath("$.caloriesGoal").value(2594));
    }
}
