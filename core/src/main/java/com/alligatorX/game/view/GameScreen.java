package com.alligatorX.game.view;

// Import other classes
import com.alligatorX.game.TypeHigher;
import com.alligatorX.game.controller.GameController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;

public class GameScreen implements Screen {

    // Controller attributes
    private GameController gameController;

    // Transfer batch
    private TypeHigher game;

    // Save to pass to game over screen
    private int targetLength;

    // Drawings
    private BitmapFont font;
    private GlyphLayout layout; // Combine typed words and display word into one
    private Texture pixelTexture;
    private Pixmap pixmap;

    public GameScreen(TypeHigher game, int targetLength) {
        this.game = game; // Passed in game for batch
        this.targetLength = targetLength;
        this.gameController = new GameController(targetLength); // Get targeted length
    }

    // Cameras and resizing
    private OrthographicCamera camera; // 2D Camera that decides what part of the game world to look at
    private FitViewport viewport; // Scales the game up and down but maintaining aspect ratio by adding black bars if necessary

    // Track pause typing
    private String pauseTyped = "";

    private float popScale = 2.0f; // Control physical size of text
    private String previousTyped = ""; // Detect if a new letter is entered

    // Execute once as it runs, initialize the screen
    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(800, 600, camera); // 800x600 as our virtual world size

        this.font = new BitmapFont(); // Font
        this.font.getData().setScale(2f); // Make text larger
        this.layout = new GlyphLayout();
        this.pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888); // Dummy shapes to replace player for demo
        pixmap.setColor(Color.WHITE); // Set color to white
        pixmap.fill(); // Fill (Originally transparent)
        this.pixelTexture = new Texture(pixmap);
        pixmap.dispose(); // Dispose it once created

        // Handle keyboard events
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char character) {
                // If the current state is PAUSED
                if (gameController.getGameState() == GameController.GameState.PAUSE) {
                    pauseTyped += character; // Track the input during pause screen
                    // Check what is the input
                    if (pauseTyped.equals("quit")) {
                        Gdx.app.exit();
                    } else if (!"quit".startsWith(pauseTyped)) {
                        pauseTyped = ""; // Clear pauseTyped if not "quit"
                    }
                } else if (gameController.getGameState() == GameController.GameState.PLAYING) {
                    gameController.handleKeystroke(character);
                }
                return true; // Return true to tells LibGdx, "I handled this input"
            }
            // Since ESC doesn't produce a specific printable character
            @Override
            public boolean keyDown(int keyCode) {
                // If user press ESC
                if (keyCode == com.badlogic.gdx.Input.Keys.ESCAPE) {
                    gameController.togglePause(); // Pause the game
                    pauseTyped = ""; // Wiped the pause String if they unpause
                    return true;
                }
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
                return false;
            }
        });
    }

    // Infinite loop that runs 60 times a second. Strict order of update logic -> clear screen -> draw
    @Override
    public void render(float delta) {
        gameController.update(delta);
        ScreenUtils.clear(0, 0, 0, 1); // Black background

        // Tell the batch to look through the camera lens before starts painting
        game.batch.setProjectionMatrix(camera.combined);

        // 1. If playing (in game)
        if (gameController.getGameState() == GameController.GameState.PLAYING) {
            game.batch.begin(); // Start paintbrush

            // Player
            game.batch.setColor(Color.RED); // Set player to red color
            game.batch.draw(pixelTexture, gameController.getPlayerX(), gameController.getPlayerY(), 50, 100); // Draw player

            // Font
            game.batch.setColor(Color.WHITE); // Set text to white color

            // 1. Get String
            String fullWord = gameController.getCurrentWord();
            String typed = gameController.getTypedPortion();
            // Cuts the string (If full word is "apple", typed "app", gives "le")
            String remainingWord = fullWord.substring(typed.length());

            // 2. Detect keystroke for 'Pop'
            // If typed word got longer, trigger pop effect
            if (typed.length() > previousTyped.length()) {
                popScale = 2.4f; // Jump to 2.6x size
            } else if (typed.length() == 0 && previousTyped.length() > 0) {
                popScale = 2.4f; // If player made a mistake or finished a word, pop again
            }
            previousTyped = typed; // Update previousTyped for next frame

            // 3. LERP the Scale
            // Shrinks back to normal scale smoothly
            popScale = MathUtils.lerp(popScale, 2.0f, delta * 15f); // Back to normal 1.0f scale, 'delta * 15f' means speed, higher value faster speed
            font.getData().setScale(popScale); // Apply scale to the font

            // 4.Find center of the String
            float textY = viewport.getWorldHeight() / 2;
            // Measure full word to perfectly center it
            layout.setText(font, fullWord);
            float startX = (viewport.getWorldWidth() - layout.width) / 2;

            // 5. Draw typed portion as green
            font.setColor(Color.GREEN);
            font.draw(game.batch, typed, startX, textY);

            // 6. Measure typed portion
            layout.setText(font, typed); // Measure just the green text
            float offset = layout.width;// See how wide it is

            // 7. Draw remaining portion in gray
            font.setColor(Color.GRAY);
            // Draw gray text to starts where the green text ended
            font.draw(game.batch, remainingWord, startX + offset, textY);

            // Draw HUD
            // Reset font back to normal scale, so it won't affect others
            font.getData().setScale(2.0f);
            font.setColor(Color.WHITE);
            font.draw(game.batch, "Remaining Time: " + (int) gameController.getTimeLeft(), 0, viewport.getWorldHeight() - 20);
            font.draw(game.batch, "Score: " + gameController.getScore(), 0, viewport.getWorldHeight() - 20, viewport.getWorldWidth(), Align.center, false);

            game.batch.end();
            // 2. If paused (in game)
        } else if (gameController.getGameState() == GameController.GameState.PAUSE) {
            game.batch.begin();

            // Draw the game frozen in background
            // Player
            game.batch.setColor(Color.RED); // Set player to red color
            game.batch.draw(pixelTexture, gameController.getPlayerX(), gameController.getPlayerY(), 50, 100); // Draw player

            // Font
            game.batch.setColor(Color.WHITE); // Set text to white color
            font.draw(game.batch, "Target Word: " + gameController.getCurrentWord(), 100, 100);
            font.draw(game.batch, gameController.getTypedPortion(), 100, 80);
            font.draw(game.batch, "Remaining Time: " + gameController.getTimeLeft(), 0, viewport.getWorldHeight() - 20);
            font.draw(game.batch, "Score: " + gameController.getScore(), 0, viewport.getWorldHeight() - 20, viewport.getWorldWidth(), Align.center, false);

            // Draw PAUSED text in the middle
            font.draw(game.batch, "PAUSED - Press ESC to Resume", 0, viewport.getWorldHeight() / 2, viewport.getWorldWidth(), Align.center, false);
            font.draw(game.batch, "Type 'quit' to exit", 0, viewport.getWorldHeight() / 2, viewport.getWorldWidth(), Align.center, false);
            game.batch.end();

        }
        else { // 3. If game is over
            game.setScreen(new GameOverScreen(game, gameController.getIsGameWon(), gameController.getScore(), this.targetLength));
            dispose();
        }
    }

    // Dispose the font created (release memory)
    @Override
    public void dispose() {
        font.dispose();
    }

    // Player resize the window
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true tells the camera to center the screens
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

