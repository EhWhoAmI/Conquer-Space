package ConquerSpace.gui.renderers;

import ConquerSpace.game.universe.generators.TerrainGenerator;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.PlanetTypes;
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
        BufferedImage planetDisplaying = new BufferedImage(p.getPlanetSize() * 2, p.getPlanetSize(), BufferedImage.TYPE_3BYTE_BGR);
        //System.out.println(planetDisplaying);
        HashMap<Float, Color> colors = new HashMap<>();
        //Set planet tileset...
        //And composotion
        colors.put(-1f, new Color(69, 24, 4));
        colors.put(-0.25f, new Color(193, 68, 14));
        colors.put(0.25f, new Color(231, 125, 17));
        colors.put(0.75f, new Color(253, 166, 0));
        colors.put(0.9f, new Color(240, 231, 231));
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
