package ConquerSpace.gui.renderers;

import ConquerSpace.game.universe.generators.TerrainGenerator;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.PlanetTypes;
import ConquerSpace.game.universe.spaceObjects.terrain.TerrainColoring;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 *
 * @author zyunl
 */
public class TerrainRenderer {

    private Planet p;

    public TerrainRenderer(Planet p) {
        this.p = p;

    }

    public Image getImage(double scale) {
        BufferedImage planetDisplaying = new BufferedImage(p.getPlanetSize() * 2, p.getPlanetSize(), BufferedImage.TYPE_INT_ARGB);
        //System.out.println(planetDisplaying);
        HashMap<Float, Color> colors = TerrainColoring.getTerrainColoring(p.getTerrainColoringIndex());
        TerrainGenerator terrainGenerator = new TerrainGenerator();
        Color[][] terrainColorses = terrainGenerator.generate(p.getTerrainSeed(), 6, 0.5f, 2.8f, 0.5f, p.getPlanetSize() * 2, p.getPlanetSize(), 0, p.getPlanetSize() / 3, 0, p.getPlanetSize() / 6, colors);

        if (p.getPlanetType() == PlanetTypes.ROCK) {
            for (int x = 0; x < p.getPlanetSize() * 2; x++) {
                for (int y = 0; y < p.getPlanetSize(); y++) {
                    //System.out.println(x + " " + y + ";" + p.terrain.terrainColor[x][y]);
                    //System.err.println(planetDisplaying);
                    planetDisplaying.setRGB(x, y,
                            terrainColorses[x][y].getRGB());
                }
            }
        }
        return ((planetDisplaying.getScaledInstance((int) (p.getPlanetSize() * 2 * scale), (int) (p.getPlanetSize() * scale), Image.SCALE_DEFAULT)));
    }
}
