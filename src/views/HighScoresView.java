package views;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.KeyboardInput;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import java.io.FileReader;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class HighScoresView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.HighScores;
    private Font font;
    private static List<String> highScores;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics.getWindow());
        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });

        setHighScores();
    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.HighScores;
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        // Updating the keyboard can change the nextGameState
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
        setHighScores();
    }

    @Override
    public void render(double elapsedTime) {
        final Color color = new Color(205/255f, 83/255f, 201/255f);
        final String message = "High Scores:";
        float height = 0.075f;
        float width = font.measureTextWidth(message, height);
        float top = -0.1925f;

        graphics.drawTextByHeight(font, message, 0.0f - width / 2, top, height, color);
        height = 0.05f;
        top += 0.03f;

        for (String score: highScores) {
            width = font.measureTextWidth(score, height);
            top += height;
            graphics.drawTextByHeight(font, score, 0.0f - width / 2, top, height, color);
        }
    }

    public static void setHighScores() {
        // Loads the high scores from the file
        try (FileReader reader = new FileReader("highscores.json")) {
            Gson gson = new Gson();
            highScores = gson.fromJson(reader,new TypeToken<List<String>>(){}.getType());
        } catch (Exception ex) {
            java.lang.System.out.println("Error loading: " + ex.getMessage());
        }
    }
}
