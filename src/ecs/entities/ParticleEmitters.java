package ecs.entities;

import org.joml.Vector2f;

public class ParticleEmitters {

    public static Entity create(Vector2f center, float sizeMean, float sizeStdDev, float speedMean, float speedStdDev, float lifetimeMean, float lifetimeStdDev) {
        var particle = new Entity();

        particle.add(new ecs.components.ParticleEmitter(center, sizeMean, sizeStdDev, speedMean, speedStdDev, lifetimeMean, lifetimeStdDev));

        return particle;
    }
}
