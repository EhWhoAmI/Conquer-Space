package ConquerSpace.game.universe.generators;

import ConquerSpace.game.GameController;
import ConquerSpace.game.GameUpdater;
import ConquerSpace.game.universe.GalacticLocation;
import ConquerSpace.game.universe.UniverseConfig;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.CivilizationConfig;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.PlanetTypes;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.StarTypes;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.game.universe.spaceObjects.terrain.TerrainTile;
import java.awt.Color;
import java.util.HashMap;
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

        //Load resources
        GameUpdater.readResources();

        TerrainGenerator terrainGenerator = new TerrainGenerator();
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
                //System.out.println(planetType);
                long orbitalDistance = (long) (lastDistance * (rand.nextDouble() + 1.5d));
                lastDistance = orbitalDistance;
                int planetSize;
                if (planetType == PlanetTypes.GAS) {
                    planetSize = randint(rand, 50, 500);
                } else {
                    //Rock
                    planetSize = randint(rand, 30, 100);

                }

                Planet p = new Planet(planetType, orbitalDistance, planetSize, k, i);

                //Add resource veins
                int resourceCount = randint(rand, planetSize / 2, planetSize * 2);

                for (Resource res : GameController.resources) {
                    //Process...
                    float rarity = res.getRarity();
                    float probality = rand.nextFloat();
                    //Then count
                    if (probality < rarity) {
                        //Then add a certain amount
                        int amount = (int) (rarity * resourceCount * probality);
                        //Add that amount
                        for (int resCount = 0; resCount < amount; resCount++) {
                            //Add the resource
                            ResourceVein vein = new ResourceVein(res, 10);

                            vein.setRadius(randint(rand, 5, 50));
                            vein.setX(rand.nextInt(planetSize * 2));
                            vein.setY(rand.nextInt(planetSize));
                            p.resourceVeins.add(vein);
                        }
                    }
                }

                if (planetType == PlanetTypes.ROCK) {
                    HashMap<Float, Color> colors = new HashMap<>();
                    //Set planet tileset...
                    //And composotion
                    colors.put(-1f, new Color(69, 24, 4));
                    colors.put(-0.25f, new Color(193, 68, 14));
                    colors.put(0.25f, new Color(231, 125, 17));
                    colors.put(0.75f, new Color(253, 166, 0));
                    colors.put(0.9f, new Color(240, 231, 231));
                    Color[][] terrainColorses = terrainGenerator.generate(rand.nextInt(), 6, 0.5f, 2.8f, 0.5f, planetSize * 2, planetSize, 0, planetSize / 3, 0, planetSize / 6, colors);

                    for (int x = 0; x < terrainColorses.length; x++) {
                        for (int y = 0; y < terrainColorses[0].length; y++) {
                            p.terrain.terrainColor[x][y] = new TerrainTile();
                            p.terrain.terrainColor[x][y].color = terrainColorses[x][y];
                        }
                    }
                    //= terrainColorses;
                }
                //System.err.println(p.terrain.terrainColor[0][0]);
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
        Planet p = (Planet)universe.getSpaceObject(up);
        if(p.terrain.terrainColor[0][0] == null) {
            throw new ArithmeticException();
        }

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
            universe.addCivilization(civ);
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
        int randomP = 0;

        do {
            //Loop through the numbers
            if (sys.getPlanetCount() <= 0 || randomP >= sys.getPlanetCount()) {
                if (randomSS >= u.getStarSystemCount()-1) {
                    randomSS = 0;
                } else {
                    randomSS++;
                }
                sys = u.getStarSystem(randomSS);
                if (u.getStarSystemCount() > 0) {
                    randomP = rand.nextInt(u.getStarSystemCount());
                }
                continue;
            }
            //Do the stuff

            if (sys.getPlanet(randomP)
                    .getPlanetType() == PlanetTypes.ROCK) {
                break; //o7
            }
            randomP++;
        } while (true);
        return new UniversePath(randomSS, randomP);
    }
}
