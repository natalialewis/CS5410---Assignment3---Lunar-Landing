package ecs.systems;

import core.KeyboardInput;
import ecs.components.LanderAppearance;
import ecs.components.LanderFuel;
import ecs.components.LanderMovement;
import ecs.components.LanderPosition;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Movement extends System {

    Graphics2D graphics;
    KeyboardInput input;
    LanderMovement movement;
    LanderPosition position;
    LanderAppearance appearance;
    LanderFuel fuel;
    float elapsedSec;
    boolean thrustUpdated = false;
    float rotateSpeed = 1.5f;


    public Movement(Graphics2D graphics, KeyboardInput input) {
        super(ecs.components.LanderMovement.class, ecs.components.LanderPosition.class,
                ecs.components.LanderAppearance.class);

        this.graphics = graphics;
        this.input = input;

        input.registerCommand(GLFW_KEY_UP, false, (double elapsedTime) -> {
            updateThrust();
        });

        input.registerCommand(GLFW_KEY_LEFT, false, (double elapsedTime) -> {
            rotateLeft();
        });

        input.registerCommand(GLFW_KEY_RIGHT, false, (double elapsedTime) -> {
            rotateRight();
        });
    }


    @Override
    public void update(double elapsedTime) {
        thrustUpdated = false;
        elapsedSec = (float) elapsedTime;

        for (var entity : entities.values()) {
            movement = entity.get(LanderMovement.class);
            position = entity.get(LanderPosition.class);
            fuel = entity.get(LanderFuel.class);
            appearance = entity.get(LanderAppearance.class);

            glfwPollEvents();
            input.update(elapsedTime);

            // If the thrust was not updated, apply gravity
            if (!thrustUpdated) {
                // Apply gravity
                updateGravity(movement, position, elapsedSec);
            }
        }

    }

    private void rotateLeft() {
        position.setAngle(position.getAngle() - rotateSpeed * elapsedSec);
    }

    private void rotateRight() {
        position.setAngle(position.getAngle() + rotateSpeed * elapsedSec);
    }

    private void updateThrust() {
        thrustUpdated = true;

        // Convert direction angle into a vector
        float angle = position.getAngle() - ((float) Math.PI / 2.0f);

        float directionX = (float) Math.cos(angle);
        float directionY = (float) Math.sin(angle);

        // Update velocity from thrust
        movement.setVelocityX(movement.getVelocityX() + movement.getThrust() * elapsedSec * directionX);
        movement.setVelocityY(movement.getVelocityY() + movement.getThrust() * elapsedSec * directionY);

        // Update the position of the lander
        position.setX(position.getX() + movement.getVelocityX() * elapsedSec);
        position.setY(position.getY() + movement.getVelocityY() * elapsedSec);

        // Update the center of the lander
        position.setCenter(new Vector2f(position.getX() + appearance.getWidth() / 2, position.getY() +
                appearance.getHeight() / 2));

        // Update fuel
        float newLevel = Float.parseFloat(String.format("%.2f",fuel.getFuel() - elapsedSec));
        fuel.setFuel(newLevel);
    }

    private void updateGravity(LanderMovement movement, LanderPosition position, float elapsedSec) {

        // Update velocity from gravity (always down vertically)
        movement.setVelocityY(movement.getVelocityY() + movement.getGravity() * elapsedSec);

        // Update the position of the lander
        position.setX(position.getX() + movement.getVelocityX() * elapsedSec);
        position.setY(position.getY() + movement.getVelocityY() * elapsedSec);

        // Update the center of the lander
        position.setCenter(new Vector2f(position.getX() + appearance.getWidth() / 2, position.getY() +
                appearance.getHeight() / 2));
    }
}
