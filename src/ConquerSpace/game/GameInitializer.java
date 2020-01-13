package ConquerSpace.game;

import static ConquerSpace.game.AssetReader.*;
import ConquerSpace.game.buildings.AdministrativeCenter;
import ConquerSpace.game.buildings.City;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.buildings.FarmBuilding;
import ConquerSpace.game.buildings.IndustrialDistrict;
import ConquerSpace.game.buildings.Observatory;
import ConquerSpace.game.buildings.ResourceMinerDistrict;
import ConquerSpace.game.buildings.ResourceStorage;
import ConquerSpace.game.buildings.area.CapitolArea;
import ConquerSpace.game.buildings.area.industrial.ForgeArea;
import ConquerSpace.game.life.LifeTrait;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.life.Species;
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.science.Fields;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.resources.farm.Crop;
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

    final int CIV_STARTING_TECH_PTS = 10;

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
        readPersonalityTraits();

        readBuildingCosts();

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

        for (int i = 0; i < universe.getCivilizationCount(); i++) {
            Civilization c = universe.getCivilization(i);
            //Add templates
            //Science
            initializeTech(c, selector);

            initalizeCivValues(c);

            HullMaterial material = new HullMaterial("Testing Hull Material", 100, 5, 12);
            material.setId(0);
            c.hullMaterials.add(material);

            UniversePath p = c.getStartingPlanet();

            if (universe.getSpaceObject(p) instanceof Planet) {
                Planet starting = (Planet) universe.getSpaceObject(p);
                c.setCapitalPlanet(starting);
                
                //Add resource miners
                createResourceMiners(starting, c, c.getFoundingSpecies());

                createResourceStorages(c, selector, starting);

                createPopulationStorages(starting, c, selector);

                createFarms(starting, selector, c);

                createIndustrialZones(c, selector, starting);
                
                createObservatory(starting, c, selector);

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

                //Deal with people
                initalizeRecruitedPeople(c, gen, selector);

                //Add unrecruited people
                createUnrecruitedPeople(c, starting, gen);
            }
        }

        updater.calculateControl();

        updater.calculateVision();
    }

    private void createResourceMiners(Planet p, Civilization c, Race founding) {
        //Find if vein exists on the planet
        int i = 0;
        for (ResourceVein v : p.resourceVeins) {
            //Get the resource vein and stuff
            //Then place it in the center
            ResourceMinerDistrict miner = new ResourceMinerDistrict(v, 10);
            //System.out.println
            miner.setOwner(c);
            miner.setScale(1);

            miner.population.add(new PopulationUnit(founding));
            p.buildings.put(new GeographicPoint(v.getX(), v.getY()), miner);
        }
    }

    private void createFarms(Planet starting, Random selector, Civilization c) {
        //Based on population
        //Add livestock
        //Create a test crop so that you can grow stuff
        Species potato = new Species();
        potato.setName("Potato");
        potato.lifeTraits.add(LifeTrait.Rooted);
        potato.lifeTraits.add(LifeTrait.Delicious);
        potato.lifeTraits.add(LifeTrait.Photosynthetic);

        //Add the biomass
        //Set the amount on planet...
        LocalLife localLife = new LocalLife();
        localLife.setSpecies(potato);
        localLife.setBiomass(100_000);
        starting.localLife.add(localLife);
//        Fauna faun = new Fauna(0.01f);
//        faun.lifetraits.add(LifeTrait.Delicious);
//        faun.lifetraits.add(LifeTrait.Photosynthetic);
//        faun.setName("Potatoe");
//        faun.setBiomass(100_000);
//        starting.localLife.add(faun);
        for (int i = 0; i < 10; i++) {
            FarmBuilding faceBook = new FarmBuilding(FarmBuilding.FarmType.Crop);
            //Add crops
            Crop crop = new Crop(potato);
            crop.setTimeLeft(25);
            crop.setYield(10000);
            faceBook.crops.add(crop);
            //faceBook.farmCreatures.add(faun);
            faceBook.setProductivity(1000);
            //The biomass capacity
            faceBook.setMaxCapacity(5000);
            faceBook.setCapacity(1000);
            faceBook.setManpower(10);
            faceBook.setOwner(c);
            //Add a farm
            GeographicPoint pt = getRandomEmptyPoint(starting, selector);
            //Add population
            PopulationUnit u = new PopulationUnit(c.getFoundingSpecies());
            u.setSpecies(c.getFoundingSpecies());
            c.population.add(u);
            faceBook.getPopulationArrayList().add(u);
            starting.buildings.put(pt, faceBook);
        }
    }

    private void createObservatory(Planet starting, Civilization c, Random selector) {
        //Add observetory
        StarSystem container = universe.getStarSystem(starting.getParentStarSystem());
        Observatory observatory = new Observatory(10 * GameController.AU_IN_LTYR, 100, c.getID(),
                new ConquerSpace.game.universe.Point(container.getX(), container.getY()));

        observatory.setOwner(c);
        GeographicPoint pt = getRandomEmptyPoint(starting, selector);

        c.visionPoints.add(observatory);
        starting.buildings.put(pt, observatory);
    }

    private void createResourceStorages(Civilization c, Random selector, Planet starting) {
        //Add storage
        ResourceStorage storage = new ResourceStorage(starting);
        storage.setOwner(c);
        //Add all possible resources
        //Get starting resources...
        for (Resource res : GameController.resources) {
            storage.addResourceTypeStore(res);
        }

        GeographicPoint pt = getRandomEmptyPoint(starting, selector);

        c.resourceStorages.add(storage);
        starting.buildings.put(pt, storage);
    }

    private void createIndustrialZones(Civilization c, Random selector, Planet starting) {
        for (int i = 0; i < 10; i++) {
            IndustrialDistrict district = new IndustrialDistrict();
            //Add areas
            district.areas.add(new ForgeArea());
            GeographicPoint pt = getRandomEmptyPoint(starting, selector);
            starting.buildings.put(pt, district);
        }
    }

    private void createPopulationStorages(Planet starting, Civilization c, Random selector) {
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

            test.setMaxStorage(selector.nextInt(30) + 1);
            test.setOwner(c);
            //Distribute
            //Add random positions
            int popCount = (selector.nextInt(25) + 1);
            test.setMaxStorage(selector.nextInt(popCount + 5) + 1);

            for (int k = 0; k < popCount; k++) {
                //Add a couple of population to the mix...
                PopulationUnit u = new PopulationUnit(c.getFoundingSpecies());
                u.setSpecies(c.getFoundingSpecies());
                c.population.add(u);
                test.population.add(u);
            }
            city.addDistrict(test);

            GeographicPoint pt = getRandomEmptyPoint(starting, selector);

            starting.buildings.put(pt, test);

            //Expand sector
            //Choose a direction, and expand...
            CityDistrict test2 = new CityDistrict();
            int dir = selector.nextInt(4) + 1;
            GeographicPoint pt2;
            switch (dir) {
                case 0:
                    pt2 = new GeographicPoint(pt.getX(), pt.getY() + 1);
                    break;
                case 1:
                    pt2 = new GeographicPoint(pt.getX(), pt.getY() - 1);
                    break;
                case 2:
                    pt2 = new GeographicPoint(pt.getX() + 1, pt.getY());
                    break;
                case 3:
                    pt2 = new GeographicPoint(pt.getX() - 1, pt.getY());
                    break;
                default:
                    pt2 = new GeographicPoint(pt.getX(), pt.getY() + 1);
            }
            starting.buildings.put(pt2, test2);
            int popCount2 = (selector.nextInt(25) + 1);
            test2.setMaxStorage(selector.nextInt(popCount2 + 5) + 1);
            test2.setOwner(c);
            for (int k = 0; k < popCount2; k++) {
                //Add a couple of population to the mix...
                PopulationUnit u = new PopulationUnit(c.getFoundingSpecies());
                u.setSpecies(c.getFoundingSpecies());
                c.population.add(u);
                test2.population.add(u);
            }
            //test2.setCity(city);
            city.addDistrict(test2);
            //Set growth
            //Add city
            starting.cities.add(city);
        }
    }

    private void createCityDistrict(Planet starting, Civilization c, ConquerSpace.game.universe.Point pt, City city) {

    }

    private void createUnrecruitedPeople(Civilization c, Planet homePlanet, NameGenerator gen) {
        c.unrecruitedPeople.clear();
        int peopleCount = (int) (Math.random() * 5) + 5;

        for (int peep = 0; peep < peopleCount; peep++) {
            int age = (int) (Math.random() * 40) + 20;
            String person = "name";
            person = gen.getName((int) Math.round(Math.random()));
            Scientist nerd = new Scientist(person, age);
            nerd.setSkill((int) (Math.random() * 5) + 1);
            nerd.traits.add(GameController.personalityTraits.get((int) (GameController.personalityTraits.size() * Math.random())));

            //Set location
            nerd.setPosition(c.getCapitalCity());

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
            dude.traits.add(GameController.personalityTraits.get((int) (GameController.personalityTraits.size() * Math.random())));
            dude.setPosition(c.getCapitalCity());

            c.unrecruitedPeople.add(dude);
        }
    }

    private void initializeTech(Civilization c, Random selector) {
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
    }

    private void initalizeRecruitedPeople(Civilization c, NameGenerator gen, Random selector) {
        //Add researchers
        //Only one. Testing guy
        String name = "Person";
        if (gen != null) {
            name = gen.getName(Math.round(selector.nextFloat()));
        }

        Scientist r = new Scientist(name, 20);
        r.setSkill(1);
        //Add random trait 
        r.traits.add(GameController.personalityTraits.get((int) (GameController.personalityTraits.size() * Math.random())));
        r.setPosition(c.getCapitalCity());

        c.people.add(r);
    }

    private void initalizeCivValues(Civilization c) {
        //Add civ values
        c.putValue("optics.quality", 100);
    }

    private GeographicPoint getRandomEmptyPoint(Planet starting, Random selector) {
        GeographicPoint pt;

        do {
            int x = (selector.nextInt(starting.getPlanetSize() * 2 - 2) + 1);
            int y = (selector.nextInt(starting.getPlanetSize() - 2) + 1);
            pt = new GeographicPoint(x, y);
        } while (starting.buildings.containsKey(pt));
        return pt;
    }
}
