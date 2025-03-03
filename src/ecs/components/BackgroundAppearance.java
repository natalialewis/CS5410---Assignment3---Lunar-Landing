package ecs.components;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

public class BackgroundAppearance extends Component{
    public Texture image;
    public Color borderColor;

    public BackgroundAppearance(Texture image, Color borderColor) {
        this.image = image;
        this.borderColor = borderColor;
    }
}
