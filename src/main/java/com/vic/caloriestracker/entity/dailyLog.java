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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public int getTotalProtein() {
        return totalProtein;
    }

    public void setTotalProtein(int totalProtein) {
        this.totalProtein = totalProtein;
    }
}
