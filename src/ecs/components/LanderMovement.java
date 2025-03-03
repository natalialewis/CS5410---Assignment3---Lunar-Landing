package ecs.components;

public class LanderMovement extends Component{

    public enum Moves {
        UP,
        LEFT,
        RIGHT
    }

    private float velocityX;
    private float velocityY;
    private final float gravity;
    private final float thrust;

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

    public static void check(float check) {
        System.out.println(check);
    }


}
