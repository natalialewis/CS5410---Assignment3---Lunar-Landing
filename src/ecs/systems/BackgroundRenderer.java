package ecs.systems;

import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import edu.usu.graphics.Texture;

public class BackgroundRenderer {

    private final Graphics2D graphics;
    private final Texture texBackground;

    public BackgroundRenderer(Graphics2D graphics, Texture texBackground) {
        this.graphics = graphics;
        this.texBackground = texBackground;
    }

    public void update(double elapsedTime) {
        Rectangle gameBackground = new Rectangle(-0.5625f, -0.5625f, 1.125f, 1.125f, -1.0f);
        graphics.draw(texBackground, gameBackground, Color.WHITE);
    }
}