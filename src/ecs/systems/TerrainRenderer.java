package ecs.systems;

import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import edu.usu.graphics.Triangle;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TerrainRenderer {
    private final Graphics2D graphics;

    private final Random random = new Random();
    private final ArrayList<ArrayList<Float>> terrainPoints = new ArrayList<>();

    // Polygon y cord at bottom of the game screen
    private final float BOTTOM_CORD = 0.5f;
    // X cord of the start point
    private final float START_CORD = -0.5f;
    // X cord of the end point
    private final float END_CORD = 0.5f;

    private float surfaceRoughness = 1.0f;

    public TerrainRenderer(Graphics2D graphics) {
        this.graphics = graphics;
        generateTerrain();
    }

    private void generateTerrain() {
        // Initialize terrain with two endpoints
        terrainPoints.add(new ArrayList<>(Arrays.asList(START_CORD, getRandomGaussian())));
        terrainPoints.add(new ArrayList<>(Arrays.asList(END_CORD, getRandomGaussian())));

        // Iterate midpoint displacement algorithm 5 times
        for (int i = 0; i < 7; i++) {
            ArrayList<ArrayList<Float>> newPoints = new ArrayList<>();

            // For each point in terrainPoints, compute new elevation and add new point
            for (int j = 0; j < terrainPoints.size() - 1; j++) {
                ArrayList<Float> point1 = terrainPoints.get(j);
                ArrayList<Float> point2 = terrainPoints.get(j + 1);

                float newX = (point1.get(0) + point2.get(0)) / 2;
                float newY = computeNewElevation(point1, point2);

                newPoints.add(new ArrayList<>(Arrays.asList(newX, newY)));
            }

            // Merge the new points into terrainPoints in increasing x coordinate order
            for (int j = 0; j < newPoints.size(); j++) {
                terrainPoints.add(2 * j + 1, newPoints.get(j));
            }
        }

        // Makes points fit in my game size
        scaleYValues();
    }

    private float computeNewElevation(ArrayList<Float> point1, ArrayList<Float> point2) {
        float y = (point1.get(1) + point2.get(1)) / 2;
        float r = surfaceRoughness * (float) random.nextGaussian() * Math.abs(point2.get(0) - point1.get(0));

        // Makes surface a little bit smoother as the length between points gets smaller
        surfaceRoughness *= 0.995f;

        return y + r;
    }

    private void scaleYValues() {
        // Find the min and max Y values from the terrain points
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for (ArrayList<Float> point : terrainPoints) {
            float y = point.get(1);

            // Update min and max Y values
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        for (ArrayList<Float> point : terrainPoints) {
            float y = point.get(1);

            // Scale the Y value to fit between 0.0f and 0.5f (half of my game size)
            float scaledY = (y - minY) / (maxY - minY) * 0.5f;

            // Set the scaled Y value back to the point
            point.set(1, scaledY);
        }
    }

    public void update(double elapsedTime) {
        Color terrainColor = new Color(98/255f, 46/255f, 140/255f);

        // Create left triangles (hypotenuse from bottom to top right)
        for (int i = 0; i < terrainPoints.size() - 1; i++) {
            ArrayList<Float> point1 = terrainPoints.get(i);
            ArrayList<Float> point2 = terrainPoints.get(i + 1);

            // Create points for the triangle
            Vector3f topLeft = new Vector3f(point1.get(0), point1.get(1), 0.0f);
            Vector3f topRight = new Vector3f(point2.get(0), point2.get(1), 0.0f);
            Vector3f bottomLeft = new Vector3f(point1.get(0), BOTTOM_CORD, 0.0f);

            // Create triangle
            Triangle triangle = new Triangle(topLeft, topRight, bottomLeft);

            // Draw the triangle
            graphics.draw(triangle, terrainColor);
        }

        // Create right triangles (hypotenuse from bottom to top left)
        for (int i = 0; i < terrainPoints.size() - 1; i++) {
            ArrayList<Float> point1 = terrainPoints.get(i);
            ArrayList<Float> point2 = terrainPoints.get(i + 1);

            // Create points for the triangle
            Vector3f bottomLeft = new Vector3f(point1.get(0), BOTTOM_CORD, 0.0f);
            Vector3f topRight = new Vector3f(point2.get(0), point2.get(1), 0.0f);
            Vector3f bottomRight = new Vector3f(point2.get(0), BOTTOM_CORD, 0.0f);

            // Create triangle
            Triangle triangle = new Triangle(bottomLeft, topRight, bottomRight);

            // Draw the triangle
            graphics.draw(triangle, terrainColor);
        }

        // Draw outline of terrain
        for (int i = 0; i < terrainPoints.size() - 1; i++) {
            ArrayList<Float> point1 = terrainPoints.get(i);
            ArrayList<Float> point2 = terrainPoints.get(i + 1);

            // Create points for the rectangle
            Vector3f left = new Vector3f(point1.get(0), point1.get(1) - 0.002f, 0.0f);
            Vector3f right = new Vector3f(point2.get(0), point2.get(1) - 0.002f, 0.0f);

            // Draw the rectangle
            graphics.draw(left, right, Color.WHITE);
        }
    }

    private float getRandomGaussian() {
        double gaussian = random.nextGaussian();

        /*
            Most of the Gaussian values are between -3.0 and 3.0 (bell curve), so I divided the Gaussian value by 6.0
            to get an approximate range of -0.5 to 0.5. Then I added 0.25 to shift the range to -0.25 to 0.75. Then I
            just have an if statement to get rid of the values that are less than 0.0 or greater than 0.5.
         */
        double scaledGaussian = (gaussian / 6.0) + 0.25;

        if (scaledGaussian < 0.0) {
            scaledGaussian = 0.0;
        } else if (scaledGaussian > 1.0) {
            scaledGaussian = 1.0;
        }

        return (float) scaledGaussian;
    }

}
