package com.vic.caloriestracker.service;

import com.vic.caloriestracker.api.umass.SaveUmassFoodRequest;
import com.vic.caloriestracker.api.umass.UmassMenuItemResponse;
import com.vic.caloriestracker.entity.foodItem;
import com.vic.caloriestracker.repository.foodItemRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class UmassDiningService {

    private static final DateTimeFormatter UMASS_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yy");

    private final RestTemplate restTemplate;
    private final foodItemRepository foodItemRepository;
    private final String menuUrl;

    public UmassDiningService(RestTemplate restTemplate,
                              foodItemRepository foodItemRepository,
                              @Value("${umass.api.menu-url:https://umassdining.com/foodpro-menu-ajax}") String menuUrl) {
        this.restTemplate = restTemplate;
        this.foodItemRepository = foodItemRepository;
        this.menuUrl = menuUrl;
    }

    public List<UmassMenuItemResponse> getMenu(String hall, LocalDate date) {
        String diningHall = normalizeDiningHall(hall);
        String uri = UriComponentsBuilder.fromUriString(menuUrl)
                .queryParam("tid", locationId(diningHall))
                .queryParam("date", date.format(UMASS_DATE_FORMATTER))
                .build()
                .toUriString();

        Map<?, ?> response = restTemplate.getForObject(uri, Map.class);
        if (response == null || response.isEmpty()) {
            return List.of();
        }

        List<UmassMenuItemResponse> results = new ArrayList<>();
        for (Map.Entry<?, ?> mealEntry : response.entrySet()) {
            if (mealEntry.getValue() instanceof Map<?, ?> categories) {
                parseCategories(results, diningHall, String.valueOf(mealEntry.getKey()), categories);
            }
        }
        return results;
    }

    public List<UmassMenuItemResponse> searchMenu(String hall, LocalDate date, String query) {
        String normalizedQuery = query.toLowerCase(Locale.ROOT);
        return getMenu(hall, date).stream()
                .filter(item -> item.getName().toLowerCase(Locale.ROOT).contains(normalizedQuery))
                .toList();
    }

    public foodItem saveMenuItem(SaveUmassFoodRequest request) {
        return foodItemRepository.findByNameIgnoreCase(request.getName())
                .orElseGet(() -> foodItemRepository.save(toFoodItem(request)));
    }

    private void parseCategories(List<UmassMenuItemResponse> results,
                                 String diningHall,
                                 String meal,
                                 Map<?, ?> categories) {
        for (Map.Entry<?, ?> categoryEntry : categories.entrySet()) {
            String category = String.valueOf(categoryEntry.getKey());
            String html = String.valueOf(categoryEntry.getValue());
            Document document = Jsoup.parse(html);
            for (Element foodLink : document.select("a[data-dish-name]")) {
                results.add(mapFoodLink(foodLink, diningHall, meal, category));
            }
        }
    }

    private UmassMenuItemResponse mapFoodLink(Element foodLink,
                                              String diningHall,
                                              String meal,
                                              String category) {
        UmassMenuItemResponse item = new UmassMenuItemResponse();
        item.setName(foodLink.attr("data-dish-name"));
        item.setCalories(numberValue(foodLink.attr("data-calories")));
        item.setProtein(numberValue(foodLink.attr("data-protein")));
        item.setCarbs(numberValue(foodLink.attr("data-total-carb")));
        item.setFats(numberValue(foodLink.attr("data-total-fat")));
        item.setServingSize(defaultIfBlank(foodLink.attr("data-serving-size"), "1 serving"));
        item.setDiningHall(diningHall);
        item.setMeal(meal);
        item.setCategory(category);
        item.setIngredients(foodLink.attr("data-ingredient-list"));
        item.setAllergens(splitCsv(foodLink.attr("data-allergens")));
        item.setLabels(splitCsv(foodLink.attr("data-clean-diet-str")));
        return item;
    }

    private foodItem toFoodItem(SaveUmassFoodRequest request) {
        foodItem item = new foodItem();
        item.setName(request.getName());
        item.setCalories(request.getCalories());
        item.setProtein(request.getProtein());
        item.setCarbs(request.getCarbs());
        item.setFats(request.getFats());
        item.setServingSize(request.getServingSize());
        return item;
    }

    private String normalizeDiningHall(String hall) {
        String normalized = hall.toLowerCase(Locale.ROOT)
                .replace("dining commons", "")
                .replace("dining common", "")
                .trim();
        return switch (normalized) {
            case "worcester", "woo" -> "Worcester Dining Commons";
            case "frank", "franklin" -> "Franklin Dining Commons";
            case "hampshire" -> "Hampshire Dining Commons";
            case "berkshire", "berk" -> "Berkshire Dining Commons";
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unsupported UMass dining hall: " + hall);
        };
    }

    private int locationId(String diningHall) {
        return switch (diningHall) {
            case "Worcester Dining Commons" -> 1;
            case "Franklin Dining Commons" -> 2;
            case "Hampshire Dining Commons" -> 3;
            case "Berkshire Dining Commons" -> 4;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unsupported UMass dining hall: " + diningHall);
        };
    }

    private int numberValue(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }

        String numeric = text.replaceAll("[^0-9.]", "");
        if (numeric.isBlank()) {
            return 0;
        }
        return Math.toIntExact(Math.round(Double.parseDouble(numeric)));
    }

    private List<String> splitCsv(String text) {
        if (text == null || text.isBlank() || "None".equalsIgnoreCase(text.trim())) {
            return List.of();
        }

        return Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toList();
    }

    private String defaultIfBlank(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value;
    }
}
