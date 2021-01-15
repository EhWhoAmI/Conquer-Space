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
package ConquerSpace.server.generators;

import ConquerSpace.client.gui.game.planetdisplayer.TerrainColoring;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.economy.Currency;
import ConquerSpace.common.game.life.LifeTrait;
import ConquerSpace.common.game.life.LocalLife;
import ConquerSpace.common.game.life.Species;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.population.Race;
import ConquerSpace.common.game.population.RacePreferredClimateType;
import ConquerSpace.common.game.resources.ResourceDistribution;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.resources.Stratum;
import ConquerSpace.common.game.universe.PolarCoordinate;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.PlanetTypes;
import ConquerSpace.common.game.universe.bodies.Star;
import ConquerSpace.common.game.universe.bodies.StarSystem;
import ConquerSpace.common.game.universe.bodies.StarType;
import ConquerSpace.common.util.logging.CQSPLogger;
import ConquerSpace.common.util.names.NameGenerator;
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

    public static final long KM_IN_LTYR = 9460730472580800L;
    /**
     * Percentage that life happens on all planets. Only planets with life will start of with life.
     */
    public static final double LIFE_OCCURANCE = 1;

    private UniverseGenerationConfig universeGenerationConfig;
    private CivilizationConfig civilizationConfig;
    private long seed;
    private GameState gameState;

    private Random random;

    public DefaultUniverseGenerator(UniverseGenerationConfig u, CivilizationConfig c, long seed) {
        this.universeGenerationConfig = u;
        this.civilizationConfig = c;
        this.seed = seed;
    }

    @Override
    public Galaxy generate(GameState state) {
        gameState = state;
        gameState.setSeed(seed);
        random = gameState.getRandom();

        Galaxy universe = gameState.getUniverse();

        //Create random 
        Random rand = new Random(seed);

        //Create star systems
        int starSystemCount = getStarSystemCount(rand, universeGenerationConfig.universeSize);

        NameGenerator planetNameGenerator = null;
        try {
            planetNameGenerator = NameGenerator.getNameGenerator("planet.names");
        } catch (IOException ex) {
        }

        for (int systemCount = 0; systemCount < starSystemCount; systemCount++) {
            //Create star system
            // 100 light years
            long dist = nextLong(rand, KM_IN_LTYR * 100);
            float degrees = (rand.nextFloat() * 360);
            StarSystem sys = new StarSystem(state, new PolarCoordinate(degrees, dist));
            universe.addStarSystem(sys);

            //Add stars
            Star star = generateStar(rand);
            sys.addBody(star);

            int planetCount = rand.nextInt(11);
            long lastDistance = 10000000;
            for (int planetIndex = 0; planetIndex < planetCount; planetIndex++) {
                //Add planets
                //Set stuff
                int planetType = Math.round(rand.nextFloat());
                //System.out.println(planetType);
                long orbitalDistance = (long) (lastDistance * (rand.nextDouble() + 1.5d));
                lastDistance = orbitalDistance;
                int planetSize = getRandomPlanetSize(planetType, rand);
                Planet p = generatePlanet(planetType, orbitalDistance, planetSize, rand);
                sys.addBody(p);
                //Set name
                if (planetNameGenerator != null) {
                    p.setName(planetNameGenerator.getName(rand.nextInt(planetNameGenerator.getRulesCount()), rand));
                }
                sys.calculateBoundary();
            }
        }
        LOGGER.info("Done with universe generation");

        //Make civs
        int civCount = starSystemCount / 50;
        createCivilizations(universe, rand, civCount, planetNameGenerator);

        LOGGER.info("Done generating!");
        LOGGER.info("Going over civ initializing");

        CivilizationInitializer initer = new CivilizationInitializer(state);
        initer.initCivilization();
        LOGGER.info("Done with civ initializing");

        return universe;
    }
    
    public int getStarSystemCount(Random rand, UniverseGenerationConfig.UniverseSize universeSize) {
        int starSystemCount = 0;
        switch (universeSize) {
            case Small:
                starSystemCount = (rand.nextInt(150) + 100);
                break;
            case Medium:
                starSystemCount = (rand.nextInt(150) + 200);
                break;
            default:
            case Large:
                starSystemCount = (rand.nextInt(150) + 300);
                break;
        }
        return starSystemCount;
    }

    public void createCivilizations(Galaxy universe, Random rand, int civCount, NameGenerator planetNameGenerator) {
        //Player civ
        Civilization playerCiv = new Civilization(gameState);
        playerCiv.setName(civilizationConfig.civilizationName);
        playerCiv.setColor(civilizationConfig.civColor);
        playerCiv.setHomePlanetName(civilizationConfig.homePlanetName);
        playerCiv.setSpeciesName(civilizationConfig.speciesName);
        playerCiv.setCivilizationPreferredClimate(civilizationConfig.civilizationPreferredClimate);
        LOGGER.info("Creating suitable planet");
        UniversePath up = createSuitablePlanet(playerCiv, universe, rand, planetNameGenerator);
        LOGGER.info("Done creating suitable planet");
        playerCiv.setStartingPlanet(up);
        //Generate Species
        Race playerSpecies = new Race(gameState, 1, 0.01f, civilizationConfig.speciesName);
        playerSpecies.setUpkeep(0.05f);

        gameState.addSpecies(playerSpecies);

        //Set currency
        Currency nationCurrency = new Currency(gameState);
        nationCurrency.setName(civilizationConfig.civCurrencyName);
        nationCurrency.setSymbol(civilizationConfig.civCurrencySymbol);
        playerCiv.setNationalCurrency(nationCurrency);
        nationCurrency.setController(playerCiv.getReference());

        playerCiv.setFoundingSpecies(playerSpecies);
        gameState.playerCiv = playerCiv.getReference();
        gameState.addCivilization(playerCiv);

        LOGGER.info("Done with player civ " + playerCiv.getName());

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
            Civilization civ = new Civilization(gameState);
            civ.setName(name);
            civ.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
            civ.setHomePlanetName(homePlanetNameGenerator.getName(0, rand));
            civ.setSpeciesName(name);
            RacePreferredClimateType civPreferredClimate1 = RacePreferredClimateType.values()[rand.nextInt(RacePreferredClimateType.values().length)];
            civ.setCivilizationPreferredClimate(civPreferredClimate1);
            UniversePath up1 = createSuitablePlanet(playerCiv, universe, rand, planetNameGenerator);
            civ.setStartingPlanet(up1);
            Race civSpecies = new Race(gameState, 1, 0.01f, "Race " + i);
            gameState.addSpecies(civSpecies);
            civ.setFoundingSpecies(civSpecies);

            //Create currency
            nationCurrency = new Currency(gameState);
            nationCurrency.setName("ISK");
            nationCurrency.setSymbol("Z");
            civ.setNationalCurrency(nationCurrency);
            nationCurrency.setController(civ.getReference());

            LOGGER.info("Done with civ " + civ.getName());
            //universe.
            gameState.addCivilization(civ);
        }
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
    private UniversePath createSuitablePlanet(Civilization c, Galaxy u, Random rand, NameGenerator planetNameGenerator) {
        //Make the things
        long dist = nextLong(rand, KM_IN_LTYR * 100);
        float degrees = (rand.nextFloat() * 360);
        StarSystem sys = new StarSystem(gameState, new PolarCoordinate(degrees, dist));
        u.addStarSystem(sys);
        //Add stars
        Star star = generateStar(rand);
        sys.addBody(star);

        int planetCount = rand.nextInt(5) + 6;
        long lastDistance = 10000000;
        Planet living = null;
        int k = 0;
        for (k = 0; k < planetCount; k++) {
            //Add planets
            int planetType = Math.round(rand.nextFloat());

            long orbitalDistance = (long) (lastDistance * (rand.nextDouble() + 1.5d));
            lastDistance = orbitalDistance;

            int planetSize = getRandomPlanetSize(planetType, rand);
            Planet p = generatePlanet(planetType, orbitalDistance, planetSize, rand);

            if (planetType == PlanetTypes.ROCK && living == null) {
                living = p;
            }
            //Set name
            if (planetNameGenerator != null) {
                p.setName(planetNameGenerator.getName(rand.nextInt(planetNameGenerator.getRulesCount()), rand));
            }

            sys.addBody(p);
        }

        if (living == null) {
            //Add a rocky planet just to be sure.
            int planetType = PlanetTypes.ROCK;
            //System.out.println(planetType);
            long orbitalDistance = (long) (lastDistance * (rand.nextDouble() + 1.5d));
            lastDistance = orbitalDistance;
            int planetSize = getRandomPlanetSize(planetType, rand);
            Planet p = generatePlanet(planetType, orbitalDistance, planetSize, rand);

            //Set name
            if (planetNameGenerator != null) {
                p.setName(planetNameGenerator.getName(rand.nextInt(planetNameGenerator.getRulesCount()), rand));
            }

            sys.addBody(p);

            living = p;
        }

        sys.calculateBoundary();
        //get a rocky planet
        return living.getUniversePath();
    }

    private Star generateStar(Random rand) {
        int starTypeSelection = rand.nextInt(10000);
        int starSize;
        int solarRadius = 695508;
        StarType starType;
        if (starTypeSelection < 7) {
            starType = StarType.TYPE_O;
            starSize = randint(rand, (int) (6.6 * solarRadius), (int) (100f * solarRadius));
        } else if (starTypeSelection < 20) {
            starType = StarType.TYPE_B;
            starSize = randint(rand, (int) (1.8 * solarRadius), (int) (6.6 * solarRadius));
        } else if (starTypeSelection < 90) {
            starType = StarType.TYPE_A;
            starSize = randint(rand, (int) (1.4 * solarRadius), (int) (1.8 * solarRadius));
        } else if (starTypeSelection < 390) {
            starType = StarType.TYPE_F;
            starSize = randint(rand, (int) (1.15 * solarRadius), (int) (1.4 * solarRadius));
        } else if (starTypeSelection < 1290) {
            starType = StarType.TYPE_G;
            starSize = randint(rand, (int) (0.96 * solarRadius), (int) (1.15 * solarRadius));
        } else if (starTypeSelection < 2500) {
            starType = StarType.TYPE_K;
            starSize = randint(rand, (int) (0.7 * solarRadius), (int) (0.96 * solarRadius));
        } else {
            starType = StarType.TYPE_M;
            starSize = randint(rand, (int) (0.1 * solarRadius), (int) (0.7 * solarRadius));
        }

        Star star = new Star(gameState, starType, starSize);
        return star;
    }

    private Planet generatePlanet(int planetType, double orbitalDistance, int planetSize, Random rand) {
        Planet p = new Planet(gameState, planetType, planetSize);

        generateResourceVeins(p);
        if (planetType == PlanetTypes.ROCK) {
            p.setTerrainSeed(random.nextInt());
            p.setTerrainColoringIndex(random.nextInt(TerrainColoring.NUMBER_OF_ROCKY_COLORS));
            //= terrainColorses;
        } else if (planetType == PlanetTypes.GAS) {
            p.setTerrainSeed(random.nextInt());
            p.setTerrainColoringIndex(random.nextInt(TerrainColoring.NUMBER_OF_GASSY_COLORS));
        }

        //Set changin degrees
        p.modDegrees(rand.nextInt(360));

        p.setSemiMajorAxis((double) orbitalDistance);

        //Seed life
        if (rand.nextDouble() <= (LIFE_OCCURANCE)) {
            generateLocalLife(p);
        }
        return p;
    }

    public void generateResourceVeins(Planet p) {
        int planetSize = p.getPlanetSize();

        //Find the amount of resources to add...
        //Sort through resources, and find suitable
        ArrayList<StoreableReference> toAdd = new ArrayList<>();
        for (Map.Entry<StoreableReference, ResourceDistribution> entry : gameState.oreDistributions.entrySet()) {
            StoreableReference key = entry.getKey();
            ResourceDistribution val = entry.getValue();
            double rarity = val.rarity;
            if (random.nextDouble() < rarity) {
                toAdd.add(key);
            }
        }

        //Add resource veins
        int idCount = randint(random, 5, 10);

        //Add the veins
        for (int i = 0; i < idCount; i++) {
            //Create strata
            Stratum stratum = new Stratum(gameState);

            //Add resources
            for (StoreableReference o : toAdd) {
                stratum.minerals.put(o, randint(random, 10000, 500_000));
            }

            //Select the things
            stratum.setRadius(randint(random, 3, planetSize));
            stratum.setX(random.nextInt(p.getPlanetWidth()));
            stratum.setY(random.nextInt(p.getPlanetHeight()));
            stratum.setDepth(random.nextInt(planetSize / 2) + 1);
            p.strata.add(stratum.getReference());
        }
    }

    private void generateLocalLife(Planet p) {
        //Get distance from planet and calculate...
        //Then the planet has life
        //Stages it has life, and how evolved it is
        int lifeLength = random.nextInt(10);

        //Get random name
        NameGenerator speciesNameGenerator = null;
        try {
            speciesNameGenerator = NameGenerator.getNameGenerator("species.names");
        } catch (IOException ex) {
        }

        //Initialize life
        Species micro = new Species(gameState, speciesNameGenerator.getName(0));
        //Set name
        //Add random trait
        LifeTrait randomLifeTrait = LifeTrait.values()[random.nextInt(LifeTrait.values().length)];
        micro.lifeTraits.add(randomLifeTrait);
        LocalLife life = new LocalLife();
        life.setSpecies(micro);

        p.localLife.add(life);

        for (int i = 0; i < lifeLength; i++) {
            //Evolve
        }
    }

    private int getRandomPlanetSize(int planetType, Random rand) {
        int planetSize;
        if (planetType == PlanetTypes.GAS) {
            planetSize = randint(rand, 50, 500);
        } else {
            //Rock
            planetSize = randint(rand, 30, 100);
        }
        return planetSize;
    }

    private long nextLong(Random rng, long n) {
        // error checking and 2^x checking removed for simplicity.
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits - val + (n - 1) < 0L);
        return val;
    }
}
