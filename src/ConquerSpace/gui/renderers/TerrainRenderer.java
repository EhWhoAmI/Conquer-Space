/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.gui.renderers;

import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.PlanetTypes;
import ConquerSpace.game.universe.terrain.TerrainColoring;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public class TerrainRenderer {

    private Planet p;

    public TerrainRenderer(Planet p) {
        this.p = p;

    }

    public Image getImage(double scale) {
        BufferedImage planetDisplaying = new BufferedImage(p.getPlanetSize() * 2, p.getPlanetSize(), BufferedImage.TYPE_INT_ARGB);
        //System.out.println(planetDisplaying);
        HashMap<Float, Color> colors;// = TerrainColoring.getRockyTerrainColoring(p.getTerrainColoringIndex());
        TerrainGenerator terrainGenerator = new TerrainGenerator();
        Color[][] terrainColorses = null;

        if (p.getPlanetType() == PlanetTypes.ROCK) {
            colors = TerrainColoring.getRockyTerrainColoring(p.getTerrainColoringIndex());
            terrainColorses = terrainGenerator.generate(p.getTerrainSeed(), 6, 0.5f, 2.8f, 0.5f, p.getPlanetSize() * 2, p.getPlanetSize(), 0, p.getPlanetSize() / 3, 0, p.getPlanetSize() / 6, colors);
        } else if (p.getPlanetType() == PlanetTypes.GAS) {
            colors = TerrainColoring.getGassyTerrainColoring(p.getTerrainColoringIndex());

            terrainColorses = terrainGenerator.generate(p.getTerrainSeed(), 6, 0.5f, 2.8f, 0.5f, p.getPlanetSize() * 2, p.getPlanetSize(), 0, p.getPlanetSize() / 18, 0, p.getPlanetSize() / 6, colors);
        }
        
        for (int x = 0; x < p.getPlanetSize() * 2; x++) {
            for (int y = 0; y < p.getPlanetSize(); y++) {
                //System.out.println(x + " " + y + ";" + p.terrain.terrainColor[x][y]);
                //System.err.println(planetDisplaying);
                planetDisplaying.setRGB(x, y,
                        terrainColorses[x][y].getRGB());
            }
        }
        if (terrainColorses != null) {
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
