package com.alligatorX.game.model;

public class GameSession {

    private int targetLength;
    private int wordCompleted;

    public GameSession(int targetLength) {
        this.targetLength = targetLength;
        this.wordCompleted = 0; // Always starts at 0
    }

    public int getTargetLength() {
        return this.targetLength;
    }

    public int getWordCompleted() {
        return this.wordCompleted;
    }

    public void addWordCompleted() {
        this.wordCompleted++;
    }

    public boolean isPlayerWon() {
        if (this.targetLength == -1) {
            return false;
        }
        return this.wordCompleted >= this.targetLength;
    }


}
