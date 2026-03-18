package com.vic.caloriestracker.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class user {
    @Column(unique = true, nullable = false)
    private @Id @GeneratedValue Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int caloriesGoal;

    @Column(nullable = false)
    private int createdAt;

    public user(){}

    public user(String name, String email, String passwordHash, int caloriesGoal, int createdAt) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.caloriesGoal = caloriesGoal;
        this.createdAt = createdAt;
    }
}
