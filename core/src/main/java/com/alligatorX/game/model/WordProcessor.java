package com.alligatorX.game.model;

public class WordProcessor {
    private String targetWord;
    private int currentIndex;

    public WordProcessor(String firstTargetWord) {
        this.targetWord = firstTargetWord;
        this.currentIndex = 0;
    }

    public String getTargetWord() {
        return this.targetWord;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public void loadNextWord(String newWord) {
        this.targetWord = newWord;
        this.resetIndex();
    }

    public void resetIndex() {
        this.currentIndex = 0;
    }

    public int getCorrectLetterCount() {
        return this.currentIndex; // If player successfully typed two letter, index will be 2;
    }

    public enum KeystrokeStatus {
        VALID_LETTER,
        WORD_COMPLETED,
        TYPO
    }

    public KeystrokeStatus checkKeystroke(char input) {
        // Compare the input with the current character
        if (input == this.targetWord.charAt(this.currentIndex)) {

            this.currentIndex++; // If match character, move the index forward

            // If currentIndex match with target word length, then the word is completed
            if (this.currentIndex == this.targetWord.length()) {
                return KeystrokeStatus.WORD_COMPLETED;
            }

            return KeystrokeStatus.VALID_LETTER;
        } else {
            return KeystrokeStatus.TYPO;
        }
    }

}
