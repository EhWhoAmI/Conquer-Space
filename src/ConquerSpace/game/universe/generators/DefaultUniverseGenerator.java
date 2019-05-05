package ConquerSpace.game.universe.generators;

import ConquerSpace.game.universe.GalacticLocation;
import ConquerSpace.game.universe.UniverseConfig;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.CivilizationConfig;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.universe.resources.RawResourceTypes;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.PlanetTypes;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.StarTypes;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.game.universe.spaceObjects.pSectors.RawResource;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author User
 */
public class DefaultUniverseGenerator extends UniverseGenerator {

    @Override
    public Universe generate(UniverseConfig u, CivilizationConfig c, long seed) {
        Universe universe = new Universe(seed);
        //Create random 
        Random rand = new Random(seed);

        //Create star systems
        int starSystemCount = 100;
        switch (u.universeSize) {
            case "Small":
                starSystemCount = (rand.nextInt(150) + 100);
                break;
            case "Medium":
                starSystemCount = (rand.nextInt(150) + 200);
                break;
            case "Large":
                starSystemCount = (rand.nextInt(150) + 300);
                break;
        }
        for (int i = 0; i < starSystemCount; i++) {
            //Create star system
            int dist = rand.nextInt(100);
            float degrees = (rand.nextFloat() * 360);
            StarSystem sys = new StarSystem(i, new GalacticLocation(degrees, dist));

            //Add stars
            int starType = rand.nextInt(10000);
            int starSize;
            int solarRadius = 695508;

            if (starType < 7) {
                starType = StarTypes.TYPE_O;
                starSize = randint(rand, (int) (6.6 * solarRadius), (int) (100 * solarRadius));
            } else if (starType < 20) {
                starType = StarTypes.TYPE_B;
                starSize = randint(rand, (int) (1.8 * solarRadius), (int) (6.6 * solarRadius));
            } else if (starType < 90) {
                starType = StarTypes.TYPE_A;
                starSize = randint(rand, (int) (1.4 * solarRadius), (int) (1.8 * solarRadius));
            } else if (starType < 390) {
                starType = StarTypes.TYPE_F;
                starSize = randint(rand, (int) (1.15 * solarRadius), (int) (1.4 * solarRadius));
            } else if (starType < 1290) {
                starType = StarTypes.TYPE_G;
                starSize = randint(rand, (int) (0.96 * solarRadius), (int) (1.15 * solarRadius));
            } else if (starType < 2500) {
                starType = StarTypes.TYPE_K;
                starSize = randint(rand, (int) (0.7 * solarRadius), (int) (0.96 * solarRadius));
            } else {
                starType = StarTypes.TYPE_M;
                starSize = randint(rand, (int) (0.1 * solarRadius), (int) (0.7 * solarRadius));
            }

            Star star = new Star(starType, starSize, 0);
            sys.addStar(star);

            int planetCount = rand.nextInt(11);
            long lastDistance = 10000000;
            for (int k = 0; k < planetCount; k++) {
                //Add planets
                //Set stuff
                int planetType = Math.round(rand.nextFloat());
                long orbitalDistance = (long) (lastDistance * (rand.nextDouble() + 1.1d));
                lastDistance = orbitalDistance;
                int planetSize;
                if (planetType == PlanetTypes.GAS) {
                    planetSize = randint(rand, 25, 50);
                } else {
                    //Rock
                    planetSize = randint(rand, 3, 30);
                }
                Planet p = new Planet(planetType, orbitalDistance, planetSize, k, i, 0);
                for (int b = 0; b < p.getPlanetSectorCount(); b++) {
                    //Fill
                    RawResource rawr = new RawResource();
                    if (p.getPlanetType() == PlanetTypes.GAS) {
                        rawr.resources.add(new Resource(RawResourceTypes.GAS, randint(rand, 10_000, 50_000)));
                    } else {
                        //Rock add resources
                        int resourceTypesCount = randint(rand, 0, 5);
                        ArrayList<Integer> alist = new ArrayList<>();
                        alist.add(0);
                        alist.add(1);
                        alist.add(2);
                        alist.add(3);
                        alist.add(4);
                        for (int x = 0; x < resourceTypesCount; x++) {
                            //Get random from list
                            //int resid = alist.get(randint(rand, 0, alist.size()));
                            int resid = alist.remove(randint(rand, 0, alist.size() - 1));
                            rawr.resources.add(new Resource(resid, randint(rand, 10_000, 50_000)));
                        }
                    }
                    p.planetSectors[b] = rawr;
                }
                sys.addPlanet(p);
            }
            //Add planets
            universe.addStarSystem(sys);
        }
        //Do civs
        //Player civ
        Civilization playerCiv = new Civilization(0, universe);
        playerCiv.setColor(c.civColor);
        playerCiv.setController(new PlayerController());
        playerCiv.setHomePlanetName(c.homePlanetName);
        playerCiv.setName(c.civilizationName);
        playerCiv.setSpeciesName(c.speciesName);
        int civPreferredClimate = 0;
        switch (c.civilizationPreferredClimate) {
            case "Varied":
                civPreferredClimate = 0;
                break;
            case "Cold":
                civPreferredClimate = 1;
                break;
            case "Hot":
                civPreferredClimate = 2;
        }
        playerCiv.setCivilizationPreferredClimate(civPreferredClimate);
        UniversePath up = getRandomSuitablePlanet(rand, universe);
        playerCiv.setStartingPlanet(up);

        universe.addCivilization(playerCiv);
        //Calculate number of civs
        int civCount = starSystemCount / 50;

        for (int i = 0; i < civCount; i++) {
            //Create civ.
            Civilization civ = new Civilization(i + 1, universe);
            civ.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
            civ.setController(new PlayerController());
            civ.setHomePlanetName("");
            civ.setName("");
            civ.setSpeciesName("");
            int civPreferredClimate1 = rand.nextInt(3);
            civ.setCivilizationPreferredClimate(civPreferredClimate1);
            UniversePath up1 = getRandomSuitablePlanet(rand, universe);
            civ.setStartingPlanet(up1);
        }
        return universe;
    }

    private static int randint(Random rand, int min, int max) {
        return (rand.nextInt((max - min)) + min);
    }

    private UniversePath getRandomSuitablePlanet(Random rand, Universe u) {
        //Select
        int randomSS = rand.nextInt(u.getStarSystemCount());
        StarSystem sys = u.getStarSystem(randomSS);
        //Get planets

        int randomP = rand.nextInt(sys.getPlanetCount());
        while (sys.getPlanetCount() < 0) {
            randomSS++;
            if (randomSS > u.getStarSystemCount()) {
                randomSS = 0;
            }
            sys = u.getStarSystem(randomSS);
        }
        //Check suitability
        //Etc.....
        //Now, just check if it is rock or not
        int i = randomP;
        while (sys.getPlanet(i).getPlanetType() != PlanetTypes.ROCK) {
            if (i < sys.getPlanetCount()) {
                i++;
            } else {
                //Search for another star system
                if (randomSS < u.getStarSystemCount()) {
                    randomSS++;
                } else {
                    randomSS = 0;
                }
                sys = u.getStarSystem(randomSS);
                i = rand.nextInt(sys.getPlanetCount());
            }
        }
        return new UniversePath(randomSS, i);
    }
}
