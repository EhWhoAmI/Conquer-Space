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
package ConquerSpace.game.universe.generators;

import ConquerSpace.game.GameController;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.organizations.civilization.controllers.AIController;
import ConquerSpace.game.organizations.civilization.controllers.PlayerController;
import ConquerSpace.game.economy.Currency;
import ConquerSpace.game.life.LifeTrait;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.life.Species;
import ConquerSpace.game.organizations.civilization.CivilizationPreferredClimateType;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.universe.PolarCoordinate;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.PlanetTypes;
import ConquerSpace.game.universe.bodies.Star;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.StarTypes;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.game.universe.bodies.terrain.TerrainColoring;
import ConquerSpace.game.resources.ResourceDistribution;
import ConquerSpace.game.resources.Stratum;
import ConquerSpace.util.logging.CQSPLogger;
import ConquerSpace.util.names.NameGenerator;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author User
 */
public class DefaultUniverseGenerator extends UniverseGenerator {

    private static final Logger LOGGER = CQSPLogger.getLogger(DefaultUniverseGenerator.class.getName());

    public static final double G = 6.674 * Math.pow(10, -11);          //Gravitational constant, same for everything

    public static final double AU_IN_LTYR = 63241.1;
    /**
     * Percentage that life happens on all planets. Only planets with life will
     * start of with life.
     */
    public static final double LIFE_OCCURANCE = 1;

    UniverseGenerationConfig u;
    CivilizationConfig c;
    long seed;

    public DefaultUniverseGenerator(UniverseGenerationConfig u, CivilizationConfig c, long seed) {
        this.u = u;
        this.c = c;
        this.seed = seed;
    }

