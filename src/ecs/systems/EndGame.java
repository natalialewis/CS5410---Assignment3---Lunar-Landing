package ecs.systems;

import com.google.gson.reflect.TypeToken;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EndGame extends System implements Runnable {

    private enum Activity {
        Nothing,
        Load,
        Save
    }

    private boolean savedScore = false;
    private boolean done = false;
    private static final Lock lockSignal = new ReentrantLock();
    private static final Condition doSomething = lockSignal.newCondition();
    private static Activity doThis = Activity.Nothing;
    private final Thread tInternal;
    private final Graphics2D graphics;
    ecs.components.EndGame endGame;
    List<String> highScores;

    public EndGame(Graphics2D graphics) {
        super(ecs.components.EndGame.class);

        this.graphics = graphics;
        this.tInternal = new Thread(this);
        tInternal.start();

    }

    @Override
    public void run() {
        try {
            while (!done) {
                // Wait for a signal to do something
                lockSignal.lock();
                doSomething.await();
                lockSignal.unlock();

                // Based on what was requested, do something
                switch (doThis) {
                    case Activity.Nothing -> {}
                    case Activity.Save -> saveSomething();
                    case Activity.Load -> loadHighScores();
                }
            }
        } catch (Exception ex) {
            java.lang.System.out.printf("Something bad happened: %s\n", ex.getMessage());
        }
    }

    @Override
    public void update(double elapsedTime) {

        for (var entity: entities.values()) {
           endGame = entity.get(ecs.components.EndGame.class);

            if (endGame.isEndGame() && endGame.getScore() == 0.0f) {
                float score = getScore(endGame.isSafeLanding(), endGame.getFuelLeft(), endGame.getLevel());
                endGame.setScore(score);
            }

            if (endGame.isEndGame()) {
                renderScore(endGame);

                if (!savedScore) {
                    lockSignal.lock();
                    doThis = Activity.Save;
                    doSomething.signal();
                    lockSignal.unlock();
                    savedScore = true;
                }
            }
        }
    }

    public float getScore(boolean safeLanding, float fuelLeft, int level) {
        if (safeLanding) {
            return 30 + (fuelLeft * level);
        } else {
            return 10 + (fuelLeft * level / 3);
        }
    }

    public void renderScore(ecs.components.EndGame endGame) {
        Color color = new Color(205/255f, 83/255f, 201/255f);

        graphics.drawTextByHeight(endGame.getFont(), String.format("Score: %.0f", endGame.getScore()), 0.29f, -0.46f, 0.04f, 1.0f, color);
    }

    private synchronized void saveSomething() {

        // Loads existing high scores
        loadHighScores();

        // Adds the new score
        highScores.add(String.format("%.0f", endGame.getScore()));

        // Sorts the high scores to see which one to get rid of
        highScores.sort(Collections.reverseOrder());

        if (highScores.size() > 5) {
            highScores.remove(5);
        }

        // Saves highscores to the file
        try (FileWriter writer = new FileWriter("highscores.json")) {
            Gson gson = new Gson();
            gson.toJson(highScores, writer);
        } catch (Exception ex) {
            java.lang.System.out.println("Error saving high scores: " + ex.getMessage());
        }
    }

    private synchronized void loadHighScores() {
        // Loads the high scores from the file
        try (FileReader reader = new FileReader("highscores.json")) {
            Gson gson = new Gson();
            highScores = gson.fromJson(reader,new TypeToken<List<String>>(){}.getType());
        } catch (Exception ex) {
            java.lang.System.out.println("Error loading: " + ex.getMessage());
        }
    }

    public void shutdown() {
        try {
            lockSignal.lock();

            doThis = Activity.Nothing;
            done = true;
            doSomething.signal();

            lockSignal.unlock();

            tInternal.join();
        } catch (Exception ex) {
            java.lang.System.out.printf("Failure to gracefully shutdown thread: %s\n", ex.getMessage());
        }
    }
}
