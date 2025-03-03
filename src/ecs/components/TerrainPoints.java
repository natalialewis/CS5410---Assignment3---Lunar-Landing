package ecs.components;

import java.util.ArrayList;
import java.util.Random;

public class TerrainPoints extends Component {
    public ArrayList<ArrayList<Float>> terrainPoints = new ArrayList<>();
    public ArrayList<ArrayList<Float>> terrainFinal = new ArrayList<>();

    public float surfaceRoughness = 1.0f;  // This changes over time

    public void add(ArrayList<Float> newPoint) {
        terrainPoints.add(newPoint);
    }

    public ArrayList<Float> get(int index) {
        return terrainPoints.get(index);
    }

    public ArrayList<ArrayList<Float>> getTerrainPoints() {
        return terrainPoints;
    }

    public int size() {
        return terrainPoints.size();

    }

    public void insert(int index, ArrayList<Float> point) {
        terrainPoints.add(index, point);
    }

    public ArrayList<ArrayList<Float>> getTerrainFinal() {
        return terrainFinal;
    }

    public void setTerrainFinal(ArrayList<ArrayList<Float>> terrainFinal) {
        this.terrainFinal = terrainFinal;
    }
}
