package ecs.systems;

import ecs.components.TerrainPoints;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Triangle;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TerrainRenderer extends System{
    private final Graphics2D graphics;

    private final Random random = new Random();

    public TerrainRenderer(Graphics2D graphics) {
        super(ecs.components.TerrainPoints.class);
        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {
        for (var entity : entities.values()) {
            TerrainPoints terrainPointsComponent = entity.get(ecs.components.TerrainPoints.class);

            // Generate terrain if it hasn't been generated yet
            if (terrainPointsComponent.getTerrainFinal().isEmpty()) {
                generateTerrain(terrainPointsComponent);
            }

            // Render the finished terrain
            Color terrainColor = new Color(98/255f, 46/255f, 140/255f);

            // Y cord at bottom of the game screen
            float BOTTOM_CORD = 0.5f;

            // Create left triangles (hypotenuse from bottom to top right)
            for (int i = 0; i < terrainPointsComponent.size() - 1; i++) {
                ArrayList<Float> point1 = terrainPointsComponent.get(i);
                ArrayList<Float> point2 = terrainPointsComponent.get(i + 1);

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
            for (int i = 0; i < terrainPointsComponent.size() - 1; i++) {
                ArrayList<Float> point1 = terrainPointsComponent.get(i);
                ArrayList<Float> point2 = terrainPointsComponent.get(i + 1);

                // Create points for the triangle
                Vector3f bottomLeft = new Vector3f(point1.getFirst(), BOTTOM_CORD, 0.0f);
                Vector3f topRight = new Vector3f(point2.get(0), point2.get(1), 0.0f);
                Vector3f bottomRight = new Vector3f(point2.get(0), BOTTOM_CORD, 0.0f);

                // Create triangle
                Triangle triangle = new Triangle(bottomLeft, topRight, bottomRight);

                // Draw the triangle
                graphics.draw(triangle, terrainColor);
            }

            // Draw outline of terrain
            for (ArrayList<Vector3f> line : terrainPointsComponent.getLines()) {

                graphics.draw(line.get(0), line.get(1), Color.WHITE);
            }
        }
    }

    private void generateTerrain(TerrainPoints terrainPointsComponent) {
        // Initialize terrain with two endpoints

        // X cord of the start point
        float START_CORD = -0.5f;

        // X cord of the end point
        float END_CORD = 0.5f;

        terrainPointsComponent.add(new ArrayList<>(Arrays.asList(START_CORD, getRandomGaussian())));
        terrainPointsComponent.add(new ArrayList<>(Arrays.asList(END_CORD, getRandomGaussian())));

        // Iterate midpoint displacement algorithm 5 times
        for (int i = 0; i < 7; i++) {
            ArrayList<ArrayList<Float>> newPoints = new ArrayList<>();

            // For each point in terrainPoints, compute new elevation and add new point
            for (int j = 0; j < terrainPointsComponent.size() - 1; j++) {
                ArrayList<Float> point1 = terrainPointsComponent.get(j);
                ArrayList<Float> point2 = terrainPointsComponent.get(j + 1);

                float newX = (point1.getFirst() + point2.getFirst()) / 2;
                float newY = computeNewElevation(point1, point2, terrainPointsComponent);

                newPoints.add(new ArrayList<>(Arrays.asList(newX, newY)));
            }

            // Merge the new points into terrainPoints in increasing x coordinate order
            for (int j = 0; j < newPoints.size(); j++) {
                terrainPointsComponent.insert(2 * j + 1, newPoints.get(j));
            }
        }

        // Makes points fit in my game size
        scaleYValues(terrainPointsComponent);

        // Adds safe zone
        addSafeZone(terrainPointsComponent);

        terrainPointsComponent.setTerrainFinal(terrainPointsComponent.getTerrainPoints());

        // Add outline of terrain
        for (int i = 0; i < terrainPointsComponent.size() - 1; i++) {
            ArrayList<Float> point1 = terrainPointsComponent.get(i);
            ArrayList<Float> point2 = terrainPointsComponent.get(i + 1);

            // Create points for the rectangle (Move up 0.001f to make sure purple triangles don't look rough)
            Vector3f left = new Vector3f(point1.get(0), point1.get(1) - 0.001f, 1.0f);
            Vector3f right = new Vector3f(point2.get(0), point2.get(1) - 0.001f, 1.0f);

            terrainPointsComponent.addLine(new ArrayList<>(Arrays.asList(left, right)));
        }
    }

    private float computeNewElevation(ArrayList<Float> point1, ArrayList<Float> point2, TerrainPoints terrainPointsComponent) {
        float y = (point1.get(1) + point2.get(1)) / 2;
        float r = terrainPointsComponent.getSurfaceRoughness() * (float) random.nextGaussian() * Math.abs(point2.get(0) - point1.get(0));

        // Makes surface a little bit smoother as the length between points gets smaller
        terrainPointsComponent.setSurfaceRoughness(terrainPointsComponent.getSurfaceRoughness() * 0.995f);

        return y + r;
    }

    private void addSafeZone(TerrainPoints terrainPointsComponent) {
        // List of points
        ArrayList<ArrayList<Float>> points = terrainPointsComponent.getTerrainPoints();

        // Random starting coordinate
        float startX = getSafeStartCord();

        // Ending coordinates with the ending y being the same as the starting y (level 2 has smaller safe zone)
        float endX = startX + 0.1f;
        if (!terrainPointsComponent.isLevel1()) {
            endX = startX + 0.06f;
        }

        replaceY(points, startX, endX);


        // Implements a second safe zone if player is on level 1
        if (terrainPointsComponent.isLevel1()) {
            // Second safe zone starting coordinate
            boolean secondConflictsWithFirst = true;
            float startX2 = startX;

            while (secondConflictsWithFirst) {
                float start = getSafeStartCord();

                if (start > startX + 0.1f || start + 0.1f < startX) {
                    secondConflictsWithFirst = false;
                    startX2 = start;
                }
            }
            float endX2 = startX2 + 0.1f;

            replaceY(points, startX2, endX2);
        }
    }

    private float getSafeStartCord() {
        float startX = -0.5f;

        // Makes sure the starting point is within 15% of the edge
        while(!(startX > -0.35f && startX < 0.25f)) {
            startX = (float) random.nextGaussian();
        }
        return startX;
    }

    private void replaceY(ArrayList<ArrayList<Float>> points, float startCord, float endCord) {
        // Replace the y values of the points within the safe zone
        float elevation = 1.0f;

        for (ArrayList<Float> point : points) {
            float x = point.get(0);
            if (x >= startCord && x <= endCord) {
                if (elevation == 1.0f) {
                    elevation = point.get(1);
                }
                point.set(1, elevation);
            }
        }
    }

    private void scaleYValues(TerrainPoints terrainPointsComponent) {
        // Find the min and max Y values from the terrain points
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for (ArrayList<Float> point : terrainPointsComponent.getTerrainPoints()) {
            float y = point.get(1);

            // Update min and max Y values
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        for (ArrayList<Float> point : terrainPointsComponent.getTerrainPoints()) {
            float y = point.get(1);

            // Scale the Y value to fit between 0.0f and 0.5f (half of my game size)
            float scaledY = (y - minY) / (maxY - minY) * 0.5f;

            // Set the scaled Y value back to the point
            point.set(1, scaledY);
        }
    }

    private float getRandomGaussian() {
        double gaussian = random.nextGaussian();

        /*
            Most of the Gaussian values are between -3.0 and 3.0 (bell curve), so I divided the Gaussian value by 6.0
            to get an approximate range of -0.5 to 0.5. Then I added 0.15 to shift the range to -0.35 to 0.65. Then I
            just have an if statement to get rid of the values that are less than 0.0 or greater than 0.5.
         */
        double scaledGaussian = (gaussian / 6.0) + 0.15;

        if (scaledGaussian < 0.0) {
            scaledGaussian = 0.0;
        } else if (scaledGaussian > 0.5) {
            scaledGaussian = 0.5;
        }

        return (float) scaledGaussian;
    }

}
