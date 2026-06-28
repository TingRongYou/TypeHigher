package com.alligatorX.game;

// Import other classes
import com.alligatorX.game.view.GameScreen;
import com.alligatorX.game.view.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;

// Game for managing and swaping Screen
public class TypeHigher extends Game {
    // batch is public since creating batch for every screen waste memory
    public SpriteBatch batch;
    public Sound typingSound; // Global sound variable
    public Sound errorSound;
    public Sound speedUpSound;
    public Sound systemSound;
    public Music backgroundMusic;

    @Override
    // Load assets
    public void create() {
        batch = new SpriteBatch();

        // Load sound effect once
        typingSound = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/game/type.wav"));
        errorSound = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/game/typo.wav"));
        speedUpSound = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/game/speedUp.wav"));
        systemSound = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/ui/button.wav"));

        // Configure and play the music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/bgm/bgmCasinoRoulettes.mp3"));
        // Restart song when finished
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f); // Reduce volume
        backgroundMusic.play(); // Start playing

        this.setScreen(new MainMenuScreen(this)); // Create screen, default at main menu
    }

    // Create helper method
    public void playTypingSound() {
        // Volume: 50%
        // Pitch: random number between 0.8(deep) and 1.2(high)
        // Pan: 0 (center of the speakers)
        float randomPitch = MathUtils.random(0.8f, 1.2f);
        typingSound.play(0.5f, randomPitch, 0);
    }

    public void playTypoSound() {
        errorSound.play(0.5f);
    }

    public void playSpeedUpSound() {
        speedUpSound.play(0.5f);
    }

    public void playSystemSound() {
        systemSound.play(0.5f);
    }

    @Override
    // Game loop
    public void render() {
        // Clean previous frame and draw new frame with BGRA
        // Open the batch and tells what to draw
        super.render();
    }

    @Override
    // Destroys assets to free up memory
    public void dispose() {
        batch.dispose();
        typingSound.dispose();
        errorSound.dispose();
        backgroundMusic.dispose();
    }
}
