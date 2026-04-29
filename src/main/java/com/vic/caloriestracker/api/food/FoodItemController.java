package com.vic.caloriestracker.api.food;

import com.vic.caloriestracker.entity.foodItem;
import com.vic.caloriestracker.repository.foodItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodItemController {

    private final foodItemRepository foodItemRepository;

    public FoodItemController(foodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    // GET all food items
    @GetMapping
    public List<foodItem> getAllFoods() {
        return foodItemRepository.findAll();
    }

    // GET food items by search query
    @GetMapping("/search")
    public List<foodItem> searchFoods(@RequestParam String q) {
        return foodItemRepository.findByNameContainingIgnoreCase(q);
    }

    // GET single food item by id
    @GetMapping("/{id}")
    public foodItem getFoodById(@PathVariable Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
    }
}