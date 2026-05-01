package com.alligatorX.game;

// Import other classes
import com.alligatorX.game.view.GameScreen;
import com.alligatorX.game.view.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Game for managing and swaping Screen
public class TypeHigher extends Game {
    // batch is public since creating batch for every screen waste memory
    public SpriteBatch batch;

    @Override
    // Load assets
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MainMenuScreen(this)); // Create screen, default at main menu
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
    }
}
