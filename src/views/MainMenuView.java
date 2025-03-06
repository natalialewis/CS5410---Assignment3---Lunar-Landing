package views;

import core.KeyboardInput;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import static org.lwjgl.glfw.GLFW.*;

public class MainMenuView extends GameStateView {

    private enum MenuState {
        NewGame,
        HighScores,
        Credits,
        Quit;

        // Moves the cursor down one menu item
        public MenuState next() {
            // The remainder moves the cursor to the top if it is at the bottom
            int nextOrdinal = (this.ordinal() + 1) % MenuState.values().length;
            return MenuState.values()[nextOrdinal];
        }

        // Moves the cursor up one menu item
        public MenuState previous() {
            // The remainder moves the cursor to the bottom if it is at the top
            int previousOrdinal = (this.ordinal() - 1) % MenuState.values().length;
            if (previousOrdinal < 0) {
                previousOrdinal = Quit.ordinal();
            }
            return MenuState.values()[previousOrdinal];
        }
    }

    // The current menu item selected; Starts with New core.Game
    private MenuState currentSelection = MenuState.NewGame;
    private KeyboardInput inputKeyboard;
    // Tracks next screen to go to
    private GameStateEnum nextGameState = GameStateEnum.MainMenu;
    private Font fontMenu;
    private Font fontSelected;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        fontMenu = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);
        fontSelected = new Font("resources/fonts/Roboto-Bold.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics.getWindow());

        // Arrow keys to navigate the menu
        inputKeyboard.registerCommand(GLFW_KEY_UP, true, (double elapsedTime) -> {
            currentSelection = currentSelection.previous();
        });
        inputKeyboard.registerCommand(GLFW_KEY_DOWN, true, (double elapsedTime) -> {
            currentSelection = currentSelection.next();
        });
        // When Enter is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ENTER, true, (double elapsedTime) -> {
            nextGameState = switch (currentSelection) {
                case MenuState.NewGame -> GameStateEnum.NewGame;
                case MenuState.HighScores -> GameStateEnum.HighScores;
                case MenuState.Credits -> GameStateEnum.Credits;
                case MenuState.Quit -> GameStateEnum.Quit;
            };
        });
    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.MainMenu;
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
        final Color fontMenuColor = new Color(95/255f, 48/255f, 142/255f);
        final Color fontSelectedColor = new Color(205/255f, 83/255f, 201/255f);

        final float HEIGHT_MENU_ITEM = 0.075f;
        float top = -0.15f;
        top = renderMenuItem(currentSelection == MenuState.NewGame ? fontSelected : fontMenu, "New Game", top, HEIGHT_MENU_ITEM, currentSelection == MenuState.NewGame ? fontSelectedColor: fontMenuColor);
        top = renderMenuItem(currentSelection == MenuState.HighScores ? fontSelected : fontMenu, "High Scores", top, HEIGHT_MENU_ITEM, currentSelection == MenuState.HighScores ? fontSelectedColor : fontMenuColor);
        top = renderMenuItem(currentSelection == MenuState.Credits ? fontSelected : fontMenu, "Credits", top, HEIGHT_MENU_ITEM, currentSelection == MenuState.Credits ? fontSelectedColor : fontMenuColor);
        renderMenuItem(currentSelection == MenuState.Quit ? fontSelected : fontMenu, "Quit", top, HEIGHT_MENU_ITEM, currentSelection == MenuState.Quit ? fontSelectedColor : fontMenuColor);
    }

    /**
     * Centers the text horizontally, at the specified top position.
     * It also returns the vertical position to draw the next menu item
     */
    private float renderMenuItem(Font font, String text, float top, float height, Color color) {
        float width = font.measureTextWidth(text, height);
        graphics.drawTextByHeight(font, text, 0.0f - width / 2, top, height, color);

        return top + height;
    }
}
