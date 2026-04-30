package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.foodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface foodItemRepository extends JpaRepository<foodItem,Long> {
    List<foodItem> findByNameContainingIgnoreCase(String name);
}
