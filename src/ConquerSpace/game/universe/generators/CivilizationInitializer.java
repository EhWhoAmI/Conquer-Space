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
import ConquerSpace.game.PeopleProcessor;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.organizations.civilization.government.Government;
import ConquerSpace.game.organizations.civilization.government.GovernmentPosition;
import ConquerSpace.game.organizations.civilization.government.HeritableGovernmentPosition;
import ConquerSpace.game.organizations.civilization.government.PoliticalPowerSource;
import ConquerSpace.game.organizations.civilization.government.PoliticalPowerTransitionMethod;
import ConquerSpace.game.organizations.civilization.vision.VisionTypes;
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
import ConquerSpace.game.resources.ProductionProcess;
import ConquerSpace.game.resources.Stratum;
import ConquerSpace.game.ships.satellites.templates.SatelliteTemplate;
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
public class CivilizationInitializer {

    private static final Logger LOGGER = CQSPLogger.getLogger(CivilizationInitializer.class.getName());

    private Universe universe;

    final int CIV_STARTING_TECH_PTS = 10;

    public CivilizationInitializer(Universe u) {
        universe = u;
    }

    public void initGame() {
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
        universe.spaceShips.trimToSize();
        universe.starSystems.trimToSize();

        for (int i = 0; i < universe.getCivilizationCount(); i++) {
            Civilization c = universe.getCivilization(i);
            //Add templates
            initVision(c, universe);

            //Science
            initializeTech(c, selector);

            //Init templates
            initializeSpaceships(c);

            initalizeCivValues(c);

            HullMaterial material = new HullMaterial("Testing Hull Material", 100, 5, 12);
            material.setId(0);
            c.hullMaterials.add(material);

            UniversePath p = c.getStartingPlanet();

            if (universe.getSpaceObject(p) instanceof Planet) {
                Planet starting = (Planet) universe.getSpaceObject(p);
                c.setCapitalPlanet(starting);

                initializeCities(starting, c, selector);

                nameStratumOnPlanet(starting, selector);

                //Set ownership
                starting.setOwnerID(c.getId());
                starting.scanned.add(c.getId());
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
                createUnrecruitedPeople(c, starting, gen, selector);

                initializeGovernment(c, gen, selector);

                //Set head of state position
                c.government.officials.get(c.government.headofState).setPosition(c.getCapitalCity());
            }
        }
    }

