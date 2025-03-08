package ecs.systems;

import core.KeyboardInput;
import ecs.components.*;
import ecs.components.EndGame;
import ecs.entities.Entity;
import edu.usu.audio.Sound;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Movement extends System {

    KeyboardInput input;
    boolean paused = false;
    Graphics2D graphics;
    Sound thrustSound;
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


    public Movement(Graphics2D graphics, KeyboardInput input, Sound thrustSound) {
        super(ecs.components.LanderMovement.class, ecs.components.LanderPosition.class,
                ecs.components.LanderAppearance.class, ecs.components.Count.class, ecs.components.LanderFuel.class,
                ecs.components.EndGame.class);


        this.graphics = graphics;
        this.input = input;
        this.thrustSound = thrustSound;

        input.registerCommand(GLFW_KEY_UP, false, (double elapsedTime) -> {
            if (!paused) {
                updateThrust();
            }
        });

        input.registerCommand(GLFW_KEY_LEFT, false, (double elapsedTime) -> {
            if (!paused) {
                rotateLeft();
            }
        });

        input.registerCommand(GLFW_KEY_RIGHT, false, (double elapsedTime) -> {
            if (!paused) {
                rotateRight();
            }
        });

        input.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            paused = !paused;
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

        if (paused) {
            glfwPollEvents();
            input.update(elapsedTime);
        }

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

                handlePause();

                if (!paused) {
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

                                if (thrustSound.isPlaying()) {
                                    thrustSound.stop();
                                }
                            }
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

            // Play the thrust sounds
            if (!thrustSound.isPlaying()) {
                thrustSound.play();
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

    private void handlePause() {

        if (!endGame.isEndGame() && paused) {
            // Render pause screen outline
            Color color = new Color(205/255f, 83/255f, 201/255f);
            Rectangle pauseScreenOutline = new Rectangle(-0.255f, -0.155f, 0.51f, 0.31f, 1.0f);
            graphics.draw(pauseScreenOutline, color);

            // Render pause screen
            Rectangle pauseScreen = new Rectangle(-0.25f, -0.15f, 0.5f, 0.3f, 1.0f);
            graphics.draw(pauseScreen, edu.usu.graphics.Color.BLACK);

            // Render pause text
            graphics.drawTextByHeight(endGame.getFont(), "ESC - Continue", -.11f, -0.06f, 0.04f, 1.0f, color);
            graphics.drawTextByHeight(endGame.getFont(), "Q - Quit", -0.055f, 0.02f, 0.04f, 1.0f, color);
        }
    }
}
