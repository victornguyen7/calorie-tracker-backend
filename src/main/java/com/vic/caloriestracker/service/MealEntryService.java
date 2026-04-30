package com.vic.caloriestracker.service;

import com.vic.caloriestracker.entity.foodItem;
import com.vic.caloriestracker.entity.mealEntry;
import com.vic.caloriestracker.entity.user;
import com.vic.caloriestracker.repository.foodItemRepository;
import com.vic.caloriestracker.repository.mealEntryRepository;
import com.vic.caloriestracker.repository.userRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MealEntryService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;

    private final mealEntryRepository mealEntryRepository;
    private final userRepository userRepository;
    private final foodItemRepository foodItemRepository;
    private final DailyLogService dailyLogService;

    public MealEntryService(mealEntryRepository mealEntryRepository,
                            userRepository userRepository,
                            foodItemRepository foodItemRepository,
                            DailyLogService dailyLogService) {
        this.mealEntryRepository = mealEntryRepository;
        this.userRepository = userRepository;
        this.foodItemRepository = foodItemRepository;
        this.dailyLogService = dailyLogService;
    }

    @Transactional
    public mealEntry logMeal(Long userId, Long foodId, int quantity) {
        user foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with id: " + userId));
        foodItem foundFood = foodItemRepository.findById(foodId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Food not found with id: " + foodId));

        mealEntry entry = new mealEntry(foundUser, foundFood, quantity, "meal", todayAsInt());
        mealEntry savedEntry = mealEntryRepository.save(entry);
        dailyLogService.recalculateDailyTotals(userId, savedEntry.getLoggedAt());
        return savedEntry;
    }

    public List<mealEntry> getDailyMeals(Long userId, int date) {
        return mealEntryRepository.findByUserId_IdAndLoggedAt(userId, date);
    }

    public List<mealEntry> getTodayMeals(Long userId) {
        return getDailyMeals(userId, todayAsInt());
    }

    @Transactional
    public void deleteMeal(Long id) {
        mealEntry entry = mealEntryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Meal not found with id: " + id));
        Long userId = entry.getUserId().getId();
        int date = entry.getLoggedAt();

        mealEntryRepository.delete(entry);
        dailyLogService.recalculateDailyTotals(userId, date);
    }

    private int todayAsInt() {
        return Integer.parseInt(LocalDate.now().format(DATE_FORMATTER));
    }
}
