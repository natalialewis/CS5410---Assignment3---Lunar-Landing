package ecs.components;

public class EndGame extends Component{
    private boolean endGame = false;
    private boolean safeLanding;
    private float score = 0;

    public EndGame() {}

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
}
