package ecs.entities;

import edu.usu.graphics.Rectangle;
import org.joml.Vector2f;

public class Particle {

        public static Entity create(Vector2f centerParticle, Vector2f direction, float speed, Vector2f size, double lifeTime) {
            var particle = new Entity();

            particle.add(new ecs.components.ParticleInfo(centerParticle, direction, speed, size, lifeTime));

            return particle;
        }

}
