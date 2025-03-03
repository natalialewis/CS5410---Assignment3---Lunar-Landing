package ecs.entities;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

public class Background {

    public static Entity create(Texture image) {
        var background = new Entity();

        background.add(new ecs.components.BackgroundAppearance(image, Color.WHITE));

        return background;
    }
}
