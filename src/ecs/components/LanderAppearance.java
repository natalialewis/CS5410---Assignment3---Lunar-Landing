package ecs.components;

import edu.usu.graphics.Font;
import edu.usu.graphics.Texture;

public class LanderAppearance extends Component {
    private final Texture image;
    private final Font font;
    private final float width;
    private final float height;

    public LanderAppearance(Texture image, Font font, float width, float height) {
        this.image = image;
        this.font = font;
        this.width = width;
        this.height = height;
    }

    public Texture getImage() {
        return image;
    }

    public Font getFont() {
        return font;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
