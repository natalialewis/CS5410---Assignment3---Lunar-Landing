package core;

import ecs.entities.Background;
import ecs.entities.Entity;
import ecs.entities.Terrain;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Texture;
import ecs.systems.*;

import java.lang.System;


public class GameModel {
    private ecs.systems.BackgroundRenderer backgroundRenderer;
    private ecs.systems.TerrainRenderer terrainRenderer;

    public void initialize(Graphics2D graphics) {
        var texBackground = new Texture("resources/images/background.jpg");

        // Initialize systems
        backgroundRenderer = new BackgroundRenderer(graphics);
        terrainRenderer = new TerrainRenderer(graphics);

        // Initialize entities
        backgroundRenderer.add(Background.create(texBackground));
        terrainRenderer.add(Terrain.create());

    }

    public void update(double elapsedTime) {
        backgroundRenderer.update(elapsedTime);
        terrainRenderer.update(elapsedTime);
    }
}
