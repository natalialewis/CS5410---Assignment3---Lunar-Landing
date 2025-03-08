package views;

import core.KeyboardInput;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import static org.lwjgl.glfw.GLFW.*;

public class CreditsView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.Credits;
    private Font font;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics.getWindow());
        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });
    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.Credits;
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        // Updating the keyboard can change the nextGameState
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
    }

    @Override
    public void render(double elapsedTime) {
        final String[] credits = {
                "    LWJGL 2D Framework:",
                "        Professor Dean Mathias",
                "        ",
                "    Game Development:",
                "        Natalia Lewis"
        };

        float top = -0.2f;
        float height = 0.075f;
        final Color color = new Color(205/255f, 83/255f, 201/255f);

        for (String credit: credits) {
            graphics.drawTextByHeight(font, credit, -0.4f, top, height, color);
            top += height;
        }
    }
}
