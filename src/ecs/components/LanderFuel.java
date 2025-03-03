package ecs.components;

public class LanderFuel extends Component {
    private float fuel;

    public LanderFuel(float fuel) {
        this.fuel = fuel;
    }

    public float getFuel() {
        return this.fuel;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }
}
