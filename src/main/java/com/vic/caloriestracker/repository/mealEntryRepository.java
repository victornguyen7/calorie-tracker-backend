package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.mealEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface mealEntryRepository extends JpaRepository<mealEntry,Long> {
    List<mealEntry> findByUserId_Id(Long userId);

    List<mealEntry> findByUserId_IdAndLoggedAt(Long userId, int loggedAt);
}
