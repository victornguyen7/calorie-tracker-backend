package com.vic.caloriestracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table (name = "users")
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
    private LocalDateTime createdAt;

    public user(){}

    public user(String name, String email, String passwordHash, int caloriesGoal, LocalDateTime createdAt) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.caloriesGoal = caloriesGoal;
        this.createdAt = createdAt;
    }

}
