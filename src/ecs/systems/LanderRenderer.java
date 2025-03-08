package ecs.systems;

import ecs.components.LanderAppearance;
import ecs.components.LanderFuel;
import ecs.components.LanderMovement;
import ecs.components.LanderPosition;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import views.MainMenuView;

public class LanderRenderer extends System {

    private final Graphics2D graphics;

    // Initializes the graphics and texture for the background
    public LanderRenderer(Graphics2D graphics) {
        super(ecs.components.LanderAppearance.class, ecs.components.LanderPosition.class);
        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {

        for (var entity: entities.values()) {
            LanderPosition landerPosition = entity.get(ecs.components.LanderPosition.class);
            LanderAppearance landerAppearance = entity.get(ecs.components.LanderAppearance.class);
            LanderMovement movement = entity.get(LanderMovement.class);
            LanderFuel fuel = entity.get(LanderFuel.class);


            if (landerAppearance.isShowLander()) {
                // Rocket
                Rectangle rocket = new Rectangle(landerPosition.getX(), landerPosition.getY(), landerAppearance.getWidth(),
                        landerAppearance.getHeight(), 0.99f);
                graphics.draw(entity.get(LanderAppearance.class).getImage(), rocket, landerPosition.getAngle(),
                        landerPosition.getCenter(), Color.WHITE);
            }

            // Hud
            final float fuelLeft = fuel.getFuel();
            final float angleDegrees = Float.parseFloat(String.format("%.2f", (Math.toDegrees(landerPosition.getAngle()) + 360) % 360));
            landerPosition.setSpeed(Float.parseFloat(String.format("%.3f", movement.getVelocityY() * 50)));

            final float HUD_Height = 0.028f;
            float left = 0.28f;
            float top = -0.4f;

            top = renderHUDItem(landerAppearance.getFont(), "fuel      :  " + fuelLeft + " s", left, top, HUD_Height, fuelLeft >= 0.00f ? Color.GREEN : Color.WHITE);
            top = renderHUDItem(landerAppearance.getFont(), "speed  :  " + landerPosition.getSpeed() + " m/s", left, top, HUD_Height, landerPosition.getSpeed() <= 2.000f ? Color.GREEN : Color.WHITE);
            renderHUDItem(landerAppearance.getFont(), "angle   :  " + angleDegrees + "°", left, top, HUD_Height, angleDegrees >= 355.00f || angleDegrees <= 5.00f ? Color.GREEN : Color.WHITE);
        }
    }

    private float renderHUDItem(Font font, String text, float left, float top, float height, Color color) {
        float width = font.measureTextWidth(text, height);
        graphics.drawTextByHeight(font, text, left, top, height, 1.0f, color);

        return top + height;
    }
}
