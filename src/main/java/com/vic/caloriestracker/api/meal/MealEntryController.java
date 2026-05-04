package com.vic.caloriestracker.api.meal;

import com.vic.caloriestracker.service.MealEntryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
@Validated
public class MealEntryController {

    private final MealEntryService mealEntryService;

    public MealEntryController(MealEntryService mealEntryService) {
        this.mealEntryService = mealEntryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MealEntryResponse logMeal(@Valid @RequestBody LogMealRequest request) {
        return MealEntryResponse.from(mealEntryService.logMeal(request.getUserId(), request.getFoodId(), request.getQuantity()));
    }

    @GetMapping("/today")
    public List<MealEntryResponse> getTodayMeals(@RequestParam Long userId) {
        return mealEntryService.getTodayMeals(userId).stream()
                .map(MealEntryResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeal(@PathVariable Long id) {
        mealEntryService.deleteMeal(id);
    }
}
