package com.vic.caloriestracker.api.auth;

import com.vic.caloriestracker.entity.user;
import lombok.Getter;

@Getter
public class AuthResponse {

    private final String token;
    private final Long userId;
    private final String email;
    private final String name;

    public AuthResponse(String token, user foundUser) {
        this.token = token;
        this.userId = foundUser.getId();
        this.email = foundUser.getEmail();
        this.name = foundUser.getName();
    }

}
