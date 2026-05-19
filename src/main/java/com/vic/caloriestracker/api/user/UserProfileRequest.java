package com.vic.caloriestracker.api.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserProfileRequest {

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than zero")
    private Double weight;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be greater than zero")
    private Double height;

    @NotNull(message = "Age is required")
    @Positive(message = "Age must be greater than zero")
    private Integer age;

    @NotBlank(message = "Activity level is required")
    private String activityLevel;

    private String dietaryPreferences;
}
