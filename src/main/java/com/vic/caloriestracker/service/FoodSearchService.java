package com.vic.caloriestracker.service;

import com.vic.caloriestracker.entity.foodItem;
import com.vic.caloriestracker.repository.foodItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FoodSearchService {

    private static final int CALORIES_NUTRIENT_ID = 1008;
    private static final int PROTEIN_NUTRIENT_ID = 1003;
    private static final int FAT_NUTRIENT_ID = 1004;
    private static final int CARBS_NUTRIENT_ID = 1005;

    private final RestTemplate restTemplate;
    private final foodItemRepository foodItemRepository;
    private final String apiKey;
    private final String searchUrl;
    private final int pageSize;

    public FoodSearchService(RestTemplate restTemplate,
                             foodItemRepository foodItemRepository,
                             @Value("${usda.api.key:DEMO_KEY}") String apiKey,
                             @Value("${usda.api.search-url:https://api.nal.usda.gov/fdc/v1/foods/search}") String searchUrl,
                             @Value("${usda.api.page-size:10}") int pageSize) {
        this.restTemplate = restTemplate;
        this.foodItemRepository = foodItemRepository;
        this.apiKey = apiKey;
        this.searchUrl = searchUrl;
        this.pageSize = pageSize;
    }

    public List<foodItem> search(String query) {
        try {
            List<foodItem> usdaFoods = searchUsda(query);
            if (!usdaFoods.isEmpty()) {
                return usdaFoods;
            }
        } catch (RestClientException ex) {
            // Fall back to local foods when USDA is unavailable or the key is not configured.
        }

        return foodItemRepository.findByNameContainingIgnoreCase(query);
    }

    private List<foodItem> searchUsda(String query) {
        String uri = UriComponentsBuilder.fromUriString(searchUrl)
                .queryParam("api_key", apiKey)
                .queryParam("query", query)
                .queryParam("pageSize", pageSize)
                .build()
                .toUriString();

        Map<?, ?> response = restTemplate.getForObject(uri, Map.class);
        if (response == null || !(response.get("foods") instanceof List<?> foods)) {
            return List.of();
        }

        List<foodItem> results = new ArrayList<>();
        for (Object food : foods) {
            if (food instanceof Map<?, ?> foodMap) {
                results.add(mapUsdaFood(foodMap));
            }
        }
        return results;
    }

    private foodItem mapUsdaFood(Map<?, ?> foodMap) {
        foodItem item = new foodItem();
        item.setName(stringValue(foodMap.get("description"), "Unknown food"));
        item.setCalories(nutrientValue(foodMap, CALORIES_NUTRIENT_ID));
        item.setProtein(nutrientValue(foodMap, PROTEIN_NUTRIENT_ID));
        item.setCarbs(nutrientValue(foodMap, CARBS_NUTRIENT_ID));
        item.setFats(nutrientValue(foodMap, FAT_NUTRIENT_ID));
        item.setServingSize(servingSize(foodMap));
        return item;
    }

    private int nutrientValue(Map<?, ?> foodMap, int nutrientId) {
        Object nutrients = foodMap.get("foodNutrients");
        if (!(nutrients instanceof List<?> nutrientList)) {
            return 0;
        }

        for (Object nutrient : nutrientList) {
            if (nutrient instanceof Map<?, ?> nutrientMap
                    && matchesNutrientId(nutrientMap, nutrientId)
                    && nutrientMap.get("value") instanceof Number value) {
                return Math.toIntExact(Math.round(value.doubleValue()));
            }
        }
        return 0;
    }

    private boolean matchesNutrientId(Map<?, ?> nutrientMap, int nutrientId) {
        Object id = nutrientMap.get("nutrientId");
        return id instanceof Number number && number.intValue() == nutrientId;
    }

    private String servingSize(Map<?, ?> foodMap) {
        Object servingSize = foodMap.get("servingSize");
        Object servingSizeUnit = foodMap.get("servingSizeUnit");
        if (servingSize != null && servingSizeUnit != null) {
            return servingSize + " " + servingSizeUnit;
        }
        return "100g";
    }

    private String stringValue(Object value, String fallback) {
        if (value instanceof String text && !text.isBlank()) {
            return text;
        }
        return fallback;
    }
}
