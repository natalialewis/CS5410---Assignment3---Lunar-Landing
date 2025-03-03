package ecs.entities;

import ecs.components.LanderMovement;
import edu.usu.graphics.Texture;
import org.joml.Vector2f;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Lander {

    public static Entity create(Texture image, float x, float y, float angle, Vector2f center, float velocityX,
                                float velocityY, float gravity, float thrust) {
        var lander = new Entity();

        lander.add(new ecs.components.LanderAppearance(image));
        lander.add(new ecs.components.LanderPosition(x, y, angle, center));
        lander.add(new ecs.components.LanderMovement(velocityX, velocityY, gravity, thrust));
        lander.add(new ecs.components.KeyboardControlled(
                Map.of(
                        GLFW_KEY_UP, LanderMovement.Moves.UP,
                        GLFW_KEY_LEFT, LanderMovement.Moves.LEFT,
                        GLFW_KEY_RIGHT, LanderMovement.Moves.RIGHT
                )
        ));
//        lander.add(new ecs.components.LanderFuel(fuel));

        return lander;
    }
}