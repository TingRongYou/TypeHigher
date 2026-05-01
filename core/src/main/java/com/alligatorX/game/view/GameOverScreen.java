package com.alligatorX.game.view;

// Import other classes
import com.alligatorX.game.TypeHigher;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameOverScreen implements Screen {

    // Transfer batch
    private TypeHigher game;

    // Cameras and drawings
    private OrthographicCamera camera;
    private FitViewport viewport;
    private BitmapFont font;

    private boolean isPlayerWon; // Check if player has won
    private int finalScore; // Check user final score
    private int targetLength; // Need for restart

    private String currentTyped = "";

    public GameOverScreen(TypeHigher game, boolean isPlayerWon, int finalScore, int targetLength) {
        this.game = game;
        this.isPlayerWon = isPlayerWon;
        this.finalScore = finalScore;
        this.targetLength = targetLength;
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(800, 600, camera);
        this.font = new BitmapFont();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char character) {
                currentTyped += character; // Add character typed into currentTyped
                // Check user input
                if (currentTyped.equals("restart")) { // if user chooses restart
                    game.setScreen(new GameScreen(game, targetLength)); // Restart game
                    dispose();
                } else if (currentTyped.equals("menu")) { // If user chooses menu
                    game.setScreen(new MainMenuScreen(game)); // Back to main menu
                    dispose();
                } else if (currentTyped.equals("quit")) {
                    Gdx.app.exit();
                }

                if(!"restart".startsWith(currentTyped) && !"menu".startsWith(currentTyped) &&
                !"quit".startsWith(currentTyped)) {
                    currentTyped = ""; // Clear currentTyped
                }
                return true;
            }
            @Override
            public boolean keyDown(int keyCode) {
                // If user press F11, toggle fullscreen
                if (keyCode == com.badlogic.gdx.Input.Keys.F11) {
                    // Check if already fullscreen
                    boolean isFullScreen = Gdx.graphics.isFullscreen();

                    // If already fullscreen
                    if (isFullScreen) {
                        // Go back to default window mode (800x600)
                        Gdx.graphics.setWindowedMode(800, 600);
                    } else { // If still haven't full screen
                        // Get monitor max resolution and go fullscreen
                        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    }
                }
                return true;
            }
        });

    }

    @Override
    public void render(float delta) {
        // Dark red background if lost, dark green background if won
        if (isPlayerWon) {
            ScreenUtils.clear(0, 0.2f, 0, 1);
        } else {
            ScreenUtils.clear(0.2f, 0, 0, 1);
        }

        // Tell batch to look through camera lens before start painting
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Draw title
        font.setColor(Color.WHITE);
        if (isPlayerWon) {
            font.draw(game.batch, "YOU SURVIVED!", 0, viewport.getWorldHeight() - 100, viewport.getWorldWidth(), Align.center, false);
        } else {
            font.draw(game.batch, "YOU FELL...", 0, viewport.getWorldHeight() - 100, viewport.getWorldWidth(), Align.center, false);
        }

        // Draw Information
        font.draw(game.batch, "Final Score: " + finalScore, 0,viewport.getWorldHeight() - 150, viewport.getWorldWidth(), Align.center, false);

        // Draw Instructions
        float startY = viewport.getWorldHeight() / 2 + 50; // Starts slightly above middle screen
        font.draw(game.batch, "> restart", 0, startY, viewport.getWorldWidth(), Align.center, false);
        font.draw(game.batch, "> menu", 0, startY - 30, viewport.getWorldWidth(), Align.center, false);
        font.draw(game.batch, "> quit", 0, startY - 60, viewport.getWorldWidth(), Align.center, false);

        // Draw what the user typed
        font.setColor(Color.GREEN);
        font.draw(game.batch, "Typing: " + currentTyped, 0, viewport.getWorldHeight() / 2 - 90, viewport.getWorldWidth(), Align.center, false);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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

    @Override
    public void dispose() {
        font.dispose();
    }
}
