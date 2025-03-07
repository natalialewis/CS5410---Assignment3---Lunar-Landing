package ecs.components;

import edu.usu.graphics.Font;

public class EndGame extends Component{
    private boolean endGame = false;
    private boolean safeLanding;
    private float score = 0;
    private float fuelLeft = 0;
    private final Font font;
    private int level;

    public EndGame(Font font) {
        this.font = font;
    }

    public boolean isEndGame() {
        return this.endGame;
    }

    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    public float getScore() {
        return this.score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isSafeLanding() {
        return this.safeLanding;
    }

    public void setSafeLanding(boolean safeLanding) {
        this.safeLanding = safeLanding;
    }

    public float getFuelLeft() {
        return this.fuelLeft;
    }

    public void setFuelLeft(float fuelLeft) {
        this.fuelLeft = fuelLeft;
    }

    public Font getFont() {
        return this.font;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
