package ecs.components;

import edu.usu.graphics.Rectangle;
import org.joml.Vector2f;

public class ParticleInfo extends Component {

    public long name;
    public static long nextName = 0;
    public Vector2f center;
    public Vector2f direction;
    public float speed;
    public Vector2f size;
    public Rectangle area;
    public float rotation;
    public double lifeTime;
    public double alive;

    public ParticleInfo(Vector2f center, Vector2f direction, float speed, Vector2f size, double lifeTime) {
        this.name = nextName++;
        this.center = center;
        this.direction = direction;
        this.speed = speed;
        this.size = size;
        this.area = new Rectangle(center.x - size.x / 2, center.y - size.y / 2, size.x, size.y);
        this.lifeTime = lifeTime;
        this.alive = 0;
        this.rotation = 0;
    }
}
