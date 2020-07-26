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

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.characters.Administrator;
import ConquerSpace.common.game.characters.PersonalityTrait;
import ConquerSpace.common.game.characters.Scientist;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.CapitolArea;
import ConquerSpace.common.game.city.area.CommercialArea;
import ConquerSpace.common.game.city.area.FarmFieldArea;
import ConquerSpace.common.game.city.area.ManufacturerArea;
import ConquerSpace.common.game.city.area.MineArea;
import ConquerSpace.common.game.city.area.PowerPlantArea;
import ConquerSpace.common.game.city.area.ResearchArea;
import ConquerSpace.common.game.city.area.ResidentialArea;
import ConquerSpace.common.game.life.LifeTrait;
import ConquerSpace.common.game.life.LocalLife;
import ConquerSpace.common.game.life.Species;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.game.organizations.behavior.EmptyBehavior;
import ConquerSpace.common.game.organizations.behavior.PlayerBehavior;
import ConquerSpace.common.game.organizations.behavior.ResourceManagerBehavior;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.organizations.civilization.government.Government;
import ConquerSpace.common.game.organizations.civilization.government.GovernmentPosition;
import ConquerSpace.common.game.organizations.civilization.government.HeritableGovernmentPosition;
import ConquerSpace.common.game.organizations.civilization.government.PoliticalPowerSource;
import ConquerSpace.common.game.organizations.civilization.government.PoliticalPowerTransitionMethod;
import ConquerSpace.common.game.organizations.civilization.vision.VisionTypes;
import ConquerSpace.common.game.population.Culture;
import ConquerSpace.common.game.population.Population;
import ConquerSpace.common.game.population.PopulationSegment;
import ConquerSpace.common.game.population.Race;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.Stratum;
import ConquerSpace.common.game.science.Field;
import ConquerSpace.common.game.science.Fields;
import ConquerSpace.common.game.science.Technologies;
import ConquerSpace.common.game.science.Technology;
import ConquerSpace.common.game.ships.hull.HullMaterial;
import ConquerSpace.common.game.ships.satellites.templates.SatelliteTemplate;
import ConquerSpace.common.game.universe.GeographicPoint;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.bodies.Body;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.StarSystem;
import ConquerSpace.common.util.logging.CQSPLogger;
import ConquerSpace.common.util.names.NameGenerator;
import ConquerSpace.server.PeopleProcessor;
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

    private Galaxy universe;

    final int CIV_STARTING_TECH_PTS = 10;

    private GameState gameState;

    public CivilizationInitializer(GameState state) {
        universe = state.getUniverse();
        this.gameState = state;
    }

    public void initGame() {
        //All the home planets of the civs are theirs.
        //Set home planet and sector
        Random selector = new Random();
        NameGenerator gen = null;
        try {
            gen = NameGenerator.getNameGenerator("us.names");
        } catch (IOException ex) {
            //Ignore
        }
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);

            //Set behavior
            if (civilization.getId() == gameState.playerCiv) {
                civilization.setBehavior(new PlayerBehavior(gameState, civilization));
            } else {
                civilization.setBehavior(new EmptyBehavior(gameState, civilization));
            }
            //Add templates
            initVision(civilization, universe);

            //Science
            initializeTech(civilization, selector);

            //Init templates
            initializeSpaceships(civilization);

            initalizeCivValues(civilization);

            HullMaterial material = new HullMaterial(gameState, "Testing Hull Material", 100, 5, 12);
            civilization.hullMaterials.add(material.getId());

            UniversePath path = civilization.getStartingPlanet();

            Integer id = universe.getSpaceObject(path);
            Planet startingPlanet = gameState.getObject(id, Planet.class);

            civilization.setCapitalPlanet(startingPlanet);

            initializeCities(startingPlanet, civilization, selector);

            nameStratumOnPlanet(startingPlanet, selector);

            //Set ownership
            startingPlanet.setOwnerID(civilization.getId());
            startingPlanet.scan(civilization.getId());
            startingPlanet.setHabitated(true);
            startingPlanet.setName(civilization.getHomePlanetName());

            civilization.habitatedPlanets.add(startingPlanet.getId());

            //Add resources
