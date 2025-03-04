package ecs.components;

import org.joml.Vector2f;

public class LanderPosition extends Component{
    private float x;
    private float y;
    private final float angle;
    private Vector2f center;

    public LanderPosition(float x, float y, float angle, Vector2f center) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.center = center;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public Vector2f getCenter() {
        return center;
    }

    public void setCenter(Vector2f center) {
        this.center = center;
    }
}
