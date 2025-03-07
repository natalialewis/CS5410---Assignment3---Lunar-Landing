package core;

import ecs.entities.*;
import ecs.systems.EndGame;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Texture;
import ecs.systems.*;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private ecs.systems.BackgroundRenderer backgroundRenderer;
    private ecs.systems.TerrainRenderer terrainRenderer;
    private ecs.systems.LanderRenderer landerRenderer;
    private ecs.systems.Movement movement;
    private ecs.systems.Collision collision;
    private ecs.systems.Countdown countdown;
    private ecs.systems.EndGame endGame;
    private ecs.systems.ParticleSystem particleSystem;
    private KeyboardInput inputKeyboard;
    private final List<Entity> removeThese = new ArrayList<>();
    private final List<Entity> addThese = new ArrayList<>();
    private final MyRandom random = new MyRandom();

    public void initialize(Graphics2D graphics) {
        var texBackground = new Texture("resources/images/background.jpg");
        var texRocket = new Texture("resources/images/rocket.png");
        var fontHud = new Font("resources/fonts/Roboto-Regular.ttf", 20, false);
        var fontCount = new Font("resources/fonts/Roboto-Regular.ttf", 100, false);
        var texCrashOrb = new Texture("resources/images/crash-orb.png");
        var texThrustOrb = new Texture("resources/images/thrust-orb.png");

        // Initialize keyboard
        inputKeyboard = new KeyboardInput(graphics.getWindow());

        // Initialize systems
        backgroundRenderer = new BackgroundRenderer(graphics);
        terrainRenderer = new TerrainRenderer(graphics);
        landerRenderer = new LanderRenderer(graphics);
        movement = new Movement(inputKeyboard);
        collision = new Collision();
        countdown = new Countdown(graphics);
        endGame = new EndGame(graphics);
        particleSystem = new ParticleSystem(graphics, texCrashOrb, texThrustOrb);

//        particleSystem = new ParticleSystem((Entity entity) -> {
//            removeThese.add(entity);
//            addThese.add(createParticle());
//        });


        // Initialize entities
        backgroundRenderer.add(Background.create(texBackground));
        initializeRocket(texRocket, fontHud);
        // Separated out the creation because both of these systems need the same instance of terrain
        Entity terrain = Terrain.create();
        terrainRenderer.add(terrain);
        collision.add(terrain);
        Entity counter = Counter.create(true, fontCount);
        countdown.add(counter);
        collision.add(counter);
        movement.add(counter);
        Entity endgame = ecs.entities.EndGame.create(fontCount);
        collision.add(endgame);
        endGame.add(endgame);
        movement.add(endgame);

        Entity particle = createParticle();
        particleSystem.add(particle);
    }

    public void update(double elapsedTime) {
        backgroundRenderer.update(elapsedTime);
        terrainRenderer.update(elapsedTime);
        landerRenderer.update(elapsedTime);
        movement.update(elapsedTime);
        collision.update(elapsedTime);
        countdown.update(elapsedTime);
        endGame.update(elapsedTime);

//        // Manages the particles
//        for (var entity : removeThese) {
//            particleSystem.remove(entity.getId());
//        }
//        removeThese.clear();
//
//        for (var entity : addThese) {
//            particleSystem.add(entity);
//        }
//        addThese.clear();
    }

//    private Entity createParticle() {
//        float size = (float) this.random.nextGaussian(this.sizeMean, this.sizeStdDev);
//        var p = new Particle(
//                new Vector2f(this.center.x, this.center.y),
//                this.random.nextCircleVector(),
//                (float) this.random.nextGaussian(this.speedMean, this.speedStdDev),
//                new Vector2f(size, size),
//                this.random.nextGaussian(this.lifetimeMean, this.lifetimeStdDev));
//
//        return p;
//    }

    private Entity createParticle() {
        return ParticleEmitters.create(
                new Vector2f(0, 0),
                0.015f, 0.004f,
                0.07f, 0.05f,
                3, 1);
    }

    private void initializeRocket(Texture texRocket, Font font) {
        float initialX = -0.3f;
        float initialY = -0.45f;
        float initialAngle = (float) Math.PI * (3.0f / 2.0f);
        Vector2f center = new Vector2f(initialX + 0.0125f, initialY + 0.0225f);

        float initialVelocityX = 0.0f;
        float initialVelocityY = 0.0f;
        float gravity = 0.020f;
        float thrust = 0.025f;
        float fuel = 20.00f;
        float width = 0.025f;
        float height = 0.045f;

        Entity entity = Lander.create(texRocket,initialX, initialY, initialAngle, center, initialVelocityX,
                initialVelocityY, gravity, thrust, font, fuel, width, height);

        landerRenderer.add(entity);
        movement.add(entity);
        collision.add(entity);
    }
}
