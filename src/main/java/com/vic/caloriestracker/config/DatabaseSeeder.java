package com.vic.caloriestracker.config;

import com.vic.caloriestracker.entity.foodItem;
import com.vic.caloriestracker.repository.foodItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final foodItemRepository foodItemRepository;

    public DatabaseSeeder(foodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Only seed if the table is empty — prevents duplicate data on every restart
        if (foodItemRepository.count() > 0) {
            System.out.println("Database already has food items — skipping seeder.");
            return;
        }

        System.out.println("Seeding database with test food items...");

        List<foodItem> foods = List.of(
                createFood("Chicken Breast", 165, 31, 0, 4, "100g"),
                createFood("Brown Rice", 216, 5, 45, 2, "1 cup cooked"),
                createFood("Egg", 78, 6, 1, 5, "1 large egg"),
                createFood("Banana", 89, 1, 23, 0, "1 medium"),
                createFood("Broccoli", 55, 4, 11, 1, "1 cup"),
                createFood("Salmon", 208, 20, 0, 13, "100g"),
                createFood("Greek Yogurt", 100, 17, 6, 0, "170g"),
                createFood("Oatmeal", 154, 5, 27, 3, "1 cup cooked"),
                createFood("Almonds", 164, 6, 6, 14, "28g / 1oz"),
                createFood("Sweet Potato", 103, 2, 24, 0, "1 medium"),
                createFood("Whole Milk", 149, 8, 12, 8, "1 cup"),
                createFood("Avocado", 160, 2, 9, 15, "half"),
                createFood("White Rice", 206, 4, 45, 0, "1 cup cooked"),
                createFood("Tuna (canned)", 109, 25, 0, 1, "100g"),
                createFood("Apple", 95, 0, 25, 0, "1 medium")
        );

        foodItemRepository.saveAll(foods);
        System.out.println("Seeded " + foods.size() + " food items successfully.");
    }

    // Helper method to keep the code above clean and readable
    private foodItem createFood(String name, int calories, int protein,
                                int carbs, int fats, String servingSize) {
        foodItem item = new foodItem();
        item.setName(name);
        item.setCalories(calories);
        item.setProtein(protein);
        item.setCarbs(carbs);
        item.setFats(fats);
        item.setServingSize(servingSize);
        return item;
    }
}