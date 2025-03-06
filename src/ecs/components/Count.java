package ecs.components;

import edu.usu.graphics.Font;

public class Count extends Component {

    private boolean countDown;
    private float time;
    private int count = 3;
    private final Font font;

    public Count(boolean count, Font font) {
        this.countDown = count;
        this.font = font;
    }

    public boolean getCountDown() {
        return this.countDown;
    }

    public void setCountDown(boolean state) {
        this.countDown = state;
    }

    public float getTime() {
        return this.time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Font getFont() {
        return this.font;
    }
}
