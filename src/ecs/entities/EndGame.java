package ecs.entities;

public class EndGame {

    public static Entity create() {
        var endGame = new Entity();

        endGame.add(new ecs.components.EndGame());

        return endGame;
    }
}
