package ecs.systems;

import core.MyRandom;
import ecs.components.LanderAppearance;
import ecs.components.ParticleEmitter;
import ecs.components.ParticleInfo;
import ecs.entities.Entity;
import ecs.entities.Particle;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParticleSystem extends System{

    private final HashMap<Long, Entity> crashParticles = new HashMap<>();
    private final HashMap<Long, Entity> thrustParticles = new HashMap<>();
    private final MyRandom random = new MyRandom();
    private final Graphics2D graphics;
    private final Texture texCrashOrb;
    private final Texture texThrustOrb;

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

            // If the rocket crashed
            if (emitter.crash) {

                // If the crash particles haven't been spawned yet
                if (!emitter.spawnedCrashParticles) {
                    emitter.spawnedCrashParticles = true;

                    // Generates some new particles
                    for (int i = 0; i < 300; i++) {
                        float size = (float) this.random.nextGaussian(emitter.sizeMean, emitter.sizeStdDev);

                        // Creates a new entity
                        var particle = Particle.create(
                                // Center of the particle
                                new Vector2f(emitter.center.x, emitter.center.y),
                                // Direction of the particle
                                this.random.nextCircleVector(),
                                // Speed of the particle
                                (float) this.random.nextGaussian(emitter.speedMean, emitter.speedStdDev),
                                // Size of the particle
                                new Vector2f(size, size),
                                // Lifetime of the particle
                                this.random.nextGaussian(emitter.lifetimeMean, emitter.lifetimeStdDev));

                        // Gets the component for the new entity
                        ParticleInfo particleInfo = particle.get(ecs.components.ParticleInfo.class);

                        // Puts entity in the list with its name
                        crashParticles.put(particleInfo.name, particle);
                    }
                }

                // Updates existing particles that need to be removed
                List<Long> removeMe = new ArrayList<>();
                for (Entity p : crashParticles.values()) {
                    ParticleInfo particle = p.get(ecs.components.ParticleInfo.class);

                    // If the particle isn't alive anymore, add to the removeMe list
                    if (!particleUpdate(elapsedTime, particle)) {
                        removeMe.add(particle.name);
                    }
                }

                // Removes dead particles
                for (Long key : removeMe) {
                    crashParticles.remove(key);
                }

                // Renders particles
                for (Entity p : crashParticles.values()) {
                    ParticleInfo particle = p.get(ecs.components.ParticleInfo.class);

                    graphics.draw(texCrashOrb, particle.area, particle.rotation, particle.center, Color.WHITE);
                }

                // Checks if all particles have been removed to turn off emitter
                if (crashParticles.isEmpty()) {
                    emitter.crash = false;
                }
            }

            // If the player is using thrust
            if (emitter.thrust) {

                // If the thrust particles haven't been spawned yet
                if (!emitter.spawnedThrustParticles) {
                    emitter.spawnedThrustParticles = true;

                    // Angle opposite of the rocket that the particles need to go in
                    float baseAngle = emitter.angle + (float) Math.PI / 2;
                    float coneWidth = (float) Math.PI / 20;

                    // Generates some new particles
                    for (int i = 0; i < 5; i++) {
                        float size = (float) this.random.nextGaussian(emitter.sizeMean, emitter.sizeStdDev);

                        // Creates a new entity
                        var particle = Particle.create(
                                // Center of the particle
                                new Vector2f(emitter.center.x, emitter.center.y),
                                // Direction of the particle
                                this.random.nextConeVector(baseAngle, coneWidth),
                                // Speed of the particle
                                (float) this.random.nextGaussian(emitter.speedMean, emitter.speedStdDev),
                                // Size of the particle
                                new Vector2f(size, size),
                                // Lifetime of the particle
                                this.random.nextGaussian(emitter.lifetimeMean, emitter.lifetimeStdDev));

                        // Gets the component for the new entity
                        ParticleInfo particleInfo = particle.get(ecs.components.ParticleInfo.class);

                        // Puts entity in the list with its name
                        thrustParticles.put(particleInfo.name, particle);
                    }
                }

                // Updates existing particles that need to be removed
                List<Long> removeMe = new ArrayList<>();
                for (Entity p : thrustParticles.values()) {
                    ParticleInfo particle = p.get(ecs.components.ParticleInfo.class);

                    // If the particle isn't alive anymore, add to the removeMe list
                    if (!particleUpdate(elapsedTime, particle)) {
                        removeMe.add(particle.name);
                    }
                }

                // Removes dead particles
                for (Long key : removeMe) {
                    thrustParticles.remove(key);
                }

                // Renders particles
                for (Entity p : thrustParticles.values()) {
                    ParticleInfo particle = p.get(ecs.components.ParticleInfo.class);

                    graphics.draw(texThrustOrb, particle.area, particle.rotation, particle.center, Color.WHITE);
                }

                // Checks if all particles have been removed to turn off emitter
                if (thrustParticles.isEmpty()) {
                    emitter.thrust = false;
                }
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

    public static void shipThrust(ParticleEmitter thrustParticleEmitter, Vector2f center, float angle) {
        // Start the particle emitter
        thrustParticleEmitter.center = center;
        thrustParticleEmitter.thrust = true;
        thrustParticleEmitter.angle = angle;
        thrustParticleEmitter.spawnedThrustParticles = false;
    }

    public static void shipCrash(ParticleEmitter crashParticleEmitter, Vector2f center) {
        // Start the particle emitter
        crashParticleEmitter.center = center;
        crashParticleEmitter.crash = true;
    }
}
