package com.vic.caloriestracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class foodItem {
    @Column(nullable = false, unique = true, name = "id")
    private @Id
    @GeneratedValue Long id;

    @Column(nullable = false, unique = true, name = "name")
    private String name;

    @Column(nullable = false, name = "calories")
    private int calories;

    @Column(nullable = false, name = "protein")
    private int protein;

    @Column(nullable = false, name = "carbs")
    private int carbs;

    @Column(nullable = false, name = "fats")
    private int fats;

    @Column(nullable = false, name = "serving_size")
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