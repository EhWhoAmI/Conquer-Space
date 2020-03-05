package ConquerSpace.game.universe;

import ConquerSpace.game.universe.civilization.CivilizationConfig;
import ConquerSpace.game.universe.generators.DefaultUniverseGenerator;
import java.awt.Color;
import java.util.Random;

/**
 *
 * @author EhWhoAmI
 */
public class UniverseGenTest {

    public static void main(String[] args) {
        DefaultUniverseGenerator gen = new DefaultUniverseGenerator();
        UniverseConfig uc = new UniverseConfig();
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
        for (int i = 0; i < 250; i++) {
            int seed = rand.nextInt();
            gen.generate(uc, c, seed);
            System.out.println(i + ", " + seed);
        }
    }
}
