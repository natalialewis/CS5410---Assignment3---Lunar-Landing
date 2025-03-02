package ecs.systems;

import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import edu.usu.graphics.Texture;

public class BackgroundRenderer {

    private final Graphics2D graphics;
    private final Texture texBackground;

    // Initializes the graphics and texture for the background
    public BackgroundRenderer(Graphics2D graphics, Texture texBackground) {
        this.graphics = graphics;
        this.texBackground = texBackground;
    }

    // Renders the background
    public void update(double elapsedTime) {
        // White Border
        Rectangle border = new Rectangle(-0.5025f, -0.5025f, 1.005f, 1.005f, -1.0f);
        graphics.draw(border, Color.WHITE);

        // Space Background
        Rectangle gameBackground = new Rectangle(-0.5f, -0.5f, 1.0f, 1.0f, -1.0f);
        graphics.draw(texBackground, gameBackground, Color.WHITE);
    }
}