package com.vic.caloriestracker.food;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class foodItem {
    @Column(nullable = false, unique = true)
    private @Id
    @GeneratedValue Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int calories;

    @Column(nullable = false)
    private int protein;

    @Column(nullable = false)
    private int carbs;

    @Column(nullable = false)
    private int fats;

    @Column(nullable = false)
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
