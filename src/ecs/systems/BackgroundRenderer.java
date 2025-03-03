package ecs.systems;

import ecs.components.BackgroundAppearance;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;

public class BackgroundRenderer extends System {

    private final Graphics2D graphics;

    // Initializes the graphics and texture for the background
    public BackgroundRenderer(Graphics2D graphics) {
        super(ecs.components.BackgroundAppearance.class);
        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {

        for (var entity: entities.values()) {
            // Border
            Rectangle border = new Rectangle(-0.5025f, -0.5025f, 1.005f, 1.005f, -1.0f);
            graphics.draw(border, entity.get(BackgroundAppearance.class).borderColor);

            // Space Background
            Rectangle gameBackground = new Rectangle(-0.5f, -0.5f, 1.0f, 1.0f, -1.0f);
            graphics.draw(entity.get(BackgroundAppearance.class).image, gameBackground, Color.WHITE);
        }
    }
}