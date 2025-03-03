package ecs.systems;

import core.KeyboardInput;
import ecs.components.LanderMovement;
import ecs.components.LanderPosition;
import edu.usu.graphics.Graphics2D;

import static org.lwjgl.glfw.GLFW.*;

public class Movement extends System {

    Graphics2D graphics;
    KeyboardInput input;
    LanderMovement movement;
    LanderPosition position;
    LanderRenderer renderer;
    float elapsedSec;


    public Movement(Graphics2D graphics, KeyboardInput input) {
        super(ecs.components.LanderMovement.class, ecs.components.LanderPosition.class);

        this.graphics = graphics;
        this.input = input;

        input.registerCommand(GLFW_KEY_UP, false, (double elapsedTime) -> {
            updateThrust();
        });
    }


    @Override
    public void update(double elapsedTime) {
        elapsedSec = (float) elapsedTime;

        for (var entity : entities.values()) {
            movement = entity.get(LanderMovement.class);
            position = entity.get(LanderPosition.class);

            // Apply gravity and thrust forces
            updateGravity(movement, position, elapsedSec);


            glfwPollEvents();
            input.update(elapsedTime);
        }

    }

    private void updateThrust() {
        // Update velocity from thrust
        movement.setVelocityY(movement.getVelocityY() + movement.getThrust() * elapsedSec);

        // Calculate velocity vector directions based on rocket angle
        float angle = position.getAngle();

        // Actual velocity vector in terms of up, down, left, right
        float velocityX = movement.getVelocityX();
        float velocityY = movement.getVelocityY();

        // Rotated velocity vector in terms of rocket itself
        float transformedVelocityX = velocityX * (float) Math.cos(angle) - velocityY * (float) Math.sin(angle);
        float transformedVelocityY = -velocityX * (float) Math.sin(angle) + velocityY * (float) Math.cos(angle);

        // Update the position of the lander
        position.setX(position.getX() + transformedVelocityX * elapsedSec);
        position.setY(position.getY() + transformedVelocityY * elapsedSec);
        movement.check(22.2f);
    }

    private void updateGravity(LanderMovement movement, LanderPosition position, float elapsedSec) {
        // Update velocity from gravity
        movement.setVelocityY(movement.getVelocityY() + movement.getGravity() * elapsedSec);

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
