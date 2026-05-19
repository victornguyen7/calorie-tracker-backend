package com.vic.caloriestracker.api.user;

import com.vic.caloriestracker.service.CalorieCalculatorService;
import com.vic.caloriestracker.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserCalorieController {

    private final CalorieCalculatorService calorieCalculatorService;
    private final UserProfileService userProfileService;

    public UserCalorieController(CalorieCalculatorService calorieCalculatorService,
                                 UserProfileService userProfileService) {
        this.calorieCalculatorService = calorieCalculatorService;
        this.userProfileService = userProfileService;
    }

    @GetMapping("/{id}/calorie-summary")
    public CalorieSummaryResponse getCalorieSummary(@PathVariable Long id,
                                                    @RequestParam(required = false) Integer date) {
        return calorieCalculatorService.getCalorieSummary(id, date);
    }

    @GetMapping("/me")
    public UserProfileResponse getMyProfile(@AuthenticationPrincipal UserDetails currentUser) {
        Long userId = userProfileService.userIdForEmail(currentUser.getUsername());
        return userProfileService.getProfile(userId);
    }

    @PutMapping("/me")
    public UserProfileResponse updateMyProfile(@AuthenticationPrincipal UserDetails currentUser,
                                               @Valid @RequestBody UserProfileRequest request) {
        Long userId = userProfileService.userIdForEmail(currentUser.getUsername());
        return userProfileService.updateProfile(userId, request);
    }
}
