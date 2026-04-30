package com.alligatorX.game.controller;

// Import other classes
import com.alligatorX.game.model.Player;
import com.alligatorX.game.model.GameSession;
import com.alligatorX.game.model.TimeManager;
import com.alligatorX.game.model.WordProcessor;

import java.util.Arrays;
import java.util.List;

public class GameController {

    private List<String> words; // A list of words for testing
    private boolean isGameOver; // Check if game is over

    // Models attributes
    private Player player;
    private GameSession gameSession;
    private WordProcessor wordProcessor;
    private TimeManager timeManager;

    public GameController(int targetLength) {
        this.words = Arrays.asList("first", "hello", "world", "john", "johnny"); // Dummy list for testing
        this.isGameOver = false; // Game over is false at default

        // Initialize the models
        this.player = new Player();
        this.gameSession = new GameSession(targetLength); // Pass in targetLength chosen by user
        this.wordProcessor = new WordProcessor(this.words.get(0)); // Get the first word from the list
        this.timeManager = new TimeManager();

        timeManager.resetTimerForNewWord(wordProcessor.getTargetWord().length()); // Set remainingTime for first word

    }

    // Getters method
    public String getCurrentWord() {
        return this.wordProcessor.getTargetWord();
    }

    public String getTypedPortion() {
        return this.wordProcessor.getTargetWord().substring(0, wordProcessor.getCurrentIndex());
    }

    public float getTimeLeft() {
        return this.timeManager.getRemainingTime();
    }

    public int getScore() {
        return this.gameSession.getWordCompleted();
    }

    public float getPlayerY() {
        return this.player.getCurrentPosition();
    }

    public float getPlayerX() {
        return 30;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }

    public boolean getIsGameWon() {
        return gameSession.isPlayerWon();
    }

    // Update the time every frame
    public void update(float deltaTime) {

        // If game is over, return
        if (isGameOver) {
            return;
        }

        timeManager.updateTimer(deltaTime); // Pass delta time to update timer

        // If time out for a word
        if (timeManager.isTimeUp()) {
            player.revertToCheckpoint(); // Back to checkpoint
            wordProcessor.resetIndex(); // Reset index
            timeManager.resetTimerForNewWord(wordProcessor.getTargetWord().length()); // Reset timer
        }
    }

    // Handle keystroke input from player
    public void handleKeystroke(char input) {

        if (isGameOver) {
            return;
        }

        // Get keystroke status from wordProcessor
        WordProcessor.KeystrokeStatus keystrokeStatus = wordProcessor.checkKeystroke(input);

        // If it is a typo
        if (keystrokeStatus == WordProcessor.KeystrokeStatus.TYPO) {
            player.addTypoCount(); // Increment the typo count
            // If typo equal than 5
            if (player.getTotalTypos() == 5) {
                isGameOver = true; // Game over
            } else { // If not
                player.revertToCheckpoint(); // Back to checkpoint
                wordProcessor.resetIndex(); // Reset index
                timeManager.resetTimerForNewWord(wordProcessor.getTargetWord().length()); // Reset timer
            }
        } else if (keystrokeStatus == WordProcessor.KeystrokeStatus.VALID_LETTER) { // If it is a valid letter
            player.climbUp(); // Player climb up a step
        } else { // If the word is completed
            player.climbUp();
            player.saveCheckpoint(); // Save checkpoint
            gameSession.addWordCompleted(); // Increment word completed
            // If player has won
            if (gameSession.isPlayerWon()) {
                isGameOver = true; // Game over
            } else {
                wordProcessor.loadNextWord(this.words.get(gameSession.getWordCompleted())); // Load the next word in the list
                timeManager.resetTimerForNewWord(wordProcessor.getTargetWord().length()); // Reset timer
            }
        }

    }

}
