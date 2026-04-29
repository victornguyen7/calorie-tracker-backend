package com.vic.caloriestracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public int getProtein() {
        return protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getFats() {
        return fats;
    }

    public String getServingSize() {
        return servingSize;
    }
}
