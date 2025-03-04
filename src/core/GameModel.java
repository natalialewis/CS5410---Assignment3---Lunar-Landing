package core;

import ecs.entities.Background;
import ecs.entities.Entity;
import ecs.entities.Lander;
import ecs.entities.Terrain;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Texture;
import ecs.systems.*;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;


public class GameModel {
    private ecs.systems.BackgroundRenderer backgroundRenderer;
    private ecs.systems.TerrainRenderer terrainRenderer;
    private ecs.systems.LanderRenderer landerRenderer;
    private ecs.systems.Movement movement;
    private KeyboardInput inputKeyboard;

    public void initialize(Graphics2D graphics) {
        var texBackground = new Texture("resources/images/background.jpg");
        var texRocket = new Texture("resources/images/rocket.png");

        // Initialize keyboard
        inputKeyboard = new KeyboardInput(graphics.getWindow());

        // Initialize systems
        backgroundRenderer = new BackgroundRenderer(graphics);
        terrainRenderer = new TerrainRenderer(graphics);
        landerRenderer = new LanderRenderer(graphics);
        movement = new Movement(graphics, inputKeyboard);


        // Initialize entities
        backgroundRenderer.add(Background.create(texBackground));
        terrainRenderer.add(Terrain.create());
        initializeRocket(texRocket);

    }

    public void update(double elapsedTime) {
        backgroundRenderer.update(elapsedTime);
        terrainRenderer.update(elapsedTime);
        landerRenderer.update(elapsedTime);
        movement.update(elapsedTime);
    }

    private void findCenterOfRocket(float x, float y) {
        // Compute the center of the rocket based on the fact that a rocket is 0.07f x 0.07f
        Vector2f center = new Vector2f(x + 0.035f, y + 0.035f);
    }

    private void initializeRocket(Texture texRocket) {
        float initialX = -0.3f;
        float initialY = -0.45f;
        float initialAngle = (float) Math.PI * (0.0f / 2.0f);
        Vector2f center = new Vector2f(initialX + 0.035f, initialY + 0.035f);

        float initialVelocityX = 0.0f;
        float initialVelocityY = 0.0f;
        float gravity = 0.06f;
        float thrust = 0.15f;

        Entity entity = Lander.create(texRocket,initialX, initialY, initialAngle, center, initialVelocityX,
                initialVelocityY, gravity, thrust);

        landerRenderer.add(entity);
        movement.add(entity);
    }
}
