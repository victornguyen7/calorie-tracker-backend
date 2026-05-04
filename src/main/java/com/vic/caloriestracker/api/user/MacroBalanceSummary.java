package com.vic.caloriestracker.api.user;

public class MacroBalanceSummary {

    private MacroBalanceResponse protein;
    private MacroBalanceResponse carbs;
    private MacroBalanceResponse fats;

    public MacroBalanceSummary(MacroBalanceResponse protein,
                               MacroBalanceResponse carbs,
                               MacroBalanceResponse fats) {
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }

    public MacroBalanceResponse getProtein() {
        return protein;
    }

    public void setProtein(MacroBalanceResponse protein) {
        this.protein = protein;
    }

    public MacroBalanceResponse getCarbs() {
        return carbs;
    }

    public void setCarbs(MacroBalanceResponse carbs) {
        this.carbs = carbs;
    }

    public MacroBalanceResponse getFats() {
        return fats;
    }

    public void setFats(MacroBalanceResponse fats) {
        this.fats = fats;
    }
}
