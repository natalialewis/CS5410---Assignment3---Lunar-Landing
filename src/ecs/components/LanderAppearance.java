package ecs.components;

import edu.usu.graphics.Font;
import edu.usu.graphics.Texture;

public class LanderAppearance extends Component {
    private final Texture image;
    private final Font font;

    public LanderAppearance(Texture image, Font font) {
        this.image = image;
        this.font = font;
    }

    public Texture getImage() {
        return image;
    }

    public Font getFont() {
        return font;
    }
}
