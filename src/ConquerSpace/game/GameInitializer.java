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
package ConquerSpace.game;

import ConquerSpace.game.buildings.AdministrativeCenter;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.City;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.buildings.FarmBuilding;
import ConquerSpace.game.buildings.IndustrialDistrict;
import ConquerSpace.game.buildings.InfrastructureBuilding;
import ConquerSpace.game.buildings.Observatory;
import ConquerSpace.game.buildings.ResourceMinerDistrict;
import ConquerSpace.game.buildings.ResourceStorage;
import ConquerSpace.game.buildings.area.CapitolArea;
import ConquerSpace.game.buildings.area.ResearchArea;
import ConquerSpace.game.buildings.area.industrial.Factory;
import ConquerSpace.game.buildings.area.infrastructure.PowerPlantArea;
import ConquerSpace.game.life.LifeTrait;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.life.Species;
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.science.Field;
import ConquerSpace.game.science.Fields;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.government.Government;
import ConquerSpace.game.universe.civilization.government.GovernmentPosition;
import ConquerSpace.game.universe.civilization.government.HeritableGovernmentPosition;
import ConquerSpace.game.universe.civilization.government.PoliticalPowerSource;
import ConquerSpace.game.universe.civilization.government.PoliticalPowerTransitionMethod;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.ProductionProcess;
import ConquerSpace.game.universe.resources.Stratum;
import ConquerSpace.game.universe.farm.Crop;
import ConquerSpace.game.universe.ships.hull.HullMaterial;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.util.logging.CQSPLogger;
import ConquerSpace.util.names.NameGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
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

        //Memory saving tricks
        universe.civs.trimToSize();
        universe.species.trimToSize();
        universe.spaceShips.trimToSize();
        universe.starSystems.trimToSize();

        for (int i = 0; i < universe.getCivilizationCount(); i++) {
            Civilization c = universe.getCivilization(i);
            //Add templates
            initVision(c, universe);

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

                createResourceStorages(c, starting, selector);

                createPopulationStorages(starting, c, selector);

                createFarms(starting, c, selector);

                createIndustrialZones(c, selector, starting);

                createObservatory(starting, c, selector);

                //Add infrastructure
                createInfrastructure(starting, selector);

                addResearchInstitutions(starting, c, selector);

                nameStratumOnPlanet(starting);

                //Set ownership
                starting.setOwnerID(c.getID());
                starting.scanned.add(c.getID());
                starting.setHabitated(true);
                starting.setName(c.getHomePlanetName());

                c.habitatedPlanets.add(starting);

                //Add resources
//                for (Good res : GameController.ores) {
//                    c.resourceList.put(res, 0);
//                }
                LOGGER.info("Civ " + c.getName() + " Starting planet: " + starting.getUniversePath());

                //Deal with people
                initalizeRecruitedPeople(c, gen, selector);

                //Add unrecruited people
                createUnrecruitedPeople(c, starting, gen);

                //
                initializeGovernment(c, gen);

            }
            c.government.officials.get(c.government.headofState).setPosition(c.getCapitalCity());
        }

        updater.calculateControl();

        updater.calculateVision();
    }

    private void createResourceMiners(Planet p, Civilization c, Race founding) {
        City city = new City(p.getUniversePath());
        city.setName("Mines");
        //Find if vein exists on the planet
        int minerCount = (int) (Math.random() * p.getPlanetSize());
        minerCount += 5;
        for (int i = 0; i < minerCount; i++) {
            //Select random vein
            int id = (int) (p.strata.size() * Math.random());
            Stratum strata = p.strata.get(id);
            ResourceMinerDistrict miner = new ResourceMinerDistrict(strata, 10);

            //Set the type of resource to mine
            ArrayList<Good> a = new ArrayList<>(strata.minerals.keySet());
            Good g = a.get((int) (a.size() * Math.random()));

            miner.setResourceMining(g);
            miner.setOwner(c);
            miner.setScale(1);

            double randR = (strata.getRadius() * Math.sqrt(Math.random()));
            double theta = (Math.random() * 2 * Math.PI);

            int x = (int) (Math.cos(theta) * randR);
            int y = (int) (Math.sin(theta) * randR);

            miner.population.add(new PopulationUnit(founding));
            p.buildings.put(new GeographicPoint(x + strata.getX(), y + strata.getY()), miner);
            city.addDistrict(miner);
        }
//        for (ResourceVein v : p.resourceVeins) {
//            //Get the resource vein and stuff
//            //Then place it in the center
//            ResourceMinerDistrict miner = new ResourceMinerDistrict(v, 10);
//            //System.out.println
//            miner.setOwner(c);
//            miner.setScale(1);
//
//            miner.population.add(new PopulationUnit(founding));
//            p.buildings.put(new GeographicPoint(v.getX(), v.getY()), miner);
//            city.addDistrict(miner);
//        }
        p.cities.add(city);
    }

    private void createFarms(Planet starting, Civilization c, Random selector) {
        //Based on population
        //Add livestock
        //Create a test crop so that you can grow stuff
        City farmCity = new City(starting.getUniversePath());
        farmCity.setName("Farms");
        Species potato = new Species("Potato");
        potato.lifeTraits.add(LifeTrait.Rooted);
        potato.lifeTraits.add(LifeTrait.Delicious);
        potato.lifeTraits.add(LifeTrait.Photosynthetic);

        //Add the biomass
        //Set the amount on planet...
        LocalLife localLife = new LocalLife();
        localLife.setSpecies(potato);
        localLife.setBiomass(100_000);
        starting.localLife.add(localLife);
        InfrastructureBuilding infrastructureBuilding = new InfrastructureBuilding();

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
            farmCity.addDistrict(faceBook);
            infrastructureBuilding.connectedTo.add(faceBook);
            faceBook.infrastructure.add(infrastructureBuilding);
        }

        PowerPlantArea powerPlant = new PowerPlantArea();
        //Get the resources needed for powering plant
        //TODO sort out resources needed for powerplants
        /*Resource resource = null;
        for (Resource res : GameController.resources) {
            for (String tag : res.getTags()) {
                if (tag.equals("energy")) {
                    resource = res;
                    break;
                }
            }
        }*/

        powerPlant.setMaxVolume(1000);
        infrastructureBuilding.areas.add(powerPlant);
        //Connect it to many buildings
        starting.buildings.put(getRandomEmptyPoint(starting, selector), infrastructureBuilding);
        starting.cities.add(farmCity);
    }

    private void createObservatory(Planet starting, Civilization c, Random selector) {
        //Add observetory
        StarSystem container = universe.getStarSystem(starting.getParentStarSystem());
        Observatory observatory = new Observatory(10 * GameController.AU_IN_LTYR, 100, c.getID(),
                new ConquerSpace.game.universe.Point((long) container.getX(), (long) container.getY()));

        observatory.setOwner(c);
        GeographicPoint pt = getRandomEmptyPoint(starting, selector);

        c.visionPoints.add(observatory);
        starting.buildings.put(pt, observatory);
    }

    private void createResourceStorages(Civilization c, Planet starting, Random selector) {
        //Add storage
        ResourceStorage storage = new ResourceStorage(starting);
        storage.setOwner(c);
        //Add all possible resources

        GeographicPoint pt = getRandomEmptyPoint(starting, selector);

        c.resourceStorages.add(storage);
        starting.buildings.put(pt, storage);
    }

    private void createIndustrialZones(Civilization c, Random selector, Planet starting) {
        City cit = new City(starting.getUniversePath());
        for (int i = 0; i < 10; i++) {
            IndustrialDistrict district = new IndustrialDistrict();
            //Add areas
            for (ProductionProcess proc : c.productionProcesses) {
                //Add new factory
                Factory factory = new Factory(proc);
                district.areas.add(factory);
            }

            for (int k = 0; k < 5; k++) {
                PopulationUnit u = new PopulationUnit(c.getFoundingSpecies());
                u.setSpecies(c.getFoundingSpecies());
                c.population.add(u);
                district.getPopulationArrayList().add(u);
            }
            GeographicPoint pt = getRandomEmptyPoint(starting, selector);
            starting.buildings.put(pt, district);

            //Add to city
            cit.buildings.add(district);
        }
        starting.cities.add(cit);
    }

    private void createPopulationStorages(Planet starting, Civilization c, Random selector) {
        NameGenerator townGen = null;
        NameGenerator gen = null;

        try {
            townGen = NameGenerator.getNameGenerator("town.names");
            gen = NameGenerator.getNameGenerator("us.names");
        } catch (IOException ex) {
            //Ignore, assume all ok
        }

        //Amount of pop storages
        int popStorMas = (selector.nextInt(7) + 5);

        //Add admin center and capital city.
        AdministrativeCenter administrativeCenter = new AdministrativeCenter();

        for (int count = 0; count < popStorMas; count++) {
            City city = new City(starting.getUniversePath());
            city.setName(townGen.getName(0));
            CityDistrict district;
            district = new CityDistrict();

            if (count == 0) {
                district = administrativeCenter;
                c.setCapitalCity(city);
                //Add the capitol areas
                district.areas.add(new CapitolArea());
            }

            district.setMaxStorage(selector.nextInt(30) + 1);
            district.setOwner(c);
            //Distribute
            //Add random positions
            int popCount = (selector.nextInt(25) + 1);
            district.setMaxStorage(selector.nextInt(popCount + 5) + 1);

            for (int k = 0; k < popCount; k++) {
                //Add a couple of population to the mix...
                PopulationUnit u = new PopulationUnit(c.getFoundingSpecies());
                u.setSpecies(c.getFoundingSpecies());
                c.population.add(u);
                district.population.add(u);
            }
            city.addDistrict(district);

            GeographicPoint pt = getRandomEmptyPoint(starting, selector);

            starting.buildings.put(pt, district);

            //Expand sector
            //Choose a direction, and expand...
            CityDistrict district2 = new CityDistrict();
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
            starting.buildings.put(pt2, district2);
            int popCount2 = (selector.nextInt(25) + 1);
            district2.setMaxStorage(selector.nextInt(popCount2 + 5) + 1);
            district2.setOwner(c);
            for (int k = 0; k < popCount2; k++) {
                //Add a couple of population to the mix...
                PopulationUnit u = new PopulationUnit(c.getFoundingSpecies());
                u.setSpecies(c.getFoundingSpecies());
                c.population.add(u);
                district2.population.add(u);
            }
            //test2.setCity(city);
            city.addDistrict(district2);

            //Add leader to city
            Administrator gov = new Administrator(gen.getName(Math.round(selector.nextFloat())), 42);
            city.setGovernor(gov);

            c.people.add(gov);
            PeopleProcessor.placePerson(city, gov);
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
        PeopleProcessor.placePerson(c.getCapitalCity(), r);

        c.people.add(r);
    }

    private void initalizeCivValues(Civilization c) {
        //Add civ values
        c.putValue("optics.quality", 100);
        c.putValue("haslaunch", 0);
    }

    private GeographicPoint getRandomEmptyPoint(Planet planet, Random selector) {
        GeographicPoint pt;

        do {
            int x = (selector.nextInt(planet.getPlanetWidth() - 2) + 1);
            int y = (selector.nextInt(planet.getPlanetHeight()- 2) + 1);
            pt = new GeographicPoint(x, y);
        } while (planet.buildings.containsKey(pt));
        return pt;
    }

    private void initializeGovernment(Civilization c, NameGenerator gen) {
        //Create person
        int age = (int) (Math.random() * 40) + 20;
        String person = "name";
        person = gen.getName((int) Math.round(Math.random()));
        Administrator dude = new Administrator(person, 400);
        //nerd.setSkill((int) (Math.random() * 5) + 1);
        dude.traits.add(GameController.personalityTraits.get((int) (GameController.personalityTraits.size() * Math.random())));
        dude.setPosition(c.getCapitalCity());
        dude.setRole("Ruling " + c.getSpeciesName());

        c.government = new Government();
        //Because democracy is for noobs
        c.government.politicalPowerSource = PoliticalPowerSource.Autocracy;
        //Set leader
        HeritableGovernmentPosition leader = new HeritableGovernmentPosition();
        leader.setName("God-Emperor");
        leader.setMethod(PoliticalPowerTransitionMethod.Inherit);
        c.government.officials.put(leader, dude);
        c.government.headofGovernment = leader;
        c.government.headofState = leader;
        PeopleProcessor.placePerson(c.getCapitalCity(), dude);
        dude.position = leader;
        c.employ(dude);

        person = gen.getName((int) Math.round(Math.random()));

        Administrator crownPrince = new Administrator(person, 0);
        //nerd.setSkill((int) (Math.random() * 5) + 1);
        crownPrince.traits.add(GameController.personalityTraits.get((int) (GameController.personalityTraits.size() * Math.random())));
        crownPrince.setPosition(c.getCapitalCity());
        crownPrince.setRole("Loafing around -- Lazy brat");
        //Add heir to the throne of the GOD EMPEROR

        GovernmentPosition crownPrincePosition = new GovernmentPosition();
        c.government.officials.put(crownPrincePosition, crownPrince);
        crownPrincePosition.setName("Crown Prince");
        leader.nextInLine = crownPrincePosition;
        crownPrince.position = crownPrincePosition;

        PeopleProcessor.placePerson(c.getCapitalCity(), crownPrince);
        c.employ(crownPrince);
    }

    private void initVision(Civilization c, Universe u) {
        for (int i = 0; i < u.getStarSystemCount(); i++) {
            StarSystem s = u.getStarSystem(i);
            c.vision.put(new UniversePath(i), VisionTypes.UNDISCOVERED);
            for (int h = 0; h < s.getPlanetCount(); h++) {
                //Add planets
                c.vision.put(new UniversePath(i, h), VisionTypes.UNDISCOVERED);
            }
            for (int h2 = 0; h2 < s.getStarCount(); h2++) {
                c.vision.put(new UniversePath(i, h2, true), VisionTypes.UNDISCOVERED);
            }
        }
    }

    private void createInfrastructure(Planet p, Random selector) {
        //Adds the infrastructure to the planet...
        //Get building count
        int buildingCount = p.buildings.size();
        //Process them
        Collection<Building> buildings = new ArrayList<>(p.buildings.values());
        Iterator<Building> buildingIterator = buildings.iterator();
        int infrastructureCount = (buildingCount / 5);
        for (int k = 0; k < infrastructureCount; k++) {
            //Create infrastructure building for multiple things
            InfrastructureBuilding building = new InfrastructureBuilding();

            //Ensure point is near the thing
            //pt2 = getRandomEmptyPoint(starting, selector);
            //Add areas
            PowerPlantArea powerPlant = new PowerPlantArea();
            //Get the resources needed for powering plant
            //TODO: choose energy resource
            /*Resource resource = null;
            for (Resource res : GameController.resources) {
                for (String tag : res.getTags()) {
                    if (tag.equals("energy")) {
                        resource = res;
                        break;
                    }
                }
            }*/

            //Add the districts
            for (int i = 0; i < 5; i++) {
                Building b = buildingIterator.next();
                building.addBuilding(b);
            }

            powerPlant.setMaxVolume(1000);
            building.areas.add(powerPlant);
            //Add infrastructure
            p.buildings.put(getRandomEmptyPoint(p, selector), building);
        }
    }

    private PowerPlantArea createPowerPlant() {
        return null;
    }

    private void nameStratumOnPlanet(Planet p) {
        NameGenerator gen = null;
        try {
            gen = NameGenerator.getNameGenerator("strata.names");
        } catch (IOException ex) {
            //Ignore
        }
        for (Stratum strata : p.strata) {
            strata.setName(gen.getName(0));
        }
    }

    private void addResearchInstitutions(Planet p, Civilization c, Random selector) {
        NameGenerator gen = null;
        try {
            gen = NameGenerator.getNameGenerator("uni.names");
        } catch (IOException ex) {
            //Ignore
        }

        int count = p.cities.size();
        for (int i = 0; i < count; i++) {
            //Add to city
            String name = gen.getName(0);
            ResearchArea research = new ResearchArea();
            research.setName(name);

            //Add fields
            ArrayList<Field> fields = new ArrayList<>();
            //Remove the first one
            Field toFind = c.fields.getNode(0); //Because everyone does science for now, not magic when we add it...
            toFind.getFieldsExclusivse(fields);

            //Choose random field
            Field toAdd = fields.get(selector.nextInt(fields.size()));
            research.focusFields.put(toAdd, 0);
            

            //Choose random fields
            p.cities.get(i).buildings.get(0).areas.add(research);
        }
    }
}
