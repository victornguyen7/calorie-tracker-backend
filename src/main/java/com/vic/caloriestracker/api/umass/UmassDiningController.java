package com.vic.caloriestracker.api.umass;

import com.vic.caloriestracker.entity.foodItem;
import com.vic.caloriestracker.service.UmassDiningService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/umass")
@Validated
public class UmassDiningController {

    private final UmassDiningService umassDiningService;

    public UmassDiningController(UmassDiningService umassDiningService) {
        this.umassDiningService = umassDiningService;
    }

    @GetMapping("/menu")
    public List<UmassMenuItemResponse> getMenu(
            @RequestParam @NotBlank(message = "Dining hall is required") String hall,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return umassDiningService.getMenu(hall, date);
    }

    @GetMapping("/menu/search")
    public List<UmassMenuItemResponse> searchMenu(
            @RequestParam @NotBlank(message = "Dining hall is required") String hall,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @NotBlank(message = "Search query is required") String q) {
        return umassDiningService.searchMenu(hall, date, q);
    }

    @PostMapping("/menu/save")
    @ResponseStatus(HttpStatus.CREATED)
    public foodItem saveMenuItem(@Valid @RequestBody SaveUmassFoodRequest request) {
        return umassDiningService.saveMenuItem(request);
    }
}
