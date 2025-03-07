package ecs.systems;

import core.MyRandom;
import ecs.components.ParticleEmitter;
import ecs.components.ParticleInfo;
import ecs.entities.Entity;
import ecs.entities.Particle;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Texture;
import ecs.entities.ParticleEmitters;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParticleSystem extends System{

    private final HashMap<Long, Entity> particles = new HashMap<>();
    private final MyRandom random = new MyRandom();
    private Graphics2D graphics;
    private Texture texCrashOrb;
    private Texture texThrustOrb;

    public ParticleSystem(Graphics2D graphics, Texture texCrashOrb, Texture texThrustOrb) {
        super(ecs.components.ParticleEmitter.class);

        this.graphics = graphics;
        this.texCrashOrb = texCrashOrb;
        this.texThrustOrb = texThrustOrb;
    }

    @Override
    public void update(double elapsedTime) {
        for (var entity : entities.values()) {

            ParticleEmitter emitter = entity.get(ecs.components.ParticleEmitter.class);

            // Updates existing particles that need to be removed
            List<Long> removeMe = new ArrayList<>();
            for (Entity p : particles.values()) {
                ParticleInfo particle = p.get(ecs.components.ParticleInfo.class);

                // If the particle isn't alive anymore, add to the removeMe list
                if (!particleUpdate(elapsedTime, particle)) {
                    removeMe.add(particle.name);
                }
            }

            // Removes dead particles
            for (Long key : removeMe) {
                particles.remove(key);
            }

            // Generates some new particles
            for (int i = 0; i < 8; i++) {
                // Creates a new entity
                var particle = Particle.create(
                    new Vector2f(emitter.center.x, emitter.center.y),
                    this.random.nextCircleVector(),
                    (float) this.random.nextGaussian(emitter.speedMean, emitter.speedStdDev),
                    new Vector2f((float) this.random.nextGaussian(emitter.sizeMean, emitter.sizeStdDev), (float) this.random.nextGaussian(emitter.sizeMean, emitter.sizeStdDev)),
                    this.random.nextGaussian(emitter.lifetimeMean, emitter.lifetimeStdDev));

                // Gets the component for the new entity
                ParticleInfo particleInfo = particle.get(ecs.components.ParticleInfo.class);

                // Puts entity in the list with its name
                particles.put(particleInfo.name, particle);
            }
        }
    }

    public boolean particleUpdate(double elapsedTime, ParticleInfo particleInfo) {
        // Update how long it has been alive
        particleInfo.alive += elapsedTime;

        // Update its center
        particleInfo.center.x += (float) (elapsedTime * particleInfo.speed * particleInfo.direction.x);
        particleInfo.center.y += (float) (elapsedTime * particleInfo.speed * particleInfo.direction.y);
        particleInfo.area.left += (float) (elapsedTime * particleInfo.speed * particleInfo.direction.x);
        particleInfo.area.top += (float) (elapsedTime * particleInfo.speed * particleInfo.direction.y);

        // Rotate proportional to its speed
        particleInfo.rotation += (particleInfo.speed / 0.5f);

        // Return true if this particle is still alive
        return particleInfo.alive < particleInfo.lifeTime;
    }
}
