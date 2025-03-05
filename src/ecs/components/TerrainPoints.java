package ecs.components;

import org.joml.Vector3f;

import java.util.ArrayList;

public class TerrainPoints extends Component {
    private ArrayList<ArrayList<Float>> terrainPoints;
    private ArrayList<ArrayList<Float>> terrainFinal;
    private ArrayList<ArrayList<Vector3f>> lines;
    private float surfaceRoughness = 1.0f;
    private boolean level1;

    public TerrainPoints() {
        this.terrainPoints = new ArrayList<>();
        this.terrainFinal = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.level1 = true;
    }

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

    public float getSurfaceRoughness() {
        return surfaceRoughness;
    }

    public void setSurfaceRoughness(float surfaceRoughness) {
        this.surfaceRoughness = surfaceRoughness;
    }

    public void levelUp() {
        level1 = false;
    }

    public boolean isLevel1() {
        return level1;
    }

    public void addLine(ArrayList<Vector3f> line) {
        lines.add(line);
    }

    public ArrayList<ArrayList<Vector3f>> getLines() {
        return lines;
    }
}
