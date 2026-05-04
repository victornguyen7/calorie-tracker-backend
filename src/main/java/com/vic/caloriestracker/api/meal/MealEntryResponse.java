package com.vic.caloriestracker.api.meal;

import com.vic.caloriestracker.entity.mealEntry;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MealEntryResponse {

    private Long id;
    private Long userId;
    private Long foodId;
    private String foodName;
    private int quantity;
    private String mealType;
    private int loggedAt;
    private int calories;
    private int protein;

    public static MealEntryResponse from(mealEntry entry) {
        MealEntryResponse response = new MealEntryResponse();
        response.setId(entry.getId());
        response.setUserId(entry.getUserId().getId());
        response.setFoodId(entry.getFoodItem().getId());
        response.setFoodName(entry.getFoodItem().getName());
        response.setQuantity(entry.getQuantity());
        response.setMealType(entry.getMealType());
        response.setLoggedAt(entry.getLoggedAt());
        response.setCalories(entry.getFoodItem().getCalories() * entry.getQuantity());
        response.setProtein(entry.getFoodItem().getProtein() * entry.getQuantity());
        return response;
    }

}
