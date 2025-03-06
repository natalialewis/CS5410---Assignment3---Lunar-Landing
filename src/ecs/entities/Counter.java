package ecs.entities;

import edu.usu.graphics.Font;

public class Counter {

    public static Entity create(boolean state, Font font) {
        var counter = new Entity();

        counter.add(new ecs.components.Count(state, font));

        return counter;
    }
}
