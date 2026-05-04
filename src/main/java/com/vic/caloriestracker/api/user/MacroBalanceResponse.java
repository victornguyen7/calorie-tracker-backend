package com.vic.caloriestracker.api.user;

public class MacroBalanceResponse {

    private int consumedGrams;
    private int targetGrams;
    private int remainingGrams;

    public MacroBalanceResponse(int consumedGrams, int targetGrams) {
        this.consumedGrams = consumedGrams;
        this.targetGrams = targetGrams;
        this.remainingGrams = Math.max(targetGrams - consumedGrams, 0);
    }

    public int getConsumedGrams() {
        return consumedGrams;
    }

    public void setConsumedGrams(int consumedGrams) {
        this.consumedGrams = consumedGrams;
    }

    public int getTargetGrams() {
        return targetGrams;
    }

    public void setTargetGrams(int targetGrams) {
        this.targetGrams = targetGrams;
    }

    public int getRemainingGrams() {
        return remainingGrams;
    }

    public void setRemainingGrams(int remainingGrams) {
        this.remainingGrams = remainingGrams;
    }
}
