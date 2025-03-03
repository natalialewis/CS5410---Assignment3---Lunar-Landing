package ecs.entities;

import edu.usu.graphics.Texture;
import org.joml.Vector2f;

public class Lander {

    public static Entity create(Texture image, float x, float y, float angle, Vector2f center, float velocityX,
                                float velocityY, float gravity) {
        var lander = new Entity();

        lander.add(new ecs.components.LanderAppearance(image));
        lander.add(new ecs.components.LanderPosition(x, y, angle, center));
        lander.add(new ecs.components.LanderMovement(velocityX, velocityY, gravity));
//        lander.add(new ecs.components.LanderFuel(fuel));

        return lander;
    }
}