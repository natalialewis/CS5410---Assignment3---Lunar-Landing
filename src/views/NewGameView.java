package views;

import core.GameModel;
import core.KeyboardInput;
import edu.usu.graphics.*;

import static org.lwjgl.glfw.GLFW.*;

public class NewGameView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.NewGame;
    private GameModel gameModel;
    private static boolean gameOver = false;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        inputKeyboard = new KeyboardInput(graphics.getWindow());

        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            if (gameOver) {
                nextGameState = GameStateEnum.MainMenu;
            }
        });

        // When Q is pressed, go to main menu
        inputKeyboard.registerCommand(GLFW_KEY_Q, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });

    }

    @Override
    public void initializeSession() {
        gameModel = new GameModel();
        gameModel.initialize(graphics);
        nextGameState = GameStateEnum.NewGame;
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        // Updating the keyboard can change the nextGameState
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
        gameModel.update(elapsedTime);
    }

    @Override
    public void render(double elapsedTime) {
        // Render is in the update of the game model to follow the ECS pattern
    }

    public static void makeGameOver() {
        gameOver = true;
    }
}
