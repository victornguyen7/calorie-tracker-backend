package com.vic.caloriestracker.api.umass;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SaveUmassFoodRequest {

    @NotBlank(message = "Food name is required")
    private String name;

    @PositiveOrZero(message = "Calories must be zero or greater")
    private int calories;

    @PositiveOrZero(message = "Protein must be zero or greater")
    private int protein;

    @PositiveOrZero(message = "Carbs must be zero or greater")
    private int carbs;

    @PositiveOrZero(message = "Fats must be zero or greater")
    private int fats;

    @NotBlank(message = "Serving size is required")
    private String servingSize;

}
