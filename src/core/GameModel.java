package core;

import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Texture;
import ecs.systems.*;


public class GameModel {
    private ecs.systems.BackgroundRenderer backgroundRenderer;

    public void initialize(Graphics2D graphics) {
        var texBackground = new Texture("resources/images/background.jpg");

        backgroundRenderer = new BackgroundRenderer(graphics, texBackground);
    }

    public void update(double elapsedTime) {
        backgroundRenderer.update(elapsedTime);
    }
}
