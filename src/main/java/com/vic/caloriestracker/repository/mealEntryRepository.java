package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.mealEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface mealEntryRepository extends JpaRepository<mealEntry,Long> {
    mealEntry findById(long id);

    mealEntry findByUserId(long id);
}
