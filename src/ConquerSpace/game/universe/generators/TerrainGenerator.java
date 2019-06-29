package ConquerSpace.game.universe.generators;

import ConquerSpace.jLibNoise.noise.module.Perlin;
import ConquerSpace.jLibNoise.noise.utils.Image;
import ConquerSpace.jLibNoise.noise.utils.NoiseMap;
import ConquerSpace.jLibNoise.noise.utils.NoiseMapBuilderPlane;
import ConquerSpace.jLibNoise.noise.utils.RendererImage;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * http://libnoise.sourceforge.net/tutorials/tutorial4.html
 *
 * @author zyunl
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
        renderer.render();

        return image.toIntArray();
    }

    public int[][] generate(int seed, int octaves, float frequency, float persistence, int sizeX, int sizeY, float boundsX1, float boundsX2, float boundsY1, float boundsY2) {
        Perlin myModule = new Perlin();
        myModule.setOctaveCount(octaves);
        myModule.setSeed(seed);
        myModule.setFrequency(frequency);
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
        renderer.render();

        return image.toIntArray();
    }
    
    public Image generateImage(int seed, int octaves, float frequency, float lacunarity, float persistence, int sizeX, int sizeY, float boundsX1, float boundsX2, float boundsY1, float boundsY2, HashMap<Float, Color> colors) {
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
        renderer.render();

        return image;
    }
}
