package ecs.systems;

import ecs.components.LanderMovement;
import ecs.components.LanderPosition;
import edu.usu.graphics.Graphics2D;

public class Movement extends System {

    Graphics2D graphics;

    public Movement(Graphics2D graphics) {
        super(ecs.components.LanderMovement.class, ecs.components.LanderPosition.class);
        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {
        for (var entity: entities.values()) {
            LanderMovement movement = entity.get(ecs.components.LanderMovement.class);
            LanderPosition position = entity.get(ecs.components.LanderPosition.class);
            float elapsedSec = (float) elapsedTime;

            // Update velocity from gravity
            movement.setVelocityX(movement.getVelocityX() + movement.getGravity() * elapsedSec);

            // Calculate velocity vector directions based on rocket angle
            float angle = position.getAngle();

            // Actual velocity vector in terms of up, down, left, right
            float velocityX = movement.getVelocityX();
            float velocityY = movement.getVelocityY();

            // Rotated velocity vector in terms of rocket itself
            float transformedVelocityX = velocityX * (float) Math.cos(angle) + velocityY * (float) Math.sin(angle);
            float transformedVelocityY = -velocityX * (float) Math.sin(angle) + velocityY * (float) Math.cos(angle);

            // Update the position of the lander
            position.setX(position.getX() + transformedVelocityX * elapsedSec);
            position.setY(position.getY() + transformedVelocityY * elapsedSec);
        }
    }
}
