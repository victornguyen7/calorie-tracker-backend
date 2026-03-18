package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.mealEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface mealEntryRepository extends JpaRepository<mealEntry,Long> {
    Optional<mealEntry> findById(Long id);

    // find all meal entries for a given user id
    List<mealEntry> findByUserId(Long userId);
}
