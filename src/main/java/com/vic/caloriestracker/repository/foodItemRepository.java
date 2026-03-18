package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.foodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface foodItemRepository extends JpaRepository<foodItem,Long> {
        Optional<foodItem> findByName(String name);

        Optional<foodItem> findById(Long id);
}
