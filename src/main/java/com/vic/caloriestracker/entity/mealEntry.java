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
}
