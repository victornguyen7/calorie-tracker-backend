package com.vic.caloriestracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import com.vic.caloriestracker.entity.user;
import com.vic.caloriestracker.entity.foodItem;

@Entity
public class mealEntry {
    @Column(nullable = false, unique = true)
    private @Id
    @GeneratedValue Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private user userId;

    @ManyToOne
    @JoinColumn(name = "foodItemId", nullable = false)
    private foodItem foodItem;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String mealType;

    @Column(nullable = false)
    private int loggedAt;

    public mealEntry() {}

    public mealEntry(user userId, foodItem foodItemId, int quantity, String mealType, int loggedAt) {
        this.userId = userId;
        this.foodItem = foodItemId;
        this.quantity = quantity;
        this.mealType = mealType;
        this.loggedAt = loggedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public user getUserId() {
        return userId;
    }

    public void setUserId(user userId) {
        this.userId = userId;
    }

    public foodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(foodItem foodItem) {
        this.foodItem = foodItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public int getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(int loggedAt) {
        this.loggedAt = loggedAt;
    }
}
