package ecs.components;

public class LanderMovement extends Component{
    private float velocityX;
    private float velocityY;
    private final float gravity;

    public LanderMovement(float velocityX, float velocityY, float gravity) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.gravity = gravity;
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
}
