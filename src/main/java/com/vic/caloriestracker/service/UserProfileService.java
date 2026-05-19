package com.vic.caloriestracker.service;

import com.vic.caloriestracker.api.user.UserProfileRequest;
import com.vic.caloriestracker.api.user.UserProfileResponse;
import com.vic.caloriestracker.entity.user;
import com.vic.caloriestracker.repository.userRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserProfileService {

    private final userRepository userRepository;
    private final CalorieCalculatorService calorieCalculatorService;

    public UserProfileService(userRepository userRepository,
                              CalorieCalculatorService calorieCalculatorService) {
        this.userRepository = userRepository;
        this.calorieCalculatorService = calorieCalculatorService;
    }

    public UserProfileResponse getProfile(Long userId) {
        return UserProfileResponse.from(findUser(userId));
    }

    public UserProfileResponse updateProfile(Long userId, UserProfileRequest profileDto) {
        user foundUser = findUser(userId);
        int calculatedGoal = calorieCalculatorService.calculateTDEE(
                profileDto.getWeight(),
                profileDto.getHeight(),
                profileDto.getAge(),
                profileDto.getActivityLevel()
        );

        foundUser.setWeight(profileDto.getWeight());
        foundUser.setHeight(profileDto.getHeight());
        foundUser.setAge(profileDto.getAge());
        foundUser.setActivityLevel(profileDto.getActivityLevel());
        foundUser.setDietaryPreferences(profileDto.getDietaryPreferences());
        foundUser.setCaloriesGoal(calculatedGoal);

        return UserProfileResponse.from(userRepository.save(foundUser));
    }

    public Long userIdForEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user::getId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with email: " + email));
    }

    private user findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with id: " + userId));
    }
}