    @Override
    public Universe generate() {
        Universe universe = new Universe(seed);
        //Create random 
        Random rand = new Random(seed);

        //Create star systems
        int starSystemCount = 100;
        switch (u.universeSize) {
            case Small:
                starSystemCount = (rand.nextInt(150) + 100);
                break;
            case Medium:
                starSystemCount = (rand.nextInt(150) + 200);
                break;
            case Large:
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
            // 100 light years
            int dist = rand.nextInt((int) (AU_IN_LTYR * 100));
            float degrees = (rand.nextFloat() * 360);
            StarSystem sys = new StarSystem(i, new PolarCoordinate(degrees, dist));

            //Add stars
            Star star = generateStar(rand);
            sys.addBody(star);

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
                    planetSize = randint(rand, 100, 1000);
                } else {
                    //Rock
                    planetSize = randint(rand, 20, 150);
                }
                Planet p = generatePlanet(planetType, planetSize, k, i, rand);
                //Set name
                if (planetNameGenerator != null) {
                    p.setName(planetNameGenerator.getName(rand.nextInt(planetNameGenerator.getRulesCount()), rand));
                }
                //Set changin degrees
                //Closer it is, the faster it is...
                //mass is size times 100,000,0000
                double degs = (10 / (k + 1)) * (((float) (rand.nextInt(5) + 7)) / 10);
                //degs *= 10;
                p.setDegreesPerTurn((float) degs);
                //System.err.println(p.terrain.terrainColor[0][0]);
                p.modDegrees(rand.nextInt(360));
                sys.addBody(p);
            }
            //Set name
            //Add planets
            universe.addStarSystem(sys);
        }
        LOGGER.info("Done with universe generation");

        //Do civs
        //Player civ
        Civilization playerCiv = new Civilization(c.civilizationName);
        playerCiv.setColor(c.civColor);
        playerCiv.setController(new PlayerController());
        playerCiv.setHomePlanetName(c.homePlanetName);
        playerCiv.setSpeciesName(c.speciesName);
        playerCiv.setCivilizationPreferredClimate(c.civilizationPreferredClimate);
        LOGGER.info("Creating suitable planet");
        UniversePath up = createSuitablePlanet(playerCiv, universe, rand, starSystemCount, planetNameGenerator);
        LOGGER.info("Done creating suitable planet");
        starSystemCount++;
        playerCiv.setStartingPlanet(up);
        //Generate Species
        Race playerSpecies = new Race(1, 0.01f, c.speciesName);
        universe.species.put(playerSpecies.getId(), playerSpecies);
        playerSpecies.setUpkeep(0.05f);

        //Set currency
        Currency nationCurrency = new Currency();
        nationCurrency.setName(c.civCurrencyName);
        nationCurrency.setSymbol(c.civCurrencySymbol);
        playerCiv.setNationalCurrency(nationCurrency);
        nationCurrency.setController(playerCiv);

        playerCiv.setFoundingSpecies(playerSpecies);

        universe.addOrganization(playerCiv);
        universe.addCivilization(playerCiv);
        LOGGER.info("Done with player civ " + playerCiv.getName());

        GameController.playerCiv = playerCiv;

        //Calculate number of civs
        int civCount = starSystemCount / 50;

        NameGenerator civNameGenerator = null;
        NameGenerator homePlanetNameGenerator = null;

        try {
            civNameGenerator = NameGenerator.getNameGenerator("civ.names");
            homePlanetNameGenerator = NameGenerator.getNameGenerator("home.planet.names");
        } catch (Exception e) {
        }
        for (int i = 0; i < civCount; i++) {
            //Create civ.
            String name = civNameGenerator.getName(0, rand);
            Civilization civ = new Civilization(name);
            civ.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
            civ.setController(new AIController());
            civ.setHomePlanetName(homePlanetNameGenerator.getName(0, rand));
            civ.setSpeciesName(name);
            CivilizationPreferredClimateType civPreferredClimate1 = CivilizationPreferredClimateType.values()[rand.nextInt(CivilizationPreferredClimateType.values().length)];
            civ.setCivilizationPreferredClimate(civPreferredClimate1);
            UniversePath up1 = createSuitablePlanet(playerCiv, universe, rand, starSystemCount, planetNameGenerator);
            starSystemCount++;
            civ.setStartingPlanet(up1);
            Race civSpecies = new Race(1, 0.01f, "Race " + i);
            universe.species.put(civSpecies.getId(), civSpecies);
            civ.setFoundingSpecies(civSpecies);

            //Create currency
            nationCurrency = new Currency();
            nationCurrency.setName("ISK");
            nationCurrency.setSymbol("Z");
            civ.setNationalCurrency(nationCurrency);
            nationCurrency.setController(civ);

            LOGGER.info("Done with civ " + civ.getName());
            //universe.
            universe.addCivilization(civ);
            universe.addOrganization(civ);
        }
        LOGGER.info("Done generating!");
        LOGGER.info("Going over civ initializing");

        CivilizationInitializer initer = new CivilizationInitializer(universe);
        initer.initGame();
        LOGGER.info("Done with civ initializing");

        return universe;
    }

    private static int randint(Random rand, int min, int max) {
        return (rand.nextInt((max - min)) + min);
    }

    /**
     * Creates a suitable planet and star system for the civ to live in
     *
     * @param c the civ
     * @param u universe
     * @return the universe path the new star system and planet is in
     */
    private UniversePath createSuitablePlanet(Civilization c, Universe u, Random rand, int lastStarSystem, NameGenerator planetNameGenerator) {
        //Make the things
        //
        int dist = rand.nextInt((int) (AU_IN_LTYR * 100));
        float degrees = (rand.nextFloat() * 360);
        StarSystem sys = new StarSystem(lastStarSystem, new PolarCoordinate(degrees, dist));

        //Add stars
        Star star = generateStar(rand);
        sys.addBody(star);

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
            Planet p = generatePlanet(planetType, planetSize, k, lastStarSystem, rand);
            p.setSemiMajorAxis((double) orbitalDistance);
            //So a circle...

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
                p.setName(planetNameGenerator.getName(rand.nextInt(planetNameGenerator.getRulesCount()), rand));
            }

            //Set changin degrees
            //Closer it is, the faster it is...
            //mass is size times 100,000,0000
            double degs = (10 / (k + 1)) * (((float) (rand.nextInt(5) + 7)) / 10);
            //degs *= 10;
            p.setDegreesPerTurn((float) degs);
            p.modDegrees(rand.nextInt(360));

            //Seed life
            if (rand.nextDouble() <= (LIFE_OCCURANCE)) {
                generateLocalLife(rand, p);
            }
            sys.addBody(p);
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
            Planet p = generatePlanet(planetType, planetSize, k, lastStarSystem, rand);

            generateResourceVeins(p, rand);
            
            p.setTerrainSeed(rand.nextInt());
            p.setTerrainColoringIndex(rand.nextInt(TerrainColoring.NUMBER_OF_ROCKY_COLORS));
            //= terrainColorses;
            

            //Set name
            if (planetNameGenerator != null) {
                p.setName(planetNameGenerator.getName(rand.nextInt(planetNameGenerator.getRulesCount()), rand));
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
            sys.addBody(p);
            living = p;
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

        Star star = new Star(starType, starSize);
        return star;
    }

    private Planet generatePlanet(int planetType, int planetSize, int id, int systemID, Random rand) {
        Planet p = new Planet(planetType, planetSize, id, systemID);

        generateResourceVeins(p, rand);
        if (planetType == PlanetTypes.ROCK) {
            p.setTerrainSeed(rand.nextInt());
            p.setTerrainColoringIndex(rand.nextInt(TerrainColoring.NUMBER_OF_ROCKY_COLORS));
            //= terrainColorses;
        } else if (planetType == PlanetTypes.GAS) {
            p.setTerrainSeed(rand.nextInt());
            p.setTerrainColoringIndex(rand.nextInt(TerrainColoring.NUMBER_OF_GASSY_COLORS));
        }

        return p;
    }

    public void generateResourceVeins(Planet p, Random rand) {
        int planetSize = p.getPlanetSize();

        //Find the amount of resources to add...
        //Sort through resources, and find suitable
        ArrayList<Integer> toAdd = new ArrayList<>();
        for (Map.Entry<Integer, ResourceDistribution> entry : GameController.ores.entrySet()) {
            Integer key = entry.getKey();
            ResourceDistribution val = entry.getValue();
            double rarity = val.rarity;
            if (rand.nextDouble() < rarity) {
                toAdd.add(key);
            }
        }

        //Add resource veins
        int idCount = randint(rand, 5, 10);

        //Add the veins
        for (int i = 0; i < idCount; i++) {
            //Create strata
            Stratum stratum = new Stratum();

            //Add resources
            for (Integer o : toAdd) {
                stratum.minerals.put(o, randint(rand, 10000, 500_000));
            }

            //Select the things
            stratum.setRadius(randint(rand, 3, planetSize));
            stratum.setX(rand.nextInt(p.getPlanetWidth()));
            stratum.setY(rand.nextInt(p.getPlanetHeight()));
            stratum.setDepth(rand.nextInt(planetSize / 2) + 1);
            p.strata.add(stratum);
        }
    }

    private void generateLocalLife(Random rand, Planet p) {
        //Get distance from planet and calculate...
        //Then the planet has life
        //Stages it has life, and how evolved it is
        int lifeLength = rand.nextInt(10);

        //Get random name
        NameGenerator speciesNameGenerator = null;
        try {
            speciesNameGenerator = NameGenerator.getNameGenerator("species.names");
        } catch (IOException ex) {
        }

        //Initialize life
        Species micro = new Species(speciesNameGenerator.getName(0, rand));
        //Set name
        //Add random trait
        LifeTrait randomLifeTrait = LifeTrait.values()[rand.nextInt(LifeTrait.values().length)];
        micro.lifeTraits.add(randomLifeTrait);
        LocalLife life = new LocalLife();
        life.setSpecies(micro);

        p.localLife.add(life);

        for (int i = 0; i < lifeLength; i++) {
            //Evolve
        }
    }
}