    private void initializeCities(Planet starting, Civilization c, Random selector) {
        NameGenerator townNames = null;
        NameGenerator personNames = null;

        try {
            townNames = NameGenerator.getNameGenerator("town.names");
            personNames = NameGenerator.getNameGenerator("us.names");
        } catch (IOException ex) {
            //Ignore
        }

        createResourceMiners(starting, c, c.getFoundingSpecies(), selector, townNames);
        createFarms(starting, c, selector, townNames);
        createCities(starting, c, selector, townNames);

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
            addCommercialArea(city, c);
            addPopulation(city, selector, c);
            addGovenor(city, c, selector, personNames);
        }
    }

    private void addGovenor(City c, Civilization civ, Random selector, NameGenerator personGenerator) {
        String name = personGenerator.getName(selector.nextInt(personGenerator.getRulesCount()), selector);
        Administrator admin = new Administrator(name, selector.nextInt(20) + 40);
        admin.setRole("Governing " + c.getName());
        admin.setPosition(c);
        c.setGovernor(admin);
        c.peopleAtCity.add(admin);
        civ.people.add(admin);
    }

    private void addPopulation(City c, Random selector, Civilization civ) {
        PopulationSegment seg = new PopulationSegment(civ.getFoundingSpecies().getId(), new Culture());
        seg.size = selector.nextInt(200_000) + 300_000;
        seg.size *= c.areas.size();

        seg.populationIncrease = civ.getFoundingSpecies().getBreedingRate();
        c.population.addSegment(seg);

        ResidentialArea residentialArea = new ResidentialArea();
        residentialArea.setOwner(c.getId());
        c.addArea(residentialArea);
    }

    //Cities whose primary industry relies on manufacturing, not mining or farming
    private void createCities(Planet starting, Civilization c, Random selector, NameGenerator townGen) {
        for (int i = 0; i < 10; i++) {
            City city = new City(starting.getUniversePath());
            //Add areas
            if (i == 0) {
                c.setCapitalCity(city);
                CapitolArea area = new CapitolArea();
                area.setOwner(c.getId());
                city.addArea(area);
            }

            for (int k = 0; k < c.productionProcesses.size(); k++) {
                //Add random thing
                ProductionProcess proc = c.productionProcesses.get(k);
                //Add new factory
                ManufacturerArea factory = new ManufacturerArea(proc, 1);
                factory.setOwner(c.getId());

                factory.setMaxJobs(proc.diff * 10000);
                factory.setOperatingJobs(proc.diff * 5000);
                factory.setWorkingmultiplier(1.2f);
                city.areas.add(factory);
            }

            GeographicPoint pt = getRandomEmptyPoint(starting, selector);
            city.setName(townGen.getName(0, selector));

            starting.addCityDefinition(pt, city);
        }
    }

    private void createResourceMiners(Planet p, Civilization c, Race founding, Random selector, NameGenerator townGen) {
        //Find if vein exists on the planet
        for (int k = 0; k < p.strata.size(); k++) {
            Stratum stratum = p.strata.get(k);
            for (int i = 0; i < 3; i++) {
                City miner = new City(p.getUniversePath());
                for (Integer g : stratum.minerals.keySet()) {
                    MineArea mineArea = new MineArea(stratum, g, (float) (10 * (selector.nextDouble() + 0.1)));
                    mineArea.setWorkingmultiplier(1.5f);
                    mineArea.setOperatingJobs(50_000);
                    mineArea.setMaxJobs(100_000);
                    mineArea.setOwner(c.getId());
                    miner.addArea(mineArea);
                }

                //Add random production process
                ProductionProcess proc = c.productionProcesses.get(selector.nextInt(c.productionProcesses.size()));
                //Add new factory
                ManufacturerArea factory = new ManufacturerArea(proc, 1);

                factory.setMaxJobs(proc.diff * 10000);
                factory.setOperatingJobs(proc.diff * 5000);
                factory.setWorkingmultiplier(1.2f);
                miner.areas.add(factory);

                double randR = (stratum.getRadius() * Math.sqrt(selector.nextDouble()));
                double theta = (selector.nextDouble() * 2 * Math.PI);

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

                miner.setName(townGen.getName(0, selector) + " Mines");
            }
        }
    }

    private void createFarms(Planet starting, Civilization c, Random selector, NameGenerator gen) {
        //Based on population
        //Add livestock
        //Create a test crop so that you can grow stuff
        Species potato = new Species("Potato");
        potato.lifeTraits.add(LifeTrait.Rooted);
        potato.lifeTraits.add(LifeTrait.Delicious);
        potato.lifeTraits.add(LifeTrait.Photosynthetic);

        //Set food good
        c.getFoundingSpecies().food = potato.getFoodGood();

        //Add the biomass
        //Set the amount on planet...
        LocalLife localLife = new LocalLife();
        localLife.setSpecies(potato);
        localLife.setBiomass(100_000);
        starting.localLife.add(localLife);

        for (int i = 0; i < 10; i++) {
            City faceBook = new City(starting.getUniversePath());

            //Add farm fields...
            for (int k = 0; k < 15; k++) {
                FarmFieldArea field = new FarmFieldArea(potato);
                field.setGrown(potato);
                //30 days
                field.setTime(30 * 24);
                field.grow();
                field.setFieldSize(5000);
                field.setOperatingJobs(10000);
                field.setMaxJobs(30000);
                field.setOwner(c.getId());

                faceBook.addArea(field);
            }

            faceBook.setName(gen.getName(0, selector));
            //Add a farm
            GeographicPoint pt = getRandomEmptyPoint(starting, selector);

            starting.addCityDefinition(pt, faceBook);
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
    }

    private void createUnrecruitedPeople(Civilization c, Planet homePlanet, NameGenerator gen, Random selector) {
        c.unrecruitedPeople.clear();
        int peopleCount = selector.nextInt(5) + 5;

        for (int peep = 0; peep < peopleCount; peep++) {
            int age = selector.nextInt(40) + 20;
            String person = "name";
            person = gen.getName(selector.nextInt(gen.getRulesCount()), selector);
            Scientist nerd = new Scientist(person, age);
            nerd.setSkill(selector.nextInt(4) + 1);
            nerd.traits.add(GameController.personalityTraits.get((selector.nextInt(GameController.personalityTraits.size()))));

            //Set location
            nerd.setPosition(c.getCapitalCity());

            c.unrecruitedPeople.add(nerd);
        }

        //Admins
        peopleCount = selector.nextInt(5) + 5;

        for (int peep = 0; peep < peopleCount; peep++) {
            int age = selector.nextInt(40) + 20;
            String person = "name";
            person = gen.getName(selector.nextInt(gen.getRulesCount()), selector);
            Administrator dude = new Administrator(person, age);
            //nerd.setSkill((int) (Math.random() * 5) + 1);
            dude.traits.add(GameController.personalityTraits.get((selector.nextInt(GameController.personalityTraits.size()))));
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
            name = gen.getName(selector.nextInt(gen.getRulesCount()), selector);
        }

        Scientist r = new Scientist(name, 20);
        r.setSkill(5);
        //Add random trait 
        r.traits.add(GameController.personalityTraits.get((selector.nextInt(GameController.personalityTraits.size()))));
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

    //Test, will change in the future with a more robust system
    private void initializeGovernment(Civilization c, NameGenerator gen, Random selector) {
        //Create person
        int age = selector.nextInt(40) + 20;
        String person = "name";
        person = gen.getName(selector.nextInt(gen.getRulesCount()), selector);
        Administrator dude = new Administrator(person, 400);
        //nerd.setSkill((int) (Math.random() * 5) + 1);
        dude.traits.add(GameController.personalityTraits.get((selector.nextInt(GameController.personalityTraits.size()))));
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

        person = gen.getName(selector.nextInt(gen.getRulesCount()), selector);

        Administrator crownPrince = new Administrator(person, 0);
        //nerd.setSkill((int) (Math.random() * 5) + 1);
        crownPrince.traits.add(GameController.personalityTraits.get((selector.nextInt(GameController.personalityTraits.size()))));
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
                c.vision.put(new UniversePath(i, b.getId()), VisionTypes.UNDISCOVERED);
            }
        }
    }

    private void nameStratumOnPlanet(Planet p, Random selector) {
        NameGenerator gen = null;
        try {
            gen = NameGenerator.getNameGenerator("strata.names");
        } catch (IOException ex) {
            //Ignore
        }
        for (Stratum strata : p.strata) {
            strata.setName(gen.getName(0, selector));
        }
    }

    private void addResearchInstitution(City city, Civilization civilization, NameGenerator gen, Random selector) {
        //Add to city
        String name = gen.getName(0, selector);
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
        research.setOwner(civilization.getId());

        //Choose random fields
        city.addArea(research);
    }

    private void addCommercialArea(City c, Civilization civ) {
        CommercialArea area = new CommercialArea();
        area.setMaxJobs(100_000);
        area.setOperatingJobs(1_000);
        area.setTradeValue(50_000);
        area.setOwner(civ.getId());
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
        powerPlant.setOwner(c.getId());
        c.addArea(powerPlant);
    }

    private void addSupplyLines(Planet p) {
        //Consolidate all resource miners
    }

    private void initializeSpaceships(Civilization c) {
        SatelliteTemplate template = new SatelliteTemplate();
        template.setMass(84);
        template.setName("Sputnik");
        c.satelliteTemplates.add(template);
    }
}
