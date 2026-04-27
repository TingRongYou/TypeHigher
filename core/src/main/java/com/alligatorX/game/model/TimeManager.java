package com.alligatorX.game.model;

public class TimeManager {

    private float remainingTime; // Remaining time for a word, return every frame
    private float decrementAmount; // How much to decrement to increase game difficulty
    private float minTimeFloor; // How low the time limit should be (avoid being impossible)
    private float timePerChar; // Dynamic difficulty, time to type per character

    public TimeManager() {
        this.remainingTime = 0f;
        this.decrementAmount = 0.1f;
        this.minTimeFloor = 0.5f;
        this.timePerChar = 1.5f;

    }

    public float getRemainingTime() {
        return this.remainingTime;
    }

    public float getTimePerChar() {
        return this.timePerChar;
    }

    public float getDecrementAmount() {
        return this.decrementAmount;
    }

    public float getMinTimeFloor() {
        return this.minTimeFloor;
    }

    // Call every single frame to subtract time
    public void updateTimer(float deltaTime) {
        this.remainingTime -= deltaTime;
    }

    public boolean isTimeUp() {
        return this.remainingTime <= 0;
    }

    public void shrinkTimeLimit() {
        // Ensure even if that the result of timePerChar - decrementAmount is 0.45f, it converts to max, which is minTimeFloor
        this.timePerChar = Math.max(this.minTimeFloor, this.timePerChar - this.decrementAmount);
    }

    public void resetTimerForNewWord(int wordLength) {
        this.remainingTime = wordLength * this.timePerChar;
    }

}
