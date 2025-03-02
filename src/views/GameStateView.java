package views;

import edu.usu.graphics.Graphics2D;

public abstract class GameStateView implements IGameState {
    protected Graphics2D graphics;

    @Override
    public void initialize(Graphics2D graphics) {
        this.graphics = graphics;
    }

    @Override
    public void initializeSession() {};

    @Override
    public abstract GameStateEnum processInput(double elapsedTime);

    @Override
    public abstract void update(double elapsedTime);

    @Override
    public abstract void render(double elapsedTime);
}
