package com.vic.caloriestracker.service;

import com.vic.caloriestracker.api.user.CalorieSummaryResponse;
import com.vic.caloriestracker.api.user.MacroBalanceResponse;
import com.vic.caloriestracker.api.user.MacroBalanceSummary;
import com.vic.caloriestracker.entity.dailyLog;
import com.vic.caloriestracker.entity.mealEntry;
import com.vic.caloriestracker.entity.user;
import com.vic.caloriestracker.repository.mealEntryRepository;
import com.vic.caloriestracker.repository.userRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class CalorieCalculatorService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
    private static final double PROTEIN_TARGET_PERCENT = 0.30;
    private static final double CARB_TARGET_PERCENT = 0.40;
    private static final double FAT_TARGET_PERCENT = 0.30;

    private final userRepository userRepository;
    private final mealEntryRepository mealEntryRepository;
    private final DailyLogService dailyLogService;

    public CalorieCalculatorService(userRepository userRepository,
                                    mealEntryRepository mealEntryRepository,
                                    DailyLogService dailyLogService) {
        this.userRepository = userRepository;
        this.mealEntryRepository = mealEntryRepository;
        this.dailyLogService = dailyLogService;
    }

    public int calculateTDEE(double weight, double height, int age, String activityLevel) {
        double bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        return Math.toIntExact(Math.round(bmr * activityMultiplier(activityLevel)));
    }

    public int getRemainingCalories(Long userId, int date) {
        user foundUser = findUser(userId);
        dailyLog log = dailyLogService.getDailyLog(userId, date);
        return Math.max(foundUser.getCaloriesGoal() - log.getTotalCalories(), 0);
    }

    public MacroBalanceSummary getMacroBalance(Long userId, int date) {
        user foundUser = findUser(userId);
        List<mealEntry> meals = mealEntryRepository.findByUserId_IdAndLoggedAt(userId, date);
        MacroTotals consumed = macroTotals(meals);
        MacroTargets targets = macroTargets(foundUser.getCaloriesGoal());

        return new MacroBalanceSummary(
                new MacroBalanceResponse(consumed.protein(), targets.protein()),
                new MacroBalanceResponse(consumed.carbs(), targets.carbs()),
                new MacroBalanceResponse(consumed.fats(), targets.fats())
        );
    }

    public CalorieSummaryResponse getCalorieSummary(Long userId, Integer date) {
        int summaryDate = date != null ? date : todayAsInt();
        user foundUser = findUser(userId);
        dailyLog log = dailyLogService.getDailyLog(userId, summaryDate);
        MacroBalanceSummary macros = getMacroBalance(userId, summaryDate);

        return new CalorieSummaryResponse(
                userId,
                summaryDate,
                foundUser.getCaloriesGoal(),
                log.getTotalCalories(),
                macros
        );
    }

    private user findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with id: " + userId));
    }

    private double activityMultiplier(String activityLevel) {
        return switch (activityLevel.toLowerCase(Locale.ROOT).replace(" ", "_")) {
            case "sedentary" -> 1.2;
            case "light", "lightly_active" -> 1.375;
            case "moderate", "moderately_active" -> 1.55;
            case "active", "very_active" -> 1.725;
            case "extra_active", "athlete" -> 1.9;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unsupported activity level: " + activityLevel);
        };
    }

    private MacroTotals macroTotals(List<mealEntry> meals) {
        int protein = meals.stream()
                .mapToInt(meal -> meal.getFoodItem().getProtein() * meal.getQuantity())
                .sum();
        int carbs = meals.stream()
                .mapToInt(meal -> meal.getFoodItem().getCarbs() * meal.getQuantity())
                .sum();
        int fats = meals.stream()
                .mapToInt(meal -> meal.getFoodItem().getFats() * meal.getQuantity())
                .sum();
        return new MacroTotals(protein, carbs, fats);
    }

    private MacroTargets macroTargets(int calorieGoal) {
        int protein = Math.toIntExact(Math.round((calorieGoal * PROTEIN_TARGET_PERCENT) / 4));
        int carbs = Math.toIntExact(Math.round((calorieGoal * CARB_TARGET_PERCENT) / 4));
        int fats = Math.toIntExact(Math.round((calorieGoal * FAT_TARGET_PERCENT) / 9));
        return new MacroTargets(protein, carbs, fats);
    }

    private int todayAsInt() {
        return Integer.parseInt(LocalDate.now().format(DATE_FORMATTER));
    }

    private record MacroTotals(int protein, int carbs, int fats) {
    }

    private record MacroTargets(int protein, int carbs, int fats) {
    }
}
