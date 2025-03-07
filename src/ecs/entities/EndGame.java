package ecs.entities;

import edu.usu.graphics.Font;

public class EndGame {

    public static Entity create(Font font) {
        var endGame = new Entity();

        endGame.add(new ecs.components.EndGame(font));

        return endGame;
    }
}
