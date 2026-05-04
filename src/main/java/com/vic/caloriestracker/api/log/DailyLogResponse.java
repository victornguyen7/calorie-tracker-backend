package com.vic.caloriestracker.api.log;

import com.vic.caloriestracker.entity.dailyLog;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DailyLogResponse {

    private Long id;
    private Long userId;
    private int date;
    private int totalCalories;
    private int totalProtein;

    public static DailyLogResponse from(dailyLog log) {
        DailyLogResponse response = new DailyLogResponse();
        response.setId(log.getId());
        response.setUserId(log.getUserId().getId());
        response.setDate(log.getDate());
        response.setTotalCalories(log.getTotalCalories());
        response.setTotalProtein(log.getTotalProtein());
        return response;
    }

}
