package ecs.components;

import org.joml.Vector2f;

public class ParticleEmitter extends Component{

    public Vector2f center;
    public float sizeMean;
    public float sizeStdDev;
    public float speedMean;
    public float speedStdDev;
    public float lifetimeMean;
    public float lifetimeStdDev;
    public boolean crash = false;
    public boolean thrust = false;
    public boolean spawnedParticles = false;

    public ParticleEmitter(Vector2f center, float sizeMean, float sizeStdDev, float speedMean, float speedStdDev, float lifetimeMean, float lifetimeStdDev) {
        this.center = center;
        this.sizeMean = sizeMean;
        this.sizeStdDev = sizeStdDev;
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
        this.lifetimeMean = lifetimeMean;
        this.lifetimeStdDev = lifetimeStdDev;
    }
}
