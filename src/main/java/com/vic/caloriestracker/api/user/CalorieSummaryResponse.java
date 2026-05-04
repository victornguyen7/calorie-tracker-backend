package com.vic.caloriestracker.api.user;

public class CalorieSummaryResponse {

    private Long userId;
    private int date;
    private int goalCalories;
    private int eatenCalories;
    private int remainingCalories;
    private MacroBalanceSummary macros;

    public CalorieSummaryResponse(Long userId,
                                  int date,
                                  int goalCalories,
                                  int eatenCalories,
                                  MacroBalanceSummary macros) {
        this.userId = userId;
        this.date = date;
        this.goalCalories = goalCalories;
        this.eatenCalories = eatenCalories;
        this.remainingCalories = Math.max(goalCalories - eatenCalories, 0);
        this.macros = macros;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getGoalCalories() {
        return goalCalories;
    }

    public void setGoalCalories(int goalCalories) {
        this.goalCalories = goalCalories;
    }

    public int getEatenCalories() {
        return eatenCalories;
    }

    public void setEatenCalories(int eatenCalories) {
        this.eatenCalories = eatenCalories;
    }

    public int getRemainingCalories() {
        return remainingCalories;
    }

    public void setRemainingCalories(int remainingCalories) {
        this.remainingCalories = remainingCalories;
    }

    public MacroBalanceSummary getMacros() {
        return macros;
    }

    public void setMacros(MacroBalanceSummary macros) {
        this.macros = macros;
    }
}
