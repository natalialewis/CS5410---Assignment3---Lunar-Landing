package ecs.systems;

import ecs.components.*;
import ecs.components.EndGame;
import ecs.entities.Entity;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Collision extends System {

    public Collision() {
        super(ecs.components.Collision.class, TerrainPoints.class, LanderPosition.class,
                LanderAppearance.class, LanderMovement.class, EndGame.class);
    }

    @Override
    protected boolean isInterested(Entity entity) {
        return entity.contains(TerrainPoints.class)
                || entity.contains((Count.class)) ||
                entity.contains(EndGame.class) ||
                entity.contains(LanderAppearance.class)
                        && entity.contains(LanderPosition.class)
                        && entity.contains(ecs.components.Collision.class
                );
    }

    @Override
    public void update(double elapsedTime) {
        TerrainPoints terrainPoints = null;
        LanderPosition landerPosition = null;
        LanderAppearance landerAppearance = null;
        LanderMovement landerMovement = null;
        LanderFuel landerFuel = null;
        Count count = null;
        EndGame endGame = null;


        for (var entity: entities.values()) {

            if (entity.contains(TerrainPoints.class)) {
               terrainPoints = entity.get(TerrainPoints.class);
            } else if (entity.contains(LanderAppearance.class)) {
                landerPosition = entity.get(LanderPosition.class);
                landerAppearance = entity.get(LanderAppearance.class);
                landerMovement = entity.get(LanderMovement.class);
                landerFuel = entity.get(LanderFuel.class);
            } else if (entity.contains(Count.class)) {
                count = entity.get(Count.class);
            } else if (entity.contains(EndGame.class)) {
                endGame = entity.get(EndGame.class);
            }

            if (terrainPoints != null && landerPosition != null && landerAppearance != null && landerFuel != null &&
                    count != null && endGame != null) {
                hasIntersection(terrainPoints, landerPosition, landerAppearance, landerMovement, landerFuel, count, endGame, terrainPoints);

                // If moveable was turned off for a collision, restart it when the countdown is over
                if (!landerMovement.isMoveable() && !count.getCountDown() && !terrainPoints.isLevel1() && !endGame.isEndGame()) {
                    landerMovement.startMoving();
                }
            }
        }
    }

    private void hasIntersection(TerrainPoints terrainPointsComponent, LanderPosition landerPosition,
                                    LanderAppearance landerAppearance, LanderMovement landerMovement, LanderFuel landerFuel,
                                    Count count, EndGame endGame, TerrainPoints terrainPoints) {
        Vector2f rocketCenter = landerPosition.getCenter();
        float radiusX = landerAppearance.getWidth() / 2;
        float radiusY = landerAppearance.getHeight() / 2;

        for (ArrayList<Vector3f> line : terrainPointsComponent.getLines()) {
            // Start and end points of the line
            Vector3f point1 = line.get(0);
            Vector3f point2 = line.get(1);


            // Translate points to ellipse coordinates (rocket isn't square, so it is an ellipse instead of circle)
            Vector2f ellipsePoint1 = new Vector2f((point1.get(0) - rocketCenter.get(0)) / radiusX,
                    (point1.get(1) - rocketCenter.get(1)) / radiusY);

            Vector2f ellipsePoint2 = new Vector2f((point2.get(0) - rocketCenter.get(0)) / radiusX,
                    (point2.get(1) - rocketCenter.get(1)) / radiusY);

            if (lineEllipseIntersection(ellipsePoint2, ellipsePoint1)) {
                boolean safeZone = point1.get(1) == point2.get(1);

                handleCollision(safeZone, landerMovement, landerPosition, terrainPoints, count, landerFuel,
                        endGame);
            }

        }
    }

    private boolean lineEllipseIntersection(Vector2f point1, Vector2f point2) {
        // Get vector direction of the line
        float xDirection = point2.get(0) - point1.get(0);
        float yDirection = point2.get(1) - point1.get(1);

        // Solve the intersection of the line and the ellipse using the equations of the ellipse and the line
        float A = xDirection * xDirection + yDirection * yDirection;
        float B = 2 * (xDirection * point1.get(0) + yDirection * point1.get(1));
        float C = point1.get(0) * point1.get(0) + point1.get(1) * point1.get(1) - 1.0f;

        float discriminant = B * B - 4 * A * C;

        // If discriminant is 0, there is no intersection
        if (discriminant < 0) {
            return false;
        }

        // Check if the intersection points are within the segment
        discriminant = (float) Math.sqrt(discriminant);
        float t1 = (-B - discriminant) / (2 * A);
        float t2 = (-B + discriminant) / (2 * A);

        // Check if either t1 or t2 is between 0 and 1 (meaning the intersection is on the line segment)
        return (t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1);
    }

    private void handleCollision(boolean safeZone, LanderMovement landerMovement, LanderPosition landerPosition,
                                 TerrainPoints terrainPoints, Count count, LanderFuel landerFuel, EndGame endGame) {

        landerMovement.stopMoving();

        // Angle needs to be converted to degrees
        final float angleDegrees = Float.parseFloat(String.format("%.2f", (Math.toDegrees(landerPosition.getAngle()) + 360) % 360));

        // If safe landing, move to level 2 or give score if on level 2
        if ((angleDegrees <= 5.00f || angleDegrees >= 355.00f) &&
                landerPosition.getSpeed() <= 2.000f && safeZone) {

            // If on safe zone

            if (terrainPoints.isLevel1()) {
                terrainPoints.levelUp();

                // Show countdown
                count.setCountDown(true);

                // Generate new terrain
                terrainPoints.setNeedsGeneration(true);
                terrainPoints.resetTerrain();

                // Reset lander position
                landerPosition.setAngle((float) Math.PI * (3.0f / 2.0f));
                landerPosition.setCenter(new Vector2f(-0.2875f, -.4275f));
                landerPosition.setX(-0.3f);
                landerPosition.setY(-0.45f);
                landerMovement.setVelocityY(0.0f);
                landerMovement.setVelocityX(0.0f);

                // Reset fuel
                landerFuel.setFuel(20.00f);

            } else {
                // States that the rocket landed safely at the end of the game
                endGame.setSafeLanding(true);
                endGame.setEndGame(true);
            }
        } else {
            // Stop the game if the rocket lands or crashes in level two
            if (!terrainPoints.isLevel1()) {
                endGame.setEndGame(true);
            }
        }
    }
}
