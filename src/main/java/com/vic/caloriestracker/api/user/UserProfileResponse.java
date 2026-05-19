package com.vic.caloriestracker.api.user;

import com.vic.caloriestracker.entity.user;

import java.time.LocalDateTime;

public class UserProfileResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final int caloriesGoal;
    private final Double weight;
    private final Double height;
    private final Integer age;
    private final String activityLevel;
    private final String dietaryPreferences;
    private final LocalDateTime createdAt;

    private UserProfileResponse(user foundUser) {
        this.id = foundUser.getId();
        this.name = foundUser.getName();
        this.email = foundUser.getEmail();
        this.caloriesGoal = foundUser.getCaloriesGoal();
        this.weight = foundUser.getWeight();
        this.height = foundUser.getHeight();
        this.age = foundUser.getAge();
        this.activityLevel = foundUser.getActivityLevel();
        this.dietaryPreferences = foundUser.getDietaryPreferences();
        this.createdAt = foundUser.getCreatedAt();
    }

    public static UserProfileResponse from(user foundUser) {
        return new UserProfileResponse(foundUser);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getCaloriesGoal() {
        return caloriesGoal;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getHeight() {
        return height;
    }

    public Integer getAge() {
        return age;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public String getDietaryPreferences() {
        return dietaryPreferences;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
