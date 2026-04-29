package com.vic.caloriestracker.service;

import com.vic.caloriestracker.entity.foodItem;
import com.vic.caloriestracker.repository.foodItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {

    private final foodItemRepository foodItemRepository;

    public FoodItemService(foodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    public List<foodItem> findAll() {
        return foodItemRepository.findAll();
    }

    public List<foodItem> search(String query) {
        return foodItemRepository.findByNameContainingIgnoreCase(query);
    }

    public foodItem findById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
    }

    public foodItem save(foodItem item) {
        return foodItemRepository.save(item);
    }
}
