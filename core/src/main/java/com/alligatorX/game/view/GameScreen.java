package com.alligatorX.game.view;

// Import other classes
import com.alligatorX.game.TypeHigher;
import com.alligatorX.game.controller.GameController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    // Controller attributes
    private GameController gameController;

    // Transfer batch
    private TypeHigher game;

    // Drawings
    private BitmapFont font;
    private Texture pixelTexture;
    private Pixmap pixmap;

    public GameScreen(TypeHigher game, int targetLength) {
        this.game = game; // Passed in game for batch
        this.gameController = new GameController(targetLength); // Get targeted length
    }

    // Execute once as it runs, initialize the screen
    @Override
    public void show() {
        this.font = new BitmapFont(); // Font
        this.pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888); // Dummy shapes to replace player for demo
        pixmap.setColor(Color.WHITE); // Set color to white
        pixmap.fill(); // Fill (Originally transparent)
        this.pixelTexture = new Texture(pixmap);
        pixmap.dispose(); // Dispose it once created

        // Handle keyboard events
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char character) {
                gameController.handleKeystroke(character);
                return true; // Return true to tells LibGdx, "I handled this input"
            }
        });
    }

    // Infinite loop that runs 60 times a second. Strict order of update logic -> clear screen -> draw
    @Override
    public void render(float delta) {
        gameController.update(delta);
        ScreenUtils.clear(0, 0, 0, 1); // Black background

        // If not game over (in game)
        if (!gameController.getIsGameOver()) {
            game.batch.begin(); // Start paintbrush

            // Player
            game.batch.setColor(Color.RED); // Set player to red color
            game.batch.draw(pixelTexture, gameController.getPlayerX(), gameController.getPlayerY(), 50, 100); // Draw player

            // Font
            game.batch.setColor(Color.WHITE); // Set text to white color
            font.draw(game.batch, "Target Word: " + gameController.getCurrentWord(), 100, 100);
            font.draw(game.batch, gameController.getTypedPortion(), 100, 80);
            font.draw(game.batch, "Remaining Time: " + gameController.getTimeLeft(), 0, Gdx.graphics.getHeight() - 20);
            font.draw(game.batch, "Score: " + gameController.getScore(), 200, Gdx.graphics.getHeight() - 20);

            game.batch.end();
        } else { // If game is over
            game.batch.begin();
            if (gameController.getIsGameWon()) { // If player won
                font.draw(game.batch, "You Win!", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            }
            else { // If player lose
                font.draw(game.batch, "You Lose! Too Many Typos!", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            }
            game.batch.end();
        }

    }

    // Dispose the font created (release memory)
    @Override
    public void dispose() {
        font.dispose();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

}

