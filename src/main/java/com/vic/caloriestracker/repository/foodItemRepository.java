package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.foodItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface foodItemRepository extends JpaRepository<foodItem,Long> {
        foodItem findByName(String name);

        foodItem findById(long id);
}
