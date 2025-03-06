package ecs.systems;

import edu.usu.graphics.Graphics2D;

public class EndGame extends System {

    private final Graphics2D graphics;

    public EndGame(Graphics2D graphics) {
        super(ecs.components.EndGame.class);

        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {

    }

}
