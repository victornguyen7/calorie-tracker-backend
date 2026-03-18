package com.vic.caloriestracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import com.vic.caloriestracker.entity.user;

@Entity
public class dailyLog {
    @Column(nullable = false, unique = true)
    private @Id
    @GeneratedValue Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private user userId;

    @Column(nullable = false)
    private int date; // Format: YYYYMMDD

    @Column(nullable = false)
    private int totalCalories;

    @Column(nullable = false)
    private int totalProtein;

    public dailyLog(){}

    public dailyLog(user userId, int date, int totalCalories, int totalProtein) {
        this.userId = userId;
        this.date = date;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
    }
}
