package ecs.components;

public class LanderMovement extends Component{

    private float velocityX;
    private float velocityY;
    private final float gravity;
    private final float thrust;
    private boolean moveable = true;

    public LanderMovement(float velocityX, float velocityY, float gravity, float thrust) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.gravity = gravity;
        this.thrust = thrust;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public float getVelocityY() {
        return this.velocityY;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityX() {
        return this.velocityX;
    }

    public float getGravity() {
        return this.gravity;
    }

    public float getThrust() {
        return this.thrust;
    }

    public void stopMoving() {
        this.moveable = false;
    }

    public void startMoving() {
        this.moveable = true;
    }

    public boolean isMoveable() {
        return this.moveable;
    }
}
