package com.alligatorX.game.controller;

// Import other classes
import com.alligatorX.game.model.Player;
import com.alligatorX.game.model.GameSession;
import com.alligatorX.game.model.TimeManager;
import com.alligatorX.game.model.WordProcessor;

import com.badlogic.gdx.Gdx;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class GameController {

    private List<String> words; // A list of words for testing

    // Models attributes
    private Player player;
    private GameSession gameSession;
    private WordProcessor wordProcessor;
    private TimeManager timeManager;

    // Game state enumerators
    public enum GameState {
        PLAYING,
        PAUSE,
        GAME_OVER
    }

    private GameState currentState;

    public GameController(int targetLength) {

        // Read words from text file
        this.words = new ArrayList<>();
        // Open a "Stream" to read the file one line at a time
        try(BufferedReader bufferedReader = new BufferedReader(Gdx.files.internal("words.txt").reader())) {
            String line;
            // Keep reading until there are no lines left
            while ((line = bufferedReader.readLine()) != null) {
                String cleanWords = line.trim().toLowerCase(); // Remove blank spaces and force into lower case
                if (!cleanWords.isEmpty() && cleanWords.matches("[a-z]+")) { // If not empty
                    this.words.add(cleanWords);
                }
            }
        } catch (Exception e) {
            System.out.println("CRITICAL ERROR: Could not load words.txt!");
            e.printStackTrace(); // Prints the exact error to console
        }

        // 4. Shuffle list so that the game is random every time
        Collections.shuffle(this.words);

        this.currentState = GameState.PLAYING; // Game state is playing as default

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

    public GameState getGameState() {
        return this.currentState;
    }

    public boolean getIsGameWon() {
        return gameSession.isPlayerWon();
    }

    // Update the time every frame
    public void update(float deltaTime) {

        // If game is over, return
        if (this.currentState == GameState.GAME_OVER) {
            return;
        }

        // Should not update anything unless is in playing state
        if (this.currentState == GameState.PLAYING) {
            timeManager.updateTimer(deltaTime); // Pass delta time to update timer

            // If time out for a word
            if (timeManager.isTimeUp()) {
                player.revertToCheckpoint(); // Back to checkpoint
                wordProcessor.resetIndex(); // Reset index
                timeManager.resetTimerForNewWord(wordProcessor.getTargetWord().length()); // Reset timer
            }
        }

    }

    // Handle keystroke input from player
    public void handleKeystroke(char input) {

        // Only handle keystroke if user is in playing state
        if (this.currentState != GameState.PLAYING) {
            return;
        }

        // Get keystroke status from wordProcessor
        WordProcessor.KeystrokeStatus keystrokeStatus = wordProcessor.checkKeystroke(input);

        // If it is a typo
        if (keystrokeStatus == WordProcessor.KeystrokeStatus.TYPO) {
            player.addTypoCount(); // Increment the typo count
            // If typo equal than 5
            if (player.getTotalTypos() == 5) {
                this.currentState = GameState.GAME_OVER; // Game over
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
                this.currentState = GameState.GAME_OVER; // Game over
            } else {
                // Safely calculate index using Modulo so it loops around if we run out of words
                int safeIndex = gameSession.getWordCompleted() % this.words.size();

                wordProcessor.loadNextWord(this.words.get(safeIndex)); // Load the next word in the list
                timeManager.resetTimerForNewWord(wordProcessor.getTargetWord().length()); // Reset timer
            }
        }

    }

    // Toggle Pause Screen
    public void togglePause() {
        if (this.currentState == GameState.GAME_OVER) {
            return; // Cannot toggle pause if it is already game over
        }

        if (this.currentState == GameState.PLAYING) {
            this.currentState = GameState.PAUSE; // If the current state is playing, make it pause
        }
        else {
            this.currentState = GameState.PLAYING; // If the current state is pause, make it playing
        }

    }

}
