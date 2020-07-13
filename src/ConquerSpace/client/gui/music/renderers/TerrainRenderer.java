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
package ConquerSpace.client.gui.music.renderers;

import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.PlanetTypes;
import ConquerSpace.common.game.universe.bodies.terrain.TerrainColoring;
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

    public Image getImage() {
        BufferedImage planetDisplaying;
        //System.out.println(planetDisplaying);
        HashMap<Float, Color> colors;// = TerrainColoring.getRockyTerrainColoring(p.getTerrainColoringIndex());
        TerrainGenerator terrainGenerator = new TerrainGenerator();
        ConquerSpace.common.game.jLibNoise.noise.utils.Image terrainColorses = null;
        
        int imageRes = 2;
        int mapRes = 2;
        switch (p.getPlanetType()) {
            case PlanetTypes.ROCK:
                colors = TerrainColoring.getRockyTerrainColoring(p.getTerrainColoringIndex());
                terrainColorses = terrainGenerator.generateImage(p.getTerrainSeed(),
                        7, 0.9f, 2f, 0.5f, p.getPlanetWidth() * imageRes * 2, p.getPlanetHeight() * imageRes * 2,
                        -p.getPlanetWidth() / (16*mapRes), 
                        p.getPlanetWidth() / (16*mapRes), 
                        -p.getPlanetHeight() / (16*mapRes), 
                        p.getPlanetHeight() / (16*mapRes), 
                        colors);
                break;
            default:
            case PlanetTypes.GAS:
                colors = TerrainColoring.getGassyTerrainColoring(p.getTerrainColoringIndex());
                terrainColorses = terrainGenerator.generateImage(p.getTerrainSeed(),
                        7, 0.5f, 2f, 0.5f, p.getPlanetWidth() * imageRes, p.getPlanetHeight() * imageRes,
                        0, p.getPlanetWidth() / (8*mapRes), 0, p.getPlanetHeight() / (2*mapRes), colors);
                break;
        }

        planetDisplaying = terrainColorses.toBufferedImage();
        return (planetDisplaying);
    }

    //A small image for the map
    public Image getSquareImage(double scale) {
        BufferedImage planetDisplaying = new BufferedImage(p.getPlanetHeight(), p.getPlanetHeight(), BufferedImage.TYPE_INT_ARGB);
        //System.out.println(planetDisplaying);
        HashMap<Float, Color> colors;// = TerrainColoring.getRockyTerrainColoring(p.getTerrainColoringIndex());
        TerrainGenerator terrainGenerator = new TerrainGenerator();
        Color[][] terrainColorses = null;

        switch (p.getPlanetType()) {
            case PlanetTypes.ROCK:
                colors = TerrainColoring.getRockyTerrainColoring(p.getTerrainColoringIndex());
                terrainColorses = terrainGenerator.generate(p.getTerrainSeed(), 2, 0.5f, 2f, 0.5f, p.getPlanetHeight(), p.getPlanetHeight(), 0, p.getPlanetSize() / 3, 0, p.getPlanetSize() / 3, colors);
                break;
            case PlanetTypes.GAS:
                colors = TerrainColoring.getGassyTerrainColoring(p.getTerrainColoringIndex());
                terrainColorses = terrainGenerator.generate(p.getTerrainSeed(), 2, 0.5f, 2f, 0.5f, p.getPlanetHeight(), p.getPlanetHeight(), 0, p.getPlanetSize() / 18, 0, p.getPlanetSize() / 18, colors);
                break;
            default:
                //Default option
                colors = TerrainColoring.getGassyTerrainColoring(p.getTerrainColoringIndex());
                terrainColorses = terrainGenerator.generate(p.getTerrainSeed(), 2, 0.5f, 2f, 0.5f, p.getPlanetHeight(), p.getPlanetHeight(), 0, p.getPlanetSize() / 18, 0, p.getPlanetSize() / 18, colors);
                break;
        }

        if (terrainColorses != null) {
            for (int x = 0; x < p.getPlanetHeight(); x++) {
                for (int y = 0; y < p.getPlanetHeight(); y++) {
                    //System.out.println(x + " " + y + ";" + p.terrain.terrainColor[x][y]);
                    //System.err.println(planetDisplaying);
                    planetDisplaying.setRGB(x, y,
                            terrainColorses[x][y].getRGB());
                }
            }
        }
        return ((planetDisplaying.getScaledInstance((int) (p.getPlanetHeight() * scale), (int) (p.getPlanetHeight() * scale), Image.SCALE_DEFAULT)));
    }

}
