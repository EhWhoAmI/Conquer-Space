package ConquerSpace.game.universe.generators;

import ConquerSpace.game.AssetReader;
import ConquerSpace.game.GameController;
import ConquerSpace.game.economy.Currency;
import ConquerSpace.game.life.LifeTrait;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.life.Species;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.universe.GalacticLocation;
import ConquerSpace.game.universe.UniverseConfig;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.CivilizationConfig;
import ConquerSpace.game.universe.civilization.controllers.AIController.AIController;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.PlanetTypes;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.StarTypes;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.game.universe.spaceObjects.terrain.TerrainColoring;
import ConquerSpace.util.names.NameGenerator;
import java.awt.Color;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author User
 */
public class DefaultUniverseGenerator extends UniverseGenerator {

    public static final double G = 6.674 * Math.pow(10, -11);          //Gravitational constant, same for everything
    /**
     * Percentage that life happens on all planets. Only planets with life will
     * start of with life.
     */
    public static final double LIFE_OCCURANCE = 1;

    @Override
    public Universe generate(UniverseConfig u, CivilizationConfig c, long seed) {
        Universe universe = new Universe(seed);
        //Create random 
        Random rand = new Random(seed);

        //Load resources
        AssetReader.readResources();

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

        NameGenerator planetNameGenerator = null;
        try {
            planetNameGenerator = NameGenerator.getNameGenerator("planet.names");
        } catch (IOException ex) {
        }

        for (int i = 0; i < starSystemCount; i++) {
            //Create star system
            int dist = rand.nextInt(6324100);
            float degrees = (rand.nextFloat() * 360);
            StarSystem sys = new StarSystem(i, new GalacticLocation(degrees, dist));

            //Add stars
            Star star = generateStar(rand);
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

                generateResourceVeins(p, rand);
                if (planetType == PlanetTypes.ROCK) {
                    p.setTerrainSeed(rand.nextInt());
                    p.setTerrainColoringIndex(rand.nextInt(TerrainColoring.NUMBER_OF_ROCKY_COLORS));
                    //= terrainColorses;
                } else if (planetType == PlanetTypes.GAS) {
                    p.setTerrainSeed(rand.nextInt());
                    p.setTerrainColoringIndex(rand.nextInt(TerrainColoring.NUMBER_OF_GASSY_COLORS));
                }
                //Set name
                if (planetNameGenerator != null) {
                    p.setName(planetNameGenerator.getName(rand.nextInt(planetNameGenerator.getRulesCount())));
                }

                //Set changin degrees
                //Closer it is, the faster it is...
                //mass is size times 100,000,0000
                double degs = (10 / (k + 1)) * (((float) (rand.nextInt(5) + 7)) / 10);
                //degs *= 10;
                p.setDegreesPerTurn((float) degs);
                //System.err.println(p.terrain.terrainColor[0][0]);
                p.modDegrees(rand.nextInt(360));

                //Seed life
                if (rand.nextDouble() <= (LIFE_OCCURANCE)) {
                    generateLocalLife(rand, p);
                }
                sys.addPlanet(p);
            }
            //Set name
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
        up = createSuitablePlanet(playerCiv, universe, rand, starSystemCount, planetNameGenerator);
        starSystemCount++;
        playerCiv.setStartingPlanet(up);
        //Generate Species
        Race playerSpecies = new Race(1, 1, c.speciesName);
        playerSpecies.setUpkeep(0.05f);

        //Set currency
        Currency nationCurrency = new Currency();
        nationCurrency.setName(c.civCurrencyName);
        nationCurrency.setSymbol(c.civCurrencySymbol);
        playerCiv.setNationalCurrency(nationCurrency);
        nationCurrency.setController(playerCiv);

        playerCiv.setFoundingSpecies(playerSpecies);
        universe.addCivilization(playerCiv);

        //Calculate number of civs
        int civCount = starSystemCount / 50;

        for (int i = 0; i < civCount; i++) {
            //Create civ.
            Civilization civ = new Civilization(i + 1, universe);
            civ.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
            civ.setController(new AIController());
            civ.setHomePlanetName("");
            civ.setName("");
            civ.setSpeciesName("");
            int civPreferredClimate1 = rand.nextInt(3);
            civ.setCivilizationPreferredClimate(civPreferredClimate1);
            UniversePath up1 = getRandomSuitablePlanet(rand, universe);
            up1 = createSuitablePlanet(playerCiv, universe, rand, starSystemCount, planetNameGenerator);
            starSystemCount++;
            civ.setStartingPlanet(up1);
            Race civSpecies = new Race(1, 1, "");
            civ.setFoundingSpecies(civSpecies);

            //Create currency
            nationCurrency = new Currency();
            nationCurrency.setName("ISK");
            nationCurrency.setSymbol("Z");
            civ.setNationalCurrency(nationCurrency);
            nationCurrency.setController(civ);

            //universe.
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

        main:
        do {
            //Loop through the numbers
            if (sys.getPlanetCount() <= 0 || randomP >= sys.getPlanetCount()) {
                if (randomSS >= u.getStarSystemCount() - 1) {
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

    /**
     * Creates a suitable planet and star system for the civ to live in
     *
     * @param c the civ
     * @param u universe
     * @return the universe path the new star system and planet is in
     */
    private UniversePath createSuitablePlanet(Civilization c, Universe u, Random rand, int lastPlanet, NameGenerator planetNameGenerator) {
        //Make the things
        int dist = rand.nextInt(6324100);
        float degrees = (rand.nextFloat() * 360);
        StarSystem sys = new StarSystem(lastPlanet, new GalacticLocation(degrees, dist));

        //Add stars
        Star star = generateStar(rand);
        sys.addStar(star);

        int planetCount = rand.nextInt(5) + 6;
        long lastDistance = 10000000;
        Planet living = null;
        int k = 0;
        for (k = 0; k < planetCount; k++) {
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
            Planet p = new Planet(planetType, orbitalDistance, planetSize, k, lastPlanet);
            p.setSemiMajorAxis((double) orbitalDistance);
            //So a circle...
            p.setEccentricity(0);

            p.setRotation(rand.nextDouble() * 2 * Math.PI);
            generateResourceVeins(p, rand);
            if (planetType == PlanetTypes.ROCK) {
                p.setTerrainSeed(rand.nextInt());
                p.setTerrainColoringIndex(rand.nextInt(TerrainColoring.NUMBER_OF_ROCKY_COLORS));
                //= terrainColorses;
                if (living == null) {
                    living = p;
                }
            } else if (planetType == PlanetTypes.GAS) {
                p.setTerrainSeed(rand.nextInt());
                p.setTerrainColoringIndex(rand.nextInt(TerrainColoring.NUMBER_OF_GASSY_COLORS));
            }
            //Set name
            if (planetNameGenerator != null) {
                p.setName(planetNameGenerator.getName(rand.nextInt(planetNameGenerator.getRulesCount())));
            }

            //Set changin degrees
            //Closer it is, the faster it is...
            //mass is size times 100,000,0000
            double degs = (10 / (k + 1)) * (((float) (rand.nextInt(5) + 7)) / 10);
            //degs *= 10;
            p.setDegreesPerTurn((float) degs);
            //System.err.println(p.terrain.terrainColor[0][0]);
            p.modDegrees(rand.nextInt(360));

            //Seed life
            if (rand.nextDouble() <= (LIFE_OCCURANCE)) {
                generateLocalLife(rand, p);
            }
            sys.addPlanet(p);
        }

        if (living == null) {
            //Add a rocky planet just to be sure.
            int planetType = PlanetTypes.ROCK;
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
            Planet p = new Planet(planetType, orbitalDistance, planetSize, k, lastPlanet);

            generateResourceVeins(p, rand);
            if (planetType == PlanetTypes.ROCK) {
                p.setTerrainSeed(rand.nextInt());
                p.setTerrainColoringIndex(rand.nextInt(TerrainColoring.NUMBER_OF_ROCKY_COLORS));
                //= terrainColorses;
                if (living == null) {
                    living = p;
                }
            } else if (planetType == PlanetTypes.GAS) {
                p.setTerrainSeed(rand.nextInt());
                p.setTerrainColoringIndex(rand.nextInt(TerrainColoring.NUMBER_OF_GASSY_COLORS));
            }
            //Set name
            if (planetNameGenerator != null) {
                p.setName(planetNameGenerator.getName(rand.nextInt(planetNameGenerator.getRulesCount())));
            }

            //Set changin degrees
            //Closer it is, the faster it is...
            //mass is size times 100,000,0000
            double degs = (10 / (k + 1)) * (((float) (rand.nextInt(5) + 7)) / 10);
            //degs *= 10;
            p.setDegreesPerTurn((float) degs);
            //System.err.println(p.terrain.terrainColor[0][0]);
            p.modDegrees(rand.nextInt(360));

            //Seed life
            if (rand.nextDouble() <= (LIFE_OCCURANCE)) {
                generateLocalLife(rand, p);
            }
            sys.addPlanet(p);
        }

        u.addStarSystem(sys);

        //get a rocky planet
        return living.getUniversePath();
    }

    private Star generateStar(Random rand) {
        int starType = rand.nextInt(10000);
        int starSize;
        int solarRadius = 695508;

        if (starType < 7) {
            starType = StarTypes.TYPE_O;
            starSize = randint(rand, (int) (6.6 * solarRadius), (int) (100f * solarRadius));
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
        return star;

    }

    public void generateResourceVeins(Planet p, Random rand) {
        int planetSize = p.getPlanetSize();
        //Add resource veins

        int idCount = 0;
        for (Resource res : GameController.resources) {
            //Process... 
            //Determines the resource 'richness' of a planet
            int resourceCount = randint(rand, planetSize / 2, planetSize * 2);
            float rarity = res.getRarity();
            float probality = rand.nextFloat();
            //Then count
            if (true) {
                //Then add a certain amount
                int amount = (int) (rarity * resourceCount * probality) * 2;
                //Add that amount
                for (int resCount = 0; resCount < amount; resCount++) {
                    //Add the resource
                    int resourceVolume = (int) (randint(rand, 50000, 100000) * res.getDensity());
                    ResourceVein vein = new ResourceVein(res, resourceVolume);
                    vein.setId(idCount++);
                    vein.setRadius(randint(rand, res.getDistributionLow(), res.getDistributionHigh()));
                    vein.setX(rand.nextInt(planetSize * 2));
                    vein.setY(rand.nextInt(planetSize));
                    p.resourceVeins.add(vein);
                }
            }
        }
    }

    private void generateLocalLife(Random rand, Planet p) {
        //Get distance from planet and calculate...
        //Then the planet has life
        //Stages it has life, and how evolved it is
        int lifeLength = rand.nextInt(10);
        //Initialize life
        Species micro = new Species();
        //Set name
        //Add random trait
        LifeTrait randomLifeTrait = LifeTrait.values()[rand.nextInt(LifeTrait.values().length)];
        micro.lifeTraits.add(randomLifeTrait);
        micro.setName(randomLifeTrait.name());
        LocalLife life = new LocalLife();
        life.setSpecies(micro);

        p.localLife.add(life);

        for (int i = 0; i < lifeLength; i++) {
        }
    }
}
