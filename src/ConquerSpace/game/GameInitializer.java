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

import ConquerSpace.game.districts.District;
import ConquerSpace.game.districts.City;
import ConquerSpace.game.districts.InfrastructureBuilding;
import ConquerSpace.game.districts.Observatory;
import ConquerSpace.game.districts.ResourceStorage;
import ConquerSpace.game.buildings.area.CapitolArea;
import ConquerSpace.game.buildings.area.FarmFieldArea;
import ConquerSpace.game.buildings.area.ManufacturerArea;
import ConquerSpace.game.buildings.area.MineArea;
import ConquerSpace.game.buildings.area.ResearchArea;
import ConquerSpace.game.buildings.area.PowerPlantArea;
import ConquerSpace.game.buildings.area.ResidentialArea;
import ConquerSpace.game.life.LifeTrait;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.life.Species;
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.science.Field;
import ConquerSpace.game.science.Fields;
import ConquerSpace.game.science.tech.Technologies;
import ConquerSpace.game.science.tech.Technology;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.bodies.Body;
import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.civilization.government.Government;
import ConquerSpace.game.civilization.government.GovernmentPosition;
import ConquerSpace.game.civilization.government.HeritableGovernmentPosition;
import ConquerSpace.game.civilization.government.PoliticalPowerSource;
import ConquerSpace.game.civilization.government.PoliticalPowerTransitionMethod;
import ConquerSpace.game.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.ProductionProcess;
import ConquerSpace.game.universe.resources.Stratum;
import ConquerSpace.game.buildings.farm.Crop;
import ConquerSpace.game.population.Culture;
import ConquerSpace.game.population.PopulationSegment;
import ConquerSpace.game.ships.hull.HullMaterial;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.util.logging.CQSPLogger;
import ConquerSpace.util.names.NameGenerator;
import java.io.IOException;
import java.util.ArrayList;
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

                initializeBuildings(starting, c, selector);
                initializePopulation(c, starting, selector);
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

                initializeGovernment(c, gen);

            }
            c.government.officials.get(c.government.headofState).setPosition(c.getCapitalCity());
        }

        updater.calculateControl();
        updater.calculateVision();
    }

    private void initializeBuildings(Planet starting, Civilization c, Random selector) {
        //Add resource miners
        createResourceMiners(starting, c, c.getFoundingSpecies());

        //createResourceStorages(c, starting, selector);
        createPopulationStorages(starting, c, selector);

        createFarms(starting, c, selector);

        createIndustrialZones(c, selector, starting);

        //createObservatory(starting, c, selector);
        //Add infrastructure
        createInfrastructure(starting, selector);

        addResearchInstitutions(starting, c, selector);
    }

    private void createResourceMiners(Planet p, Civilization c, Race founding) {
        NameGenerator townGen = null;

        try {
            townGen = NameGenerator.getNameGenerator("town.names");
        } catch (IOException ex) {
            //Ignore, assume all ok
        }

        //Find if vein exists on the planet
        int minerCount = (int) (Math.random() * p.getPlanetSize());
        minerCount += 45;

        for (int i = 0; i < minerCount; i++) {
            //Select random vein
            int id = (int) (p.strata.size() * Math.random());
            Stratum strata = p.strata.get(id);
            District miner = new District();

            //Set the type of resource to mine
            ArrayList<Good> a = new ArrayList<>(strata.minerals.keySet());
            Good g = a.get((int) (a.size() * Math.random()));

            MineArea mineArea = new MineArea(strata, g, 10);
            miner.addArea(p, mineArea);

            miner.setOwner(c);

            double randR = (strata.getRadius() * Math.sqrt(Math.random()));
            double theta = (Math.random() * 2 * Math.PI);

            int x = (int) (Math.cos(theta) * randR) + strata.getX();
            int y = (int) (Math.sin(theta) * randR) + strata.getY();

            if (x < 0) {
                x = 0;
            } else if (x >= p.getPlanetWidth()) {
                x = (p.getPlanetWidth() - 1);
            }

            if (y < 0) {
                y = 0;
            } else if (y >= p.getPlanetHeight()) {
                y = (p.getPlanetHeight() - 1);
            }

            GeographicPoint pt = new GeographicPoint(x, y);

            City city = p.addBuildingToPlanet(pt, miner);

            if (city.getName().equals(City.CITY_DEFAULT)) {
                city.setName(townGen.getName(0) + " Mines");
            }
            //mineCity.addDistrict(miner);
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
        //p.cities.add(mineCity);
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
            District faceBook = new District();
            //Add crops
            Crop crop = new Crop(potato);
            crop.setTimeLeft(25);
            crop.setYield(10000);
            faceBook.setOwner(c);
            //Add farm fields...
            for (int k = 0; k < 30; k++) {
                FarmFieldArea field = new FarmFieldArea(potato);
                field.setTime(30);
                field.grow();
                faceBook.addArea(starting, field);
            }            //Add a farm
            GeographicPoint pt = getRandomEmptyPoint(starting, selector);

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
        NameGenerator townGen = null;

        try {
            townGen = NameGenerator.getNameGenerator("town.names");
        } catch (IOException ex) {
            //Ignore, assume all ok
        }
        for (int i = 0; i < 10; i++) {
            District district = new District();
            //Add areas
            for (ProductionProcess proc : c.productionProcesses) {
                //Add new factory
                ManufacturerArea factory = new ManufacturerArea(proc, 1);
                district.areas.add(factory);
            }

            GeographicPoint pt = getRandomEmptyPoint(starting, selector);

            City city = starting.addBuildingToPlanet(pt, district);
            if (city.getName().equals(City.CITY_DEFAULT)) {
                city.setName(townGen.getName(0) + " Industial Complex");
            }
        }
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

        for (int count = 0; count < popStorMas; count++) {
            District district = new District();
            String townName = townGen.getName(0);
            if (count == 0) {
                //Admin center
                district.addArea(starting, new CapitolArea());
            }

            //district.setMaxStorage(selector.nextInt(30) + 1);
            district.setOwner(c);
            //Distribute
            //Add random positions
            //Add residential areas.
            for (int k = 0; k < 5; k++) {
                ResidentialArea residentialArea = new ResidentialArea();
                district.addArea(starting, residentialArea);
            }

            GeographicPoint pt = getRandomEmptyPoint(starting, selector);

            starting.addBuildingToPlanet(pt, district);

            //Expand sector
            //Choose a direction, and expand...
            District district2 = new District();

            //district2.setMaxStorage(selector.nextInt(popCount2 + 5) + 1);
            district2.setOwner(c);

            //Add residential areas.
            for (int k = 0; k < 5; k++) {
                ResidentialArea residentialArea = new ResidentialArea();
                district2.addArea(starting, residentialArea);
            }

            //test2.setCity(city);
            int dir = selector.nextInt(4) + 1;
            GeographicPoint pt2 = pt.getNorth();
            switch (dir) {
                case 0:
                    pt2 = pt.getNorth();
                    break;
                case 1:
                    pt2 = pt.getSouth();
                    break;
                case 2:
                    pt2 = pt.getEast();
                    break;
                case 3:
                    pt2 = pt.getWest();
            }
            City city = starting.addBuildingToPlanet(pt2, district2);

            if (count == 0) {
                c.setCapitalCity(city);
            }
            if (city.getName().equals(City.CITY_DEFAULT)) {
                city.setName(townName);
            }

            //Add leader to city
            Administrator gov = new Administrator(gen.getName(Math.round(selector.nextFloat())), 42);
            city.setGovernor(gov);

            c.people.add(gov);
            PeopleProcessor.placePerson(city, gov);
            //Set growth
            //Add city
            //starting.cities.add(city);
        }
    }

    private void createCityDistrict(Planet starting, Civilization c, ConquerSpace.game.universe.Point pt, City city) {
        //Add
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
            int y = (selector.nextInt(planet.getPlanetHeight() - 2) + 1);
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
            for (int h = 0; h < s.bodies.size(); h++) {
                Body b = s.bodies.get(h);
                //Add planets
                c.vision.put(new UniversePath(i, b.getID()), VisionTypes.UNDISCOVERED);
            }
        }
    }

    private void createInfrastructure(Planet p, Random selector) {
        //Adds the infrastructure to the planet...        
        for (City c : p.cities) {
            int citySize = c.buildings.size();
            if (citySize > 0) {
                District b = c.buildings.get(selector.nextInt(citySize));
                PowerPlantArea powerPlant = new PowerPlantArea();
                //TODO: choose energy resource
                /*
                Resource resource = null;
                for (Resource res : GameController.resources) {
                    for (String tag : res.getTags()) {
                        if (tag.equals("energy")) {
                            resource = res;
                            break;
                        }
                    }
                }*/
                b.addArea(p, powerPlant);
            }
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

    private void initializePopulation(Civilization civ, Planet p, Random selector) {
        for (City c : p.cities) {
            //Add first population segment
            PopulationSegment seg = new PopulationSegment(civ.getFoundingSpecies(), new Culture());
            seg.size = selector.nextInt(20_000_000) + 150_000;
            seg.populationIncrease = civ.getFoundingSpecies().getBreedingRate();
            c.population.addSegment(seg);
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
            research.focusFields.put(toAdd.getName(), 1);

            c.scienceLabs.add(research);

            //Choose random fields
            if (p.cities.get(i).buildings.size() > 0) {
                p.cities.get(i).buildings.get(0).areas.add(research);
            }
        }
    }

    private void addSupplyLines(Planet p) {
        //Consolidate all resource miners
    }
}
