package com.vic.caloriestracker.api.food;

import com.vic.caloriestracker.entity.foodItem;
import com.vic.caloriestracker.service.FoodItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@Validated
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping
    public List<foodItem> getAllFoods() {
        return foodItemService.findAll();
    }

    @GetMapping("/search")
    public List<foodItem> searchFoods(@RequestParam @NotBlank(message = "Search query is required") String q) {
        return foodItemService.search(q);
    }

    @GetMapping("/{id}")
    public foodItem getFoodById(@PathVariable Long id) {
        return foodItemService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public foodItem createFood(@Valid @RequestBody foodItem item) {
        return foodItemService.save(item);
    }
}
