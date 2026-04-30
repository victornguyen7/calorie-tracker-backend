package com.vic.caloriestracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class foodItem {
    @Column(nullable = false, unique = true, name = "id")
    private @Id
    @GeneratedValue Long id;

    @Column(nullable = false, unique = true, name = "name")
    @NotBlank(message = "Food name is required")
    private String name;

    @Column(nullable = false, name = "calories")
    @PositiveOrZero(message = "Calories must be zero or greater")
    private int calories;

    @Column(nullable = false, name = "protein")
    @PositiveOrZero(message = "Protein must be zero or greater")
    private int protein;

    @Column(nullable = false, name = "carbs")
    @PositiveOrZero(message = "Carbs must be zero or greater")
    private int carbs;

    @Column(nullable = false, name = "fats")
    @PositiveOrZero(message = "Fats must be zero or greater")
    private int fats;

    @Column(nullable = false, name = "serving_size")
    @NotBlank(message = "Serving size is required")
    private String servingSize;

    public foodItem(){}

    public foodItem(String name, int calories, int protein, int carbs, int fats) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }

}
