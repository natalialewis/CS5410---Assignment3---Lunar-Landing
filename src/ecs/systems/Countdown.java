package ecs.systems;

import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import ecs.components.Count;

public class Countdown extends System {

    private final Graphics2D graphics;

    public Countdown(Graphics2D graphics) {
        super(ecs.components.Count.class);

        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {
        for (var entity: entities.values()) {
            Count count = entity.get(ecs.components.Count.class);

            if (count.getCountDown()) {
                count.setTime(count.getTime() + (float) elapsedTime);

                render(count.getCount(), count);

                if (count.getTime() >= 1.00) {
                    count.setCount(count.getCount() + 1);
                    count.setTime(0.00f);

                    if (count.getCount() > 3) {
                        count.setCountDown(false);
                        count.setCount(0);
                    }
                }
            }
        }
    }

    public void render(int currCount, Count count) {
        float height = 0.175f;
        final Color color = new Color(205/255f, 83/255f, 201/255f);

        graphics.drawTextByHeight(count.getFont(), String.valueOf(currCount), -0.05f, -0.3375f, height,
                1.0f, Color.WHITE);
    }
}


