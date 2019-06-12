package ConquerSpace.game.universe.resources;

import java.awt.Color;

public class Resource {

    private String name;
    private int value;
    private int id;
    private float rarity;
    private int difficulty;
    private boolean mineable;
    private float density;
    private int r;
    private int g;
    private int b;

    public Resource(String name, int value, float rarity, int id) {
        this.name = name;
        this.value = value;
        this.rarity = rarity;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public float getRarity() {
        return rarity;
    }

    public int getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRarity(float rarity) {
        this.rarity = rarity;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public float getDensity() {
        return density;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setMineable(boolean mineable) {
        this.mineable = mineable;
    }

    public void setColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color getColor() {
        return new Color(r, g, b);
    }

    public boolean isMineable() {
        return mineable;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Resource) {
            Resource res = (Resource) obj;
            return (res.name == name && res.id == id);
        }
        return false;
    }

}
