package ecs.systems;

import core.KeyboardInput;
import ecs.components.*;
import ecs.components.EndGame;
import ecs.entities.Entity;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Movement extends System {

    KeyboardInput input;
    LanderMovement movement;
    LanderPosition position;
    LanderAppearance appearance;
    LanderFuel fuel;
    Count count;
    EndGame endGame;
    ParticleEmitter thrustParticleEmitter;
    float elapsedSec;
    boolean thrustUpdated = false;
    float rotateSpeed = 1.5f;


    public Movement(KeyboardInput input) {
        super(ecs.components.LanderMovement.class, ecs.components.LanderPosition.class,
                ecs.components.LanderAppearance.class, ecs.components.Count.class, ecs.components.LanderFuel.class,
                ecs.components.EndGame.class);


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
    protected boolean isInterested(Entity entity) {
        return (entity.contains(LanderMovement.class)
                && entity.contains(LanderPosition.class)
                && entity.contains(LanderAppearance.class)
                && entity.contains(LanderFuel.class) ||
                entity.contains(Count.class) ||
                entity.contains(ParticleEmitter.class) ||
                entity.contains(ecs.components.EndGame.class));
    }


    @Override
    public void update(double elapsedTime) {
        thrustUpdated = false;
        elapsedSec = (float) elapsedTime;

        for (var entity : entities.values()) {

            if (entity.contains(Count.class)) {
                count = entity.get(Count.class);
            } else if (entity.contains(ecs.components.EndGame.class)) {
                endGame = entity.get(ecs.components.EndGame.class);
            } else if (entity.contains(ParticleEmitter.class)) {
                thrustParticleEmitter = entity.get(ParticleEmitter.class);
            }else {
                movement = entity.get(LanderMovement.class);
                position = entity.get(LanderPosition.class);
                fuel = entity.get(LanderFuel.class);
                appearance = entity.get(LanderAppearance.class);
            }

            if (count != null && movement != null && position != null && fuel != null && appearance != null
                    && thrustParticleEmitter != null) {

                if (!count.getCountDown() && !movement.isMoveable() && !endGame.isEndGame()) {
                    movement.startMoving();
                }

                if (entity.contains(LanderMovement.class) && !count.getCountDown()) {
                    // If the lander is moveable (hasn't crashed, hasn't landed, etc.)
                    if (movement.isMoveable()) {
                        glfwPollEvents();
                        input.update(elapsedTime);

                        // If the thrust was not updated, apply gravity
                        if (!thrustUpdated) {
                            // Apply gravity
                            updateGravity(movement, position, elapsedSec);
                        }
                    }
                }
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
        if (fuel.getFuel() > 0) {
            thrustUpdated = true;

            // Start the particle emitter
            ecs.systems.ParticleSystem.shipThrust(thrustParticleEmitter, position.getCenter(), position.getAngle());

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
            if (newLevel >= 0.0f) {
                fuel.setFuel(Math.abs(newLevel));
            }
        }

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
