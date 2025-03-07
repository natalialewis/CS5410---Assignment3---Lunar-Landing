package ecs.systems;

import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;

public class EndGame extends System {

    private final Graphics2D graphics;

    public EndGame(Graphics2D graphics) {
        super(ecs.components.EndGame.class);

        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {

        for (var entity: entities.values()) {
            ecs.components.EndGame endGame = entity.get(ecs.components.EndGame.class);

            if (endGame.isEndGame() && endGame.getScore() == 0.0f) {
                float score = getScore(endGame.isSafeLanding(), endGame.getFuelLeft(), endGame.getLevel());
                endGame.setScore(score);
            }

            if (endGame.isEndGame()) {
                renderScore(endGame);
            }
        }
    }

    public float getScore(boolean safeLanding, float fuelLeft, int level) {
        if (safeLanding) {
            return 30 + (fuelLeft * level);
        } else {
            return 10 + (fuelLeft * level);
        }
    }

    public void renderScore(ecs.components.EndGame endGame) {
        Color color = new Color(205/255f, 83/255f, 201/255f);

        graphics.drawTextByHeight(endGame.getFont(), String.format("Score: %.0f", endGame.getScore()), 0.29f, -0.46f, 0.04f, 1.0f, color);
    }

}