//                for (Good res : GameController.ores) {
//                    c.resourceList.put(res, 0);
//                }
            LOGGER.info("Civ " + civilization.getName() + " Starting planet: " + startingPlanet.getUniversePath());

            //Deal with people
            initalizeRecruitedPeople(civilization, gen, selector);

            //Add unrecruited people
            createUnrecruitedPeople(civilization, startingPlanet, gen, selector);

            initializeGovernment(civilization, gen, selector);

            //init orgs
            initializeOrgs(civilization, startingPlanet);

            //Set head of state position
            civilization.government.officials.get(civilization.government.headofState).setPosition(civilization.getCapitalCity());

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
        Species food = createEdibleFood(c);
        
        createResourceMiners(starting, c, c.getFoundingSpecies(), selector, townNames);
        createFarms(starting, food, c, selector, townNames);
        createCities(starting, c, selector, townNames);

        //Initialize namelists
        NameGenerator researchInstitutionGenerator = null;
        try {
            researchInstitutionGenerator = NameGenerator.getNameGenerator("uni.names");
        } catch (IOException ex) {
            //Ignore
        }

        for (int i = 0; i < starting.cities.size(); i++) {
            City city = gameState.getObject(starting.cities.get(i), City.class);
            addInfrastructure(city);
            addResearchInstitution(city, c, researchInstitutionGenerator, selector);
            addCommercialArea(city, c);
            addPopulation(city, selector, c);
            addGovenor(city, c, selector, personNames);
        }
    }

    private void addGovenor(City c, Civilization civ, Random selector, NameGenerator personGenerator) {
        String name = personGenerator.getName(selector.nextInt(personGenerator.getRulesCount()), selector);
        Administrator admin = new Administrator(gameState, name, selector.nextInt(20) + 40);
        admin.setRole("Governing " + c.getName());
        admin.setPosition(c);
        c.setGovernor(admin);
        c.peopleAtCity.add(admin);
        civ.people.add(admin.getId());
    }

    private void addPopulation(City c, Random selector, Civilization civ) {
        Culture culture = new Culture(gameState);
        PopulationSegment seg = new PopulationSegment(gameState, civ.getFoundingSpecies().getId(), culture.getId());
        seg.size = selector.nextInt(200_000) + 300_000;
        seg.size *= c.areas.size();

        seg.populationIncrease = civ.getFoundingSpecies().getBreedingRate();
        gameState.getObject(c.population, Population.class).addSegment(seg.getId());

        ResidentialArea residentialArea = new ResidentialArea(gameState);
        residentialArea.setOwner(c.getId());
        c.addArea(residentialArea.getId());
    }

    //Cities whose primary industry relies on manufacturing, not mining or farming
    private void createCities(Planet starting, Civilization c, Random selector, NameGenerator townGen) {
        for (int i = 0; i < 10; i++) {
            City city = new City(gameState, starting.getId());
            //Add areas
            if (i == 0) {
                c.setCapitalCity(city);
                CapitolArea area = new CapitolArea(gameState);
                area.setOwner(c.getId());
                city.addArea(area.getId());
            }

            for (int k = 0; k < c.productionProcesses.size(); k++) {
                //Add random thing
                ProductionProcess proc = c.productionProcesses.get(k);
                //Add new factory
                ManufacturerArea factory = new ManufacturerArea(gameState, proc, 1);
                factory.setOwner(c.getId());

                factory.setMaxJobs(proc.diff * 10000);
                factory.setOperatingJobs(proc.diff * 5000);
                factory.setWorkingmultiplier(1.2f);
                city.areas.add(factory.getId());
            }

            GeographicPoint pt = getRandomEmptyPoint(starting, selector);
            city.setName(townGen.getName(0, selector));

            starting.addCityDefinition(pt, city);
        }
    }

    private void createResourceMiners(Planet p, Civilization c, Race founding, Random selector, NameGenerator townGen) {
        //Find if vein exists on the planet
        for (int k = 0; k < p.strata.size(); k++) {
            Stratum stratum = gameState.getObject(p.strata.get(k), Stratum.class);
            for (int i = 0; i < 3; i++) {
                City miner = new City(gameState, p.getId());
                for (Integer g : stratum.minerals.keySet()) {
                    MineArea mineArea = new MineArea(gameState, stratum.getId(), g, (float) (10 * (selector.nextDouble() + 0.1)));
                    mineArea.setWorkingmultiplier(1.5f);
                    mineArea.setOperatingJobs(50_000);
                    mineArea.setMaxJobs(100_000);
                    mineArea.setOwner(c.getId());
                    miner.addArea(mineArea.getId());
                }

                //Add random production process
                ProductionProcess proc = c.productionProcesses.get(selector.nextInt(c.productionProcesses.size()));
                //Add new factory
                ManufacturerArea factory = new ManufacturerArea(gameState, proc, 1);

                factory.setMaxJobs(proc.diff * 10000);
                factory.setOperatingJobs(proc.diff * 5000);
                factory.setWorkingmultiplier(1.2f);
                miner.areas.add(factory.getId());

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
    
    private Species createEdibleFood(Civilization civ) {
        Species potato = new Species(gameState, "Potato");
        potato.lifeTraits.add(LifeTrait.Rooted);
        potato.lifeTraits.add(LifeTrait.Delicious);
        potato.lifeTraits.add(LifeTrait.Photosynthetic);
        gameState.addSpecies(potato);
        
        int consumableResources = civ.getFoundingSpecies().getConsumableResource();
        ProductionProcess foodProcess = new ProductionProcess(gameState);

        foodProcess.input.put(potato.getFoodGood(), 10d);
        foodProcess.output.put(consumableResources, 10d);
        foodProcess.setName(potato.getName() + " to food");
        
        civ.productionProcesses.add(foodProcess);
        return potato;
    }

    private void createFarms(Planet starting, Species crop, Civilization c, Random selector, NameGenerator gen) {
        //Based on population
        //Add livestock
        //Create a test crop so that you can grow stuff
        
        //Set processable to foodgood
        
        //Set food good
        //c.getFoundingSpecies().food = potato.getFoodGood();

        //Add the biomass
        //Set the amount on planet...
        LocalLife localLife = new LocalLife();
        localLife.setSpecies(crop);
        localLife.setBiomass(100_000);
        starting.localLife.add(localLife);

        for (int i = 0; i < 10; i++) {
            City faceBook = new City(gameState, starting.getId());

            //Add farm fields...
            for (int k = 0; k < 15; k++) {
                FarmFieldArea field = new FarmFieldArea(gameState, crop);
                field.setGrown(crop);
                //30 days
                field.setTime(30 * 24);
                field.grow();
                field.setFieldSize(5000);
                field.setOperatingJobs(10000);
                field.setMaxJobs(30000);
                field.setOwner(c.getId());

                faceBook.addArea(field.getId());
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
            Scientist nerd = new Scientist(gameState, person, age);
            nerd.setSkill(selector.nextInt(4) + 1);
            nerd.traits.add(getRandomPersonalityTrait(selector));

            //Set location
            nerd.setPosition(c.getCapitalCity());

            c.unrecruitedPeople.add(nerd.getId());
        }

        //Admins
        peopleCount = selector.nextInt(5) + 5;

        for (int peep = 0; peep < peopleCount; peep++) {
            int age = selector.nextInt(40) + 20;
            String person = "name";
            person = gen.getName(selector.nextInt(gen.getRulesCount()), selector);
            Administrator dude = new Administrator(gameState, person, age);
            //nerd.setSkill((int) (Math.random() * 5) + 1);
            dude.traits.add(getRandomPersonalityTrait(selector));
            dude.setPosition(c.getCapitalCity());

            c.unrecruitedPeople.add(dude.getId());
        }
    }

    private PersonalityTrait getRandomPersonalityTrait(Random selector) {
        return gameState.personalityTraits.get((selector.nextInt(gameState.personalityTraits.size())));
    }

    private void initializeTech(Civilization c, Random selector) {
        //Add fields
        c.fields = (Fields.toField(gameState.fieldNodeRoot));
        //Add all starting techs            
        for (Technology tech : Technologies.getTechsByTag(gameState, "Starting")) {
            c.researchTech(gameState, tech);
        }

        //Select one of the space travel sciences
        Technology[] teks = Technologies.getTechsByTag(gameState, "space travel base");

        //To research this
        c.civTechs.put(teks[selector.nextInt(teks.length)], 100);

        //Propulsion
        teks = Technologies.getTechsByTag(gameState, "Propulsion");
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

        Scientist r = new Scientist(gameState, name, 20);
        r.setSkill(5);
        //Add random trait 
        r.traits.add(getRandomPersonalityTrait(selector));
        PeopleProcessor.placePerson(c.getCapitalCity(), r);

        c.people.add(r.getId());
    }

    //Governmental orgs...
    private void initializeOrgs(Civilization c, Planet planet) {
        Organization org = new Organization(gameState, "Ministry of Economic Planning");
        org.setBehavior(new ResourceManagerBehavior(gameState, org));
        //Sort through city
        for (Integer cityId : planet.cities) {
            City city = gameState.getObject(cityId, City.class);

            org.region.bodies.add(city.getId());
        }

        gameState.addOrganization(org);
        c.addChild(org.getId());
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
        Administrator dude = new Administrator(gameState, person, 400);
        //nerd.setSkill((int) (Math.random() * 5) + 1);
        dude.traits.add(getRandomPersonalityTrait(selector));
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
        dude.governmentPosition = leader;
        c.employ(dude.getId());

        person = gen.getName(selector.nextInt(gen.getRulesCount()), selector);

        Administrator crownPrince = new Administrator(gameState, person, 0);
        //nerd.setSkill((int) (Math.random() * 5) + 1);
        crownPrince.traits.add(getRandomPersonalityTrait(selector));
        crownPrince.setPosition(c.getCapitalCity());
        crownPrince.setRole("Loafing around -- Lazy brat");
        //Add heir to the throne of the GOD EMPEROR

        GovernmentPosition crownPrincePosition = new GovernmentPosition();
        c.government.officials.put(crownPrincePosition, crownPrince);
        crownPrincePosition.setName("Crown Prince");
        leader.nextInLine = crownPrincePosition;
        crownPrince.governmentPosition = crownPrincePosition;

        PeopleProcessor.placePerson(c.getCapitalCity(), crownPrince);
        c.employ(crownPrince.getId());
    }

    private void initVision(Civilization c, Galaxy u) {
        for (int i = 0; i < u.getStarSystemCount(); i++) {
            StarSystem s = u.getStarSystemObject(i);
            c.vision.put(new UniversePath(i), VisionTypes.UNDISCOVERED);
            for (int h = 0; h < s.getBodyCount(); h++) {
                Body b = s.getBodyObject(h);
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
        for (Integer strata : p.strata) {
            Stratum stratum = gameState.getObject(strata, Stratum.class);
            stratum.setName(gen.getName(0, selector));
        }
    }

    private void addResearchInstitution(City city, Civilization civilization, NameGenerator gen, Random selector) {
        //Add to city
        String name = gen.getName(0, selector);
        ResearchArea research = new ResearchArea(gameState);
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

        civilization.scienceLabs.add(research.getId());
        research.setOwner(civilization.getId());

        //Choose random fields
        city.addArea(research.getId());
    }

    private void addCommercialArea(City c, Civilization civ) {
        CommercialArea area = new CommercialArea(gameState);
        area.setMaxJobs(100_000);
        area.setOperatingJobs(1_000);
        area.setTradeValue(50_000);
        area.setOwner(civ.getId());
        c.addArea(area.getId());
    }

    private void addInfrastructure(City c) {
        PowerPlantArea powerPlant = new PowerPlantArea(gameState);
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
        c.addArea(powerPlant.getId());
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
