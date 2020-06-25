package ConquerSpace.game.universe;

import ConquerSpace.game.universe.generators.UniverseGenerationConfig;
import ConquerSpace.game.GameLoader;
import ConquerSpace.game.universe.generators.CivilizationConfig;
import ConquerSpace.game.universe.generators.DefaultUniverseGenerator;
import java.awt.Color;
import java.util.Random;

/**
 *
 * @author EhWhoAmI
 */
public class UniverseGenTest {

    public static void main(String[] args) {
        long timeStart = System.currentTimeMillis();
        GameLoader.load();
        long timeEnd = System.currentTimeMillis();
        System.out.println("Took " + (timeEnd - timeStart) + "ms to load assets");

        UniverseGenerationConfig uc = new UniverseGenerationConfig();
        uc.seed = 0;
        uc.universeSize = "Medium";
        CivilizationConfig c = new CivilizationConfig();
        c.speciesName = "adsf";
        c.civSymbol = "asdf";
        c.civColor = Color.BLACK;
        c.civilizationName = "adsf";
        c.homePlanetName = "afsd";
        c.civilizationPreferredClimate = "Cold";

        Random rand = new Random();

        int seed = rand.nextInt();
        timeStart = System.currentTimeMillis();

        DefaultUniverseGenerator gen = new DefaultUniverseGenerator(uc, c, seed);
        gen.generate();

        timeEnd = System.currentTimeMillis();
        System.out.println("Generated with seed " + seed);
        System.out.println("Took " + (timeEnd - timeStart) + "ms to generate game");
    }
}
