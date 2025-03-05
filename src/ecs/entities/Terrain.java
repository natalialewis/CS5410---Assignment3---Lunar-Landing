package ecs.entities;

public class Terrain {

    public static Entity create() {
        var terrain = new Entity();

        terrain.add(new ecs.components.TerrainPoints());

        return terrain;
    }
}
