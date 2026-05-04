package com.vic.caloriestracker.service;

import com.vic.caloriestracker.entity.dailyLog;
import com.vic.caloriestracker.entity.foodItem;
import com.vic.caloriestracker.entity.mealEntry;
import com.vic.caloriestracker.entity.user;
import com.vic.caloriestracker.repository.mealEntryRepository;
import com.vic.caloriestracker.repository.userRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalorieCalculatorServiceTest {

    @Mock
    private userRepository userRepository;

    @Mock
    private mealEntryRepository mealEntryRepository;

    @Mock
    private DailyLogService dailyLogService;

    @Test
    void calculateTDEEUsesMifflinStJeorAndActivityMultiplier() {
        CalorieCalculatorService service = newService();

        int result = service.calculateTDEE(70, 175, 25, "moderate");

        assertThat(result).isEqualTo(2594);
    }

    @Test
    void getCalorieSummaryReturnsGoalEatenRemainingAndMacros() {
        CalorieCalculatorService service = newService();
        user demoUser = new user("Demo User", "demo@example.com", "hash", 2000, LocalDateTime.now());
        demoUser.setId(1L);
        foodItem chicken = food("Chicken Breast", 165, 31, 0, 4);
        mealEntry meal = new mealEntry(demoUser, chicken, 2, "meal", 20260504);
        dailyLog log = new dailyLog(demoUser, 20260504, 330, 62);

        when(userRepository.findById(1L)).thenReturn(Optional.of(demoUser));
        when(dailyLogService.getDailyLog(1L, 20260504)).thenReturn(log);
        when(mealEntryRepository.findByUserId_IdAndLoggedAt(1L, 20260504)).thenReturn(List.of(meal));

        var result = service.getCalorieSummary(1L, 20260504);

        assertThat(result.getGoalCalories()).isEqualTo(2000);
        assertThat(result.getEatenCalories()).isEqualTo(330);
        assertThat(result.getRemainingCalories()).isEqualTo(1670);
        assertThat(result.getMacros().getProtein().getConsumedGrams()).isEqualTo(62);
        assertThat(result.getMacros().getProtein().getTargetGrams()).isEqualTo(150);
        assertThat(result.getMacros().getCarbs().getTargetGrams()).isEqualTo(200);
        assertThat(result.getMacros().getFats().getTargetGrams()).isEqualTo(67);
    }

    private CalorieCalculatorService newService() {
        return new CalorieCalculatorService(userRepository, mealEntryRepository, dailyLogService);
    }

    private foodItem food(String name, int calories, int protein, int carbs, int fats) {
        foodItem item = new foodItem();
        item.setName(name);
        item.setCalories(calories);
        item.setProtein(protein);
        item.setCarbs(carbs);
        item.setFats(fats);
        item.setServingSize("100g");
        return item;
    }
}
