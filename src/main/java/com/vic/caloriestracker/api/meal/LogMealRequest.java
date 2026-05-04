package com.vic.caloriestracker.api.meal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogMealRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Food id is required")
    private Long foodId;

    @Positive(message = "Quantity must be greater than zero")
    private int quantity;

}
