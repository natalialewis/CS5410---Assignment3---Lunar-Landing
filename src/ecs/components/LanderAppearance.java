package ecs.components;

import edu.usu.graphics.Texture;

public class LanderAppearance extends Component {
    private final Texture image;

    public LanderAppearance(Texture image) {
        this.image = image;
    }

    public Texture getImage() {
        return image;
    }
}
