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

import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.civilization.government.Government;
import ConquerSpace.game.civilization.government.GovernmentPosition;
import ConquerSpace.game.civilization.government.HeritableGovernmentPosition;
import ConquerSpace.game.civilization.government.PoliticalPowerSource;
import ConquerSpace.game.civilization.government.PoliticalPowerTransitionMethod;
import ConquerSpace.game.civilization.vision.VisionTypes;
import ConquerSpace.game.city.City;
import ConquerSpace.game.city.area.CapitolArea;
import ConquerSpace.game.city.area.CommercialArea;
import ConquerSpace.game.city.area.FarmFieldArea;
import ConquerSpace.game.city.area.ManufacturerArea;
import ConquerSpace.game.city.area.MineArea;
import ConquerSpace.game.city.area.PowerPlantArea;
import ConquerSpace.game.city.area.ResearchArea;
import ConquerSpace.game.city.area.ResidentialArea;
import ConquerSpace.game.life.LifeTrait;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.life.Species;
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.population.Culture;
import ConquerSpace.game.population.PopulationSegment;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.science.Field;
import ConquerSpace.game.science.Fields;
import ConquerSpace.game.science.tech.Technologies;
import ConquerSpace.game.science.tech.Technology;
import ConquerSpace.game.ships.hull.HullMaterial;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.bodies.Body;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.game.resources.Good;
import ConquerSpace.game.resources.ProductionProcess;
import ConquerSpace.game.resources.Stratum;
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
        createResourceMiners(starting, c, c.getFoundingSpecies(), selector);

        createPopulationStorages(starting, c, selector);

        createFarms(starting, c, selector);

        createIndustrialZones(c, selector, starting);
        
        //Initialize namelists
        NameGenerator researchInstitutionGenerator = null;
        try {
            researchInstitutionGenerator = NameGenerator.getNameGenerator("uni.names");
        } catch (IOException ex) {
            //Ignore
        }
        
        for (int i = 0; i < starting.cities.size(); i++) {
            City city = starting.cities.get(i);
            addInfrastructure(city);
            addResearchInstitution(city, c, researchInstitutionGenerator, selector);
            addCommercialArea(city);
        }
    }

    private void createResourceMiners(Planet p, Civilization c, Race founding, Random selector) {
        NameGenerator townGen = null;

        try {
            townGen = NameGenerator.getNameGenerator("town.names");
        } catch (IOException ex) {
            //Ignore, assume all ok
        }

        //Find if vein exists on the planet
        int minerCount = (int) (Math.random() * p.getPlanetSize());
        minerCount += 45;
        for (int k = 0; k < p.strata.size(); k++) {
            Stratum stratum = p.strata.get(k);
            for (int i = 0; i < 3; i++) {
                City miner = new City(p.getUniversePath());
                for (Integer g : stratum.minerals.keySet()) {
                    MineArea mineArea = new MineArea(stratum, g, (float) (10 * (selector.nextDouble() + 0.1)));
                    mineArea.setOperatingJobs(50_000);
                    mineArea.setMaxJobs(100_000);
                    miner.addArea(mineArea);
                }

                double randR = (stratum.getRadius() * Math.sqrt(Math.random()));
                double theta = (Math.random() * 2 * Math.PI);

                int x = (int) (Math.cos(theta) * randR) + stratum.getX();
                int y = (int) (Math.sin(theta) * randR) + stratum.getY();

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

                p.addCityDefinition(pt, miner);

                miner.setName(townGen.getName(0) + " Mines");
            }
        }
    }

    private void createFarms(Planet starting, Civilization c, Random selector) {
        //Based on population
        //Add livestock
        //Create a test crop so that you can grow stuff
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

        for (int i = 0; i < 10; i++) {
            City faceBook = new City(starting.getUniversePath());

            //Add farm fields...
            for (int k = 0; k < 30; k++) {
                FarmFieldArea field = new FarmFieldArea(potato);
                field.setTime(30);
                field.grow();
                field.setOperatingJobs(10000);
                field.setMaxJobs(30000);

                faceBook.addArea(field);
            }
            //Add a farm
            GeographicPoint pt = getRandomEmptyPoint(starting, selector);

            starting.cityDistributions.put(pt, faceBook);
        }

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
        //Connect it to many buildings
    }

    private void createIndustrialZones(Civilization c, Random selector, Planet starting) {
        NameGenerator townGen = null;

        try {
            townGen = NameGenerator.getNameGenerator("town.names");
        } catch (IOException ex) {
            //Ignore, assume all ok
        }
        for (int i = 0; i < 10; i++) {
            City industrialCity = new City(starting.getUniversePath());
            //Add areas
            for (ProductionProcess proc : c.productionProcesses) {
                //Add new factory
                ManufacturerArea factory = new ManufacturerArea(proc, 1);

                factory.setMaxJobs(proc.diff * 10000);
                factory.setOperatingJobs(proc.diff * 5000);
                industrialCity.areas.add(factory);
            }

            GeographicPoint pt = getRandomEmptyPoint(starting, selector);

            starting.addCityDefinition(pt, industrialCity);
            industrialCity.setName(townGen.getName(0) + " Industial Complex");
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
            City city = new City(starting.getUniversePath());
            if (count == 0) {
                //Admin center
                city.addArea(new CapitolArea());
            }

            //district.setMaxStorage(selector.nextInt(30) + 1);
            //district.setOwner(c);
            //Distribute
            //Add random positions
            //Add residential areas.
            for (int k = 0; k < 5; k++) {
                ResidentialArea residentialArea = new ResidentialArea();
                city.addArea(residentialArea);
            }

            GeographicPoint pt = getRandomEmptyPoint(starting, selector);

            starting.addCityDefinition(pt, city);

            //Expand sector
            //Choose a direction, and expand...
            //district2.setMaxStorage(selector.nextInt(popCount2 + 5) + 1);
            //Add residential areas.
            for (int k = 0; k < 5; k++) {
                ResidentialArea residentialArea = new ResidentialArea();
                city.addArea(residentialArea);
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
            starting.addCityDefinition(pt2, city);

            if (count == 0) {
                c.setCapitalCity(city);
            }
            String townName = townGen.getName(0);
            city.setName(townName);

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
        } while (planet.cityDistributions.containsKey(pt));
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
            seg.size = selector.nextInt(200_000) + 300_000;

            seg.size *= c.areas.size();
            seg.populationIncrease = civ.getFoundingSpecies().getBreedingRate();
            c.population.addSegment(seg);
        }
    }

    private void addResearchInstitution(City city, Civilization civilization, NameGenerator gen, Random selector) {
        //Add to city
        String name = gen.getName(0);
        ResearchArea research = new ResearchArea();
        research.setName(name);

        research.setMaxJobs(30_000);
        research.setOperatingJobs(15_000);

        //Add fields
        ArrayList<Field> fields = new ArrayList<>();
        //Remove the first one
        Field toFind = civilization.fields.getNode(0); //Because everyone does science for now, not magic when we add it...
        toFind.getFieldsExclusivse(fields);

        //Choose random field
        Field toAdd = fields.get(selector.nextInt(fields.size()));
        research.focusFields.put(toAdd.getName(), 1);

        civilization.scienceLabs.add(research);

        //Choose random fields
        city.addArea(research);
    }
    
    private void addCommercialArea(City c) {
        CommercialArea area = new CommercialArea();
        area.setMaxJobs(100_000);
        area.setOperatingJobs(1_000);
        area.setTradeValue(50_000);
        c.addArea(area);
    }

    private void addInfrastructure(City c) {
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
        c.addArea(powerPlant);
    }

    private void addSupplyLines(Planet p) {
        //Consolidate all resource miners
    }
}
