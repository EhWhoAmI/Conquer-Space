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

import ConquerSpace.jLibNoise.noise.module.Perlin;
import ConquerSpace.jLibNoise.noise.utils.Image;
import ConquerSpace.jLibNoise.noise.utils.NoiseMap;
import ConquerSpace.jLibNoise.noise.utils.NoiseMapBuilderPlane;
import ConquerSpace.jLibNoise.noise.utils.RendererImage;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * http://libnoise.sourceforge.net/tutorials/tutorial4.html. Just a quick driver to simplify the generation of the background
 *
 * @author EhWhoAmI
 */
public class TerrainGenerator {

    public Color[][] generate(int seed, int octaves, float frequency, float lacunarity, float persistence, int sizeX, int sizeY, float boundsX1, float boundsX2, float boundsY1, float boundsY2, HashMap<Float, Color> colors) {
        Perlin myModule = new Perlin();
        myModule.setOctaveCount(octaves);
        myModule.setSeed(seed);
        myModule.setFrequency(frequency);
        myModule.setLacunarity(lacunarity);
        myModule.setPersistence(persistence);
        NoiseMap heightMap = new NoiseMap();
        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.setSourceModule(myModule);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(sizeX, sizeY);
        heightMapBuilder.setBounds(boundsX1, boundsX2, boundsY1, boundsY2);
        heightMapBuilder.build();
        heightMapBuilder.setSourceModule(myModule);
        heightMapBuilder.setDestNoiseMap(heightMap);

        RendererImage renderer = new RendererImage();

        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.clearGradient();
        for (Map.Entry<Float, Color> entry : colors.entrySet()) {
            Float key = entry.getKey();
            Color value = entry.getValue();
            renderer.addGradientPoint(key, new ConquerSpace.jLibNoise.noise.utils.Color(value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha()));
        }
        renderer.enableLight();
        renderer.setLightContrast(1);
        renderer.setLightBrightness(2.0);
        renderer.setDestImage(image);
        renderer.render();

        Color[][] array = new Color[sizeX][sizeY];
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                ConquerSpace.jLibNoise.noise.utils.Color col = image.getConstSlabPtr(x, y).get();
                array[x][y] = new Color(col.red, col.green, col.blue, col.alpha);
            }
        }
        return array;
    }

    public int[][] generate(int seed, int octaves, float frequency, float lacunarity, float persistence, int sizeX, int sizeY, float boundsX1, float boundsX2, float boundsY1, float boundsY2) {
        Perlin perlin = new Perlin();
        perlin.setOctaveCount(octaves);
        perlin.setSeed(seed);
        perlin.setFrequency(frequency);
        perlin.setLacunarity(lacunarity);
        perlin.setPersistence(persistence);
        NoiseMap heightMap = new NoiseMap();
        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.setSourceModule(perlin);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(sizeX, sizeY);
        heightMapBuilder.setBounds(boundsX1, boundsX2, boundsY1, boundsY2);
        heightMapBuilder.build();
        heightMapBuilder.setSourceModule(perlin);
        heightMapBuilder.setDestNoiseMap(heightMap);

        RendererImage renderer = new RendererImage();

        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.render();

        return image.toIntArray();
    }

    public int[][] generate(int seed, int octaves, float frequency, float persistence, int sizeX, int sizeY, float boundsX1, float boundsX2, float boundsY1, float boundsY2) {
        Perlin perlin = new Perlin();
        perlin.setOctaveCount(octaves);
        perlin.setSeed(seed);
        perlin.setFrequency(frequency);
        perlin.setPersistence(persistence);
        NoiseMap heightMap = new NoiseMap();
        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.setSourceModule(perlin);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(sizeX, sizeY);
        heightMapBuilder.setBounds(boundsX1, boundsX2, boundsY1, boundsY2);
        heightMapBuilder.build();
        heightMapBuilder.setSourceModule(perlin);
        heightMapBuilder.setDestNoiseMap(heightMap);

        RendererImage renderer = new RendererImage();

        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.render();

        return image.toIntArray();
    }

    public Image generateImage(int seed, int octaves, float frequency, float lacunarity, float persistence, int sizeX, int sizeY, float boundsX1, float boundsX2, float boundsY1, float boundsY2, HashMap<Float, Color> colors) {
        Perlin perlin = new Perlin();
        perlin.setOctaveCount(octaves);
        perlin.setSeed(seed);
        perlin.setFrequency(frequency);
        perlin.setLacunarity(lacunarity);
        perlin.setPersistence(persistence);
        NoiseMap heightMap = new NoiseMap();
        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.setSourceModule(perlin);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(sizeX, sizeY);
        heightMapBuilder.setBounds(boundsX1, boundsX2, boundsY1, boundsY2);
        heightMapBuilder.build();
        heightMapBuilder.setSourceModule(perlin);
        heightMapBuilder.setDestNoiseMap(heightMap);

        RendererImage renderer = new RendererImage();

        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.clearGradient();
        for (Map.Entry<Float, Color> entry : colors.entrySet()) {
            Float key = entry.getKey();
            Color value = entry.getValue();

            renderer.addGradientPoint(key, new ConquerSpace.jLibNoise.noise.utils.Color(value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha()));
        }
        renderer.enableLight();
        renderer.setLightContrast(2.5);
        renderer.setLightBrightness(2);
        renderer.setDestImage(image);
        renderer.render();
        
        return image;
    }
}
