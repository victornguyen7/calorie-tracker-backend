package com.vic.caloriestracker.api.log;

import com.vic.caloriestracker.service.DailyLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
@Validated
public class DailyLogController {

    private final DailyLogService dailyLogService;

    public DailyLogController(DailyLogService dailyLogService) {
        this.dailyLogService = dailyLogService;
    }

    @GetMapping("/{date}")
    public DailyLogResponse getDailyLog(@PathVariable int date, @RequestParam Long userId) {
        return DailyLogResponse.from(dailyLogService.getDailyLog(userId, date));
    }
}
