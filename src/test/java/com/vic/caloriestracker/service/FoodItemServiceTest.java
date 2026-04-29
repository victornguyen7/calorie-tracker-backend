package com.vic.caloriestracker.service;

import com.vic.caloriestracker.entity.foodItem;
import com.vic.caloriestracker.repository.foodItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoodItemServiceTest {

    @Mock
    private foodItemRepository foodItemRepository;

    @InjectMocks
    private FoodItemService foodItemService;

    @Test
    void searchReturnsMatchingFoods() {
        foodItem chicken = createFood("Chicken Breast", 165, 31, 0, 4, "100g");
        when(foodItemRepository.findByNameContainingIgnoreCase("chicken"))
                .thenReturn(List.of(chicken));

        List<foodItem> result = foodItemService.search("chicken");

        assertThat(result).containsExactly(chicken);
        verify(foodItemRepository).findByNameContainingIgnoreCase("chicken");
    }

    @Test
    void findByIdReturnsFoodWhenItExists() {
        foodItem banana = createFood("Banana", 89, 1, 23, 0, "1 medium");
        when(foodItemRepository.findById(1L)).thenReturn(Optional.of(banana));

        foodItem result = foodItemService.findById(1L);

        assertThat(result).isEqualTo(banana);
        verify(foodItemRepository).findById(1L);
    }

    @Test
    void findByIdThrowsWhenFoodDoesNotExist() {
        when(foodItemRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> foodItemService.findById(99L));

        assertThat(exception.getMessage()).isEqualTo("Food not found with id: 99");
    }

    @Test
    void savePersistsFoodItem() {
        foodItem oatmeal = createFood("Oatmeal", 154, 5, 27, 3, "1 cup cooked");
        when(foodItemRepository.save(oatmeal)).thenReturn(oatmeal);

        foodItem result = foodItemService.save(oatmeal);

        assertThat(result).isEqualTo(oatmeal);
        verify(foodItemRepository).save(oatmeal);
    }

    private foodItem createFood(String name, int calories, int protein, int carbs, int fats, String servingSize) {
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
