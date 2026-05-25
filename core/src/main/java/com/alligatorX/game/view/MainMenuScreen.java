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
import com.sun.org.apache.xpath.internal.operations.Or;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MainMenuScreen implements Screen {

    // Transfer batch
    private TypeHigher game;

    // Camera and font
    private OrthographicCamera camera;
    private FitViewport viewport;
    private BitmapFont font;

    // A simple String to hold what the user is typing
    private String currentTyped = "";

    private float popScale = 1.0f; // Control physical size of text
    private String previousTyped = ""; // Detect if a new letter is entered

    public MainMenuScreen(TypeHigher game) {
        this.game = game;
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(800, 600, camera);

        // FreeType font
        // 1. Load .ttf file from assets folder
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("gamefont.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();

        // 2. Set the BASE resolution size to 48 pixels to avoid blurring
        parameter.size = 48;
        parameter.color = Color.WHITE;

        // 3. Generate the font
        this.font =  generator.generateFont(parameter);

        // 4. Dispose generator to avoid memory leaks
        generator.dispose();

        this.font.getData().setScale(1.0f); // Make text larger

        // Listen to user input
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char character) {

                // Add typed character into currentTyped
                currentTyped += character;

                // Check if it is a valid input
                if (currentTyped.equals("thirty")) {
                    game.setScreen(new GameScreen(game, 30));
                    dispose(); // Dispose the menu to free up memory
                } else if (currentTyped.equals("sixty")) {
                    game.setScreen(new GameScreen(game, 60));
                    dispose();
                } else if (currentTyped.equals("ninety")) {
                    game.setScreen(new GameScreen(game, 90));
                    dispose();
                } else if (currentTyped.equals("unlimited")) {
                    game.setScreen(new GameScreen(game, -1));
                    dispose();
                } else if (currentTyped.equals("quit")) {
                    Gdx.app.exit(); // Closes the game instantly
                }

                // If user messed up and typed something doesn't start with t, s, n, or u, clear it
                if (!"thirty".startsWith(currentTyped) && !"sixty".startsWith(currentTyped) &&
                    !"ninety".startsWith(currentTyped) && !"unlimited".startsWith(currentTyped) &&
                    !"quit".startsWith(currentTyped)) {
                    currentTyped = ""; // Reset their typing
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
        ScreenUtils.clear(0, 0, 0.2f, 1); // Dark blue background for the menu

        // LERP MATH
        // Detect keystroke for 'Pop'
        if (currentTyped.length() > previousTyped.length()) {
            popScale = 1.3f; // 1.3x size
        } else if (currentTyped.length() == 0 && previousTyped.length() > 0) {
            popScale = 1.3f; // Pop if they made mistake and word is cleared
        }
        previousTyped = currentTyped;

        // Shrinks smoothly back to normal
        popScale = MathUtils.lerp(popScale, 1.0f, delta * 15f);

        // Tell batch to look through camera lens before start painting
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Draw Title
        font.setColor(Color.GOLD);
        font.draw(game.batch, "TYPE HIGHER", 0, viewport.getWorldHeight() - 100, viewport.getWorldWidth(), Align.center, false);

        // Draw Instructions
        float startY = viewport.getWorldHeight() / 2 + 100; // Starts slightly above middle screen
        font.setColor(Color.WHITE);
        font.draw(game.batch, "> thirty", 0, startY, viewport.getWorldWidth(), Align.center, false);
        font.draw(game.batch, "> sixty", 0, startY - 50, viewport.getWorldWidth(), Align.center, false);
        font.draw(game.batch, "> ninety", 0, startY - 100, viewport.getWorldWidth(), Align.center, false);
        font.draw(game.batch, "> unlimited", 0, startY - 150, viewport.getWorldWidth(), Align.center, false);
        font.draw(game.batch, "> quit", 0, startY - 200, viewport.getWorldWidth(), Align.center, false);

        // Draw popping text
        font.getData().setScale(popScale); // Apply pop scale

        // Draw what the user typed
        font.setColor(Color.GREEN);
        font.draw(game.batch, "Typing: " + currentTyped, 0, startY - 280, viewport.getWorldWidth(), Align.center, false);

        // Reset Scale
        font.getData().setScale(1.0f);

        game.batch.end();

    }

    // Resize windows
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
