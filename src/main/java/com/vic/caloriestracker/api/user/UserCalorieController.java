package com.vic.caloriestracker.api.user;

import com.vic.caloriestracker.service.CalorieCalculatorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserCalorieController {

    private final CalorieCalculatorService calorieCalculatorService;

    public UserCalorieController(CalorieCalculatorService calorieCalculatorService) {
        this.calorieCalculatorService = calorieCalculatorService;
    }

    @GetMapping("/{id}/calorie-summary")
    public CalorieSummaryResponse getCalorieSummary(@PathVariable Long id,
                                                    @RequestParam(required = false) Integer date) {
        return calorieCalculatorService.getCalorieSummary(id, date);
    }
}
