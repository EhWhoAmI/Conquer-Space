package ConquerSpace.game;

import static ConquerSpace.game.AssetReader.readEngineTechs;
import static ConquerSpace.game.AssetReader.readLaunchSystems;
import static ConquerSpace.game.AssetReader.readPopulationEvents;
import static ConquerSpace.game.AssetReader.readSatellites;
import static ConquerSpace.game.AssetReader.readShipComponents;
import static ConquerSpace.game.AssetReader.readShipTypes;
import ConquerSpace.game.buildings.AdministrativeCenter;
import ConquerSpace.game.buildings.City;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.buildings.FarmBuilding;
import ConquerSpace.game.buildings.Observatory;
import ConquerSpace.game.buildings.ResourceMinerDistrict;
import ConquerSpace.game.buildings.ResourceStorage;
import ConquerSpace.game.buildings.area.CapitolArea;
import ConquerSpace.game.life.Fauna;
import ConquerSpace.game.life.LifeTrait;
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.population.Species;
import ConquerSpace.game.science.Fields;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.ships.hull.HullMaterial;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.names.NameGenerator;
import java.io.IOException;
import java.util.Random;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author zyunl
 */
public class GameInitializer {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameInitializer.class.getName());

    private Universe universe;

    private StarDate starDate;

    private GameUpdater updater;

    public GameInitializer(Universe u, StarDate s, GameUpdater updater) {
        universe = u;
        starDate = s;
        this.updater = updater;
    }

    public void initGame() {
        //Init tech and fields
        Fields.readFields();
        Technologies.readTech();

        //All things to load go here!!!
        readLaunchSystems();
        readSatellites();
        readShipTypes();
        readShipComponents();
        readEngineTechs();

        //Events
        readPopulationEvents();

        //Do calculations for system position before initing for observataries
        updater.updateObjectPositions();

        //All the home planets of the civs are theirs.
        //Set home planet and sector
        Random selector = new Random(universe.getSeed());
        NameGenerator gen = null;
        try {
            gen = NameGenerator.getNameGenerator("us.names");
        } catch (IOException ex) {
            //Ignore
        }

        final int CIV_STARTING_TECH_PTS = 10;
        for (int i = 0; i < universe.getCivilizationCount(); i++) {
            Civilization c = universe.getCivilization(i);
            //Add templates
            //Science
            //Add fields
            c.fields = (Fields.toField(Fields.fieldNodeRoot));
            //Add all starting techs            
            for (Technology tech : Technologies.getTechsByTag("Starting")) {
                c.researchTech(tech);
            }

            //Select one of the space travel sciences
            Technology[] teks = Technologies.getTechsByTag("space travel base");
            //To research this
            c.civTechs.put(teks[selector.nextInt(teks.length)], 100);

            //Propulsion
            teks = Technologies.getTechsByTag("Propulsion");
            //To research this
            c.civTechs.put(teks[selector.nextInt(teks.length)], 100);

            //Add starting tech points...
            c.setTechPoints(CIV_STARTING_TECH_PTS);

            c.calculateTechLevel();

            //Add civ values
            c.putValue("optics.quality", 100);

            //Add researchers
            //Only one. Testing guy
            String name = "Person";
            if (gen != null) {
                name = gen.getName(Math.round(selector.nextFloat()));
            }

            Scientist r = new Scientist(name, 20);
            r.setSkill(1);
            c.people.add(r);

            //Add unrecruited people
            c.unrecruitedPeople.clear();
            int peopleCount = (int) (Math.random() * 5) + 5;

            for (int peep = 0; peep < peopleCount; peep++) {
                int age = (int) (Math.random() * 40) + 20;
                String person = "name";
                person = gen.getName((int) Math.round(Math.random()));
                Scientist nerd = new Scientist(person, age);
                nerd.setSkill((int) (Math.random() * 5) + 1);
                c.unrecruitedPeople.add(nerd);
            }

            //Admins
            peopleCount = (int) (Math.random() * 5) + 5;

            for (int peep = 0; peep < peopleCount; peep++) {
                int age = (int) (Math.random() * 40) + 20;
                String person = "name";
                person = gen.getName((int) Math.round(Math.random()));
                Administrator dude = new Administrator(person, age);
                //nerd.setSkill((int) (Math.random() * 5) + 1);
                c.unrecruitedPeople.add(dude);
            }
            HullMaterial material = new HullMaterial("Testing Hull Material", 100, 5, 12);
            material.setId(0);
            c.hullMaterials.add(material);

            UniversePath p = c.getStartingPlanet();

            if (universe.getSpaceObject(p) instanceof Planet) {
                Planet starting = (Planet) universe.getSpaceObject(p);
                c.setCapitalPlanet(starting);
                NameGenerator townGen = null;
                try {
                    townGen = NameGenerator.getNameGenerator("town.names");
                } catch (IOException ex) {
                    //Ignore, assume all ok
                }

                //Amount of pop storages
                int popStorMas = (selector.nextInt(5) + 3);

                //Add admin center and capital city.
                AdministrativeCenter administrativeCenter = new AdministrativeCenter();

                for (int count = 0; count < popStorMas; count++) {
                    City city = new City(starting.getUniversePath());
                    city.setName(townGen.getName(0));

                    CityDistrict test;
                    test = new CityDistrict();

                    if (count == 0) {
                        test = administrativeCenter;
                        c.setCapitalCity(city);
                        //Add the capitol areas
                        test.areas.add(new CapitolArea());
                    }

                    test.setMaxStorage(selector.nextInt(15) + 1);
                    //Distribute
                    //Add random positions
                    int popCount = (selector.nextInt(10) + 1);
                    test.setMaxStorage(selector.nextInt(popCount + 5) + 1);

                    for (int k = 0; k < popCount; k++) {
                        //Add a couple of population to the mix...
                        PopulationUnit u = new PopulationUnit(c.getFoundingSpecies());
                        u.setSpecies(c.getFoundingSpecies());
                        c.population.add(u);
                        test.population.add(u);
                    }
                    city.storages.add(test);
                    int x = (selector.nextInt(starting.getPlanetSize() * 2 - 2) + 1);
                    int y = (selector.nextInt(starting.getPlanetSize() - 2) + 1);
                    ConquerSpace.game.universe.Point pt = new ConquerSpace.game.universe.Point(x, y);
                    starting.buildings.put(pt, test);

                    //Expand sector
                    //Choose a direction, and expand...
                    CityDistrict test2 = new CityDistrict();
                    int dir = selector.nextInt(4) + 1;
                    ConquerSpace.game.universe.Point pt2;
                    switch (dir) {
                        case 0:
                            pt2 = new ConquerSpace.game.universe.Point(pt.getX(), pt.getY() + 1);
                            break;
                        case 1:
                            pt2 = new ConquerSpace.game.universe.Point(pt.getX(), pt.getY() - 1);
                            break;
                        case 2:
                            pt2 = new ConquerSpace.game.universe.Point(pt.getX() + 1, pt.getY());
                            break;
                        case 3:
                            pt2 = new ConquerSpace.game.universe.Point(pt.getX() - 1, pt.getY());
                            break;
                        default:
                            pt2 = new ConquerSpace.game.universe.Point(pt.getX(), pt.getY() + 1);
                    }
                    starting.buildings.put(pt2, test2);
                    int popCount2 = selector.nextInt(10);
                    test2.setMaxStorage(selector.nextInt(popCount2 + 5) + 1);

                    for (int k = 0; k < popCount2; k++) {
                        //Add a couple of population to the mix...
                        PopulationUnit u = new PopulationUnit(c.getFoundingSpecies());
                        u.setSpecies(c.getFoundingSpecies());
                        c.population.add(u);
                        test2.population.add(u);
                    }
                    city.storages.add(test2);
                    //Set growth
                    //Add city
                    starting.cities.add(city);
                }

                //Add storage
                ResourceStorage storage = new ResourceStorage(starting);
                //Add all possible resources
                //Get starting resources...
                for (Resource res : GameController.resources) {
                    storage.addResourceTypeStore(res);
                }

                int x = (selector.nextInt(starting.getPlanetSize() * 2 - 2) + 1);
                int y = (selector.nextInt(starting.getPlanetSize() - 2) + 1);
                ConquerSpace.game.universe.Point pt = new ConquerSpace.game.universe.Point(x, y);

                while (starting.buildings.containsKey(pt)) {
                    x = (selector.nextInt(starting.getPlanetSize() * 2 - 2) + 1);
                    y = (selector.nextInt(starting.getPlanetSize() - 2) + 1);
                    pt = new ConquerSpace.game.universe.Point(x, y);
                }
                c.resourceStorages.add(storage);
                starting.buildings.put(pt, storage);

                //Add resource miner               
                for (Resource resource : GameController.resources) {
                    createResourceMiners(starting, resource, c.getFoundingSpecies());
                }

                //Add observetory
                StarSystem container = universe.getStarSystem(starting.getParentStarSystem());
                Observatory observatory = new Observatory(10 * GameController.AU_IN_LTYR, 100, c.getID(),
                        new ConquerSpace.game.universe.Point(container.getX(), container.getY()));

                x = (selector.nextInt(starting.getPlanetSize() * 2 - 2) + 1);
                y = (selector.nextInt(starting.getPlanetSize() - 2) + 1);
                pt = new ConquerSpace.game.universe.Point(x, y);

                while (starting.buildings.containsKey(pt)) {
                    x = (selector.nextInt(starting.getPlanetSize() * 2 - 2) + 1);
                    y = (selector.nextInt(starting.getPlanetSize() - 2) + 1);
                    pt = new ConquerSpace.game.universe.Point(x, y);
                }

                c.visionPoints.add(observatory);
                starting.buildings.put(pt, observatory);

                createFarms(starting, selector, c);
                //Add ship
                //Ship s = new Ship(new ShipClass("test", new Hull(1, 1, material, 70, 0, "adsdf")), 0, 0, new Vector(0, 0), starting.getUniversePath());
                //s.setEstimatedThrust(5000);
                //Actions.launchShip(s, starting, c);
                //Ship s2 = new Ship(new ShipClass("test", new Hull(1, 1, material, 70, 0, "adsdf")), 0, 0, new Vector(0, 0), starting.getUniversePath());
                //s2.setEstimatedThrust(10_000_000);
                //Actions.launchShip(s2, starting, c);
                //Set ownership
                starting.setOwnerID(c.getID());
                starting.scanned.add(c.getID());
                starting.setHabitated(true);
                starting.setName(c.getHomePlanetName());

                c.habitatedPlanets.add(starting);

                //Add resources
                for (Resource res : GameController.resources) {
                    c.resourceList.put(res, 0);
                }

                //Add Civ initalize values
                c.values.put("haslaunch", 0);
                LOGGER.info("Civ " + c.getName() + " Starting planet: " + starting.getUniversePath());
            }
        }

        updater.calculateControl();

        updater.calculateVision();
    }

    private void createResourceMiners(Planet p, Resource r, Species founding) {
        //Find if vein exists on the planet
        for (ResourceVein v : p.resourceVeins) {
            //Get the resource vein and stuff
            if (v.getResourceType().equals(r)) {
                //Then place it in the center
                ResourceMinerDistrict miner = new ResourceMinerDistrict(v, 10);

                miner.population.add(new PopulationUnit(founding));
                p.buildings.put(new Point(v.getX(), v.getY()), miner);
                break;
            }
        }
    }

    private void createFarms(Planet starting, Random selector, Civilization c) {
        //Based on population
        //Add livestock
        //Create a test crop so that you can grow stuff
        Fauna faun = new Fauna(0.01f);
        faun.lifetraits.add(LifeTrait.Delicious);
        faun.lifetraits.add(LifeTrait.Photosynthetic);
        faun.setName("Potatoe");
        faun.setBiomass(100_000);

        starting.localLife.add(faun);

        int x = (selector.nextInt(starting.getPlanetSize() * 2 - 2) + 1);
        int y = (selector.nextInt(starting.getPlanetSize() - 2) + 1);
        ConquerSpace.game.universe.Point pt = new ConquerSpace.game.universe.Point(x, y);

        for (int i = 0; i < 4; i++) {
            FarmBuilding faceBook = new FarmBuilding(FarmBuilding.FarmType.Crop);
            faceBook.farmCreatures.add(faun);
            faceBook.setProductivity(250);
            //The biomass capacity
            faceBook.setMaxCapacity(250);
            faceBook.setCapacity(10);
            //Add a farm
            do {
                x = (selector.nextInt(starting.getPlanetSize() * 2 - 2) + 1);
                y = (selector.nextInt(starting.getPlanetSize() - 2) + 1);
                pt = new ConquerSpace.game.universe.Point(x, y);
            } while (starting.buildings.containsKey(pt));
            //Add population
            PopulationUnit u = new PopulationUnit(c.getFoundingSpecies());
            u.setSpecies(c.getFoundingSpecies());
            c.population.add(u);
            faceBook.getPopulationArrayList().add(u);
            starting.buildings.put(pt, faceBook);
        }
    }
}
