package ConquerSpace.game.universe.spaceObjects.terrain;

import java.awt.Color;

/**
 *
 * @author zyunl
 */
public class Terrain {

    public TerrainTile[][] terrainColor;
    private int height;
    private int width;

    public Terrain() {
    }

    public Terrain(int width, int height, int layers) {
        terrainColor = new TerrainTile[width][height];
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
