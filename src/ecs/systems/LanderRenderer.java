package ecs.systems;

import ecs.components.LanderAppearance;
import ecs.components.LanderMovement;
import ecs.components.LanderPosition;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;

public class LanderRenderer extends System {

    private final Graphics2D graphics;

    // Initializes the graphics and texture for the background
    public LanderRenderer(Graphics2D graphics) {
        super(ecs.components.LanderAppearance.class, ecs.components.LanderPosition.class);
        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {

        for (var entity: entities.values()) {
            LanderPosition landerPosition = entity.get(ecs.components.LanderPosition.class);
            LanderMovement movement = entity.get(LanderMovement.class);

            // Rocket
            Rectangle rocket = new Rectangle(landerPosition.getX(), landerPosition.getY(), 0.07f, 0.07f,
                    1.0f);
            graphics.draw(entity.get(LanderAppearance.class).getImage(), rocket, landerPosition.getAngle(),
                    landerPosition.getCenter(), Color.WHITE);
        }
    }
}
