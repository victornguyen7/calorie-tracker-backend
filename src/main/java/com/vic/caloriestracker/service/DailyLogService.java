package com.vic.caloriestracker.service;

import com.vic.caloriestracker.entity.dailyLog;
import com.vic.caloriestracker.entity.mealEntry;
import com.vic.caloriestracker.entity.user;
import com.vic.caloriestracker.repository.dailyLogRepository;
import com.vic.caloriestracker.repository.mealEntryRepository;
import com.vic.caloriestracker.repository.userRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DailyLogService {

    private final dailyLogRepository dailyLogRepository;
    private final mealEntryRepository mealEntryRepository;
    private final userRepository userRepository;

    public DailyLogService(dailyLogRepository dailyLogRepository,
                           mealEntryRepository mealEntryRepository,
                           userRepository userRepository) {
        this.dailyLogRepository = dailyLogRepository;
        this.mealEntryRepository = mealEntryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public dailyLog getDailyLog(Long userId, int date) {
        return dailyLogRepository.findByUserId_IdAndDate(userId, date)
                .orElseGet(() -> recalculateDailyTotals(userId, date));
    }

    @Transactional
    public dailyLog recalculateDailyTotals(Long userId, int date) {
        user foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with id: " + userId));
        List<mealEntry> meals = mealEntryRepository.findByUserId_IdAndLoggedAt(userId, date);

        int totalCalories = meals.stream()
                .mapToInt(meal -> meal.getFoodItem().getCalories() * meal.getQuantity())
                .sum();
        int totalProtein = meals.stream()
                .mapToInt(meal -> meal.getFoodItem().getProtein() * meal.getQuantity())
                .sum();

        dailyLog log = dailyLogRepository.findByUserId_IdAndDate(userId, date)
                .orElseGet(() -> new dailyLog(foundUser, date, 0, 0));
        log.setTotalCalories(totalCalories);
        log.setTotalProtein(totalProtein);
        return dailyLogRepository.save(log);
    }
}
