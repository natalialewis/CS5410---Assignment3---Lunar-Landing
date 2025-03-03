package ecs.components;

import org.joml.Vector2f;

public class LanderPosition extends Component{
    public float x;
    public float y;
    public float angle;
    public Vector2f center;

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
        System.out.println("X: " + x);
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
}
