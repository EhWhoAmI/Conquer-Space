
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
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.characters.Administrator;
import ConquerSpace.common.game.characters.PersonEnterable;
import ConquerSpace.common.game.characters.PersonalityTrait;
import ConquerSpace.common.game.characters.Scientist;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.CapitolAreaFactory;
import ConquerSpace.common.game.city.area.CommercialAreaFactory;
import ConquerSpace.common.game.city.area.CustomComponentFactoryManufacturerAreaFactory;
import ConquerSpace.common.game.city.area.FarmFieldFactory;
import ConquerSpace.common.game.city.area.FinancialAreaFactory;
import ConquerSpace.common.game.city.area.LeisureAreaFactory;
import ConquerSpace.common.game.city.area.ManufacturerAreaFactory;
import ConquerSpace.common.game.city.area.MineAreaFactory;
import ConquerSpace.common.game.city.area.PopulationUpkeepAreaFactory;
import ConquerSpace.common.game.city.area.PortAreaFactory;
import ConquerSpace.common.game.city.area.PowerPlantAreaFactory;
import ConquerSpace.common.game.city.area.ResearchArea;
import ConquerSpace.common.game.city.area.ResearchAreaFactory;
import ConquerSpace.common.game.city.area.ResidentialAreaFactory;
import ConquerSpace.common.game.economy.Market;
import ConquerSpace.common.game.life.LifeTrait;
import ConquerSpace.common.game.life.LocalLife;
import ConquerSpace.common.game.life.Species;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.game.organizations.behavior.EmptyBehavior;
import ConquerSpace.common.game.organizations.behavior.PlayerBehavior;
import ConquerSpace.common.game.organizations.behavior.ResourceManagerBehavior;
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
import ConquerSpace.common.game.resources.Good;
import ConquerSpace.common.game.resources.GoodUtil;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.resources.Stratum;
import ConquerSpace.common.game.science.Field;
import ConquerSpace.common.game.science.Fields;
import ConquerSpace.common.game.science.Technologies;
import ConquerSpace.common.game.science.Technology;
import ConquerSpace.common.game.ships.HullMaterial;
import ConquerSpace.common.game.universe.GeographicPoint;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.StarSystem;
import ConquerSpace.common.game.universe.bodies.StarSystemBody;
import ConquerSpace.common.util.logging.CQSPLogger;
import ConquerSpace.common.util.names.NameGenerator;
import ConquerSpace.server.PeopleProcessor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
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

    public void initCivilizations(Random selector) {
        //All the home planets of the civs are theirs.
        //Set home planet and sector
        NameGenerator gen = null;
        try {
            gen = NameGenerator.getNameGenerator("us.names");
        } catch (IOException ex) {
            //Ignore
        }
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);

            //Set behavior
            if (civilization.getReference().equals(gameState.playerCiv)) {
                civilization.setBehavior(new PlayerBehavior(gameState, civilization));
            } else {
                civilization.setBehavior(new EmptyBehavior(gameState, civilization));
            }

            //Add resources for the civ
            civilization.getTaggedGoods().put("structure", GoodUtil.findGoodByTag(gameState, "structure"));
            civilization.getTaggedGoods().put("energy", GoodUtil.findGoodByTag(gameState, "energy"));

            initVision(civilization, universe);

            //Science
            initializeTech(civilization, selector);

            initalizeCivValues(civilization);

            getIndustryChains(civilization);

            HullMaterial material = new HullMaterial(gameState, "Testing Hull Material", 100, 5, 12);
            civilization.getHullMaterials().add(material.getReference());

            UniversePath path = civilization.getStartingPlanet();

            ObjectReference id = universe.getSpaceObject(path);
            Planet startingPlanet = gameState.getObject(id, Planet.class);

            civilization.setCapitalPlanet(startingPlanet);

            initializeCities(startingPlanet, civilization, selector);

            addSupplyLines(startingPlanet);

            nameStratumOnPlanet(startingPlanet, selector);

            //Set ownership
            startingPlanet.setOwnerReference(civilization.getReference());
            startingPlanet.scan(civilization.getReference());
            startingPlanet.setHabitated(true);
            startingPlanet.setName(civilization.getHomePlanetName());

            civilization.getHabitatedPlanets().add(startingPlanet.getReference());

            //Add resources
//                for (Good res : GameController.ores) {
//                    c.resourceList.put(res, 0);
//                }
            LOGGER.info("Civ " + civilization.getName() + " Starting planet: " + startingPlanet.getUniversePath());

            //Deal with people
            initalizeRecruitedPeople(civilization, gen, selector);

            //Add unrecruited people
            createUnrecruitedPeople(civilization, gen, selector);

            initializeGovernment(civilization, gen, selector);

            //init orgs
            initializeOrgs(civilization, startingPlanet);

            //Set head of state position
            civilization.getGovernment().officials.get(civilization.getGovernment().headofState).setPosition(civilization.getCapitalCity());

            //Proc gen political stuff, progress events behind the scene to generate a little bit of history
        }
    }

    private void initializeCities(Planet startingPlanet, Civilization c, Random selector) {
        NameGenerator townNames = null;
        NameGenerator personNames = null;

        try {
            townNames = NameGenerator.getNameGenerator("town.names");
            personNames = NameGenerator.getNameGenerator("us.names");
        } catch (IOException ex) {
            //Ignore
        }
        Species food = createEdibleFood(c);

        createResourceMiners(startingPlanet, c, c.getFoundingSpecies(), selector, townNames);
        createFarms(startingPlanet, food, c, selector, townNames);
        createNormalCities(startingPlanet, c, selector, townNames);

        //Initialize namelists
        NameGenerator researchInstitutionGenerator = null;
        try {
            researchInstitutionGenerator = NameGenerator.getNameGenerator("uni.names");
        } catch (IOException ex) {
            //Ignore
        }

        //Common market
        Market market = new Market(gameState);
        startingPlanet.setPlanetaryMarket(market.getReference());

        for (int i = 0; i < startingPlanet.getCities().size(); i++) {
            City city = gameState.getObject(startingPlanet.getCities().get(i), City.class);
            city.setOwner(c.getReference());
            addInfrastructure(c, city);
            addResearchInstitution(city, c, researchInstitutionGenerator, selector);
            addCommercialArea(city, c);
            addMoreProcessingToCities(city, selector, c);
            addGovenor(city, c, selector, personNames);

            //Set market for all populations
            for (ObjectReference ref : gameState.getObject(city.getPopulation(), Population.class).segments) {
                market.addTrader(gameState.getObject(ref, PopulationSegment.class));
            }

            //Set preinitialized wealth based on population or something
            city.changeWealth((int) gameState.getObject(city.getPopulation(), Population.class).getPopulationSize() * 10);
        }
    }

    private void addGovenor(City c, Civilization civ, Random selector, NameGenerator personGenerator) {
        String name = personGenerator.getName(selector.nextInt(personGenerator.getRulesCount()), selector);
        Administrator admin = new Administrator(gameState, name, selector.nextInt(20) + 40);
        admin.setRole("Governing " + c.getName());
        admin.setPosition(c);
        c.setGovernor(admin);
        c.getPeopleAtCity().add(admin.getReference());
        civ.getPeople().add(admin.getReference());
    }

    private void addMoreProcessingToCities(City city, Random selector, Civilization civ) {
        //Set culture
        Population population = gameState.getObject(city.getPopulation(), Population.class);

        Culture culture = new Culture(gameState);
        PopulationSegment seg = new PopulationSegment(gameState, civ.getFoundingSpecies().getReference(), culture.getReference());
        seg.size = selector.nextInt(200_000) + 300_000;
        seg.size *= city.getAreas().size();
        seg.workablePopulation = (long) (seg.size * 0.3);
        seg.setWealth(100000);
        seg.tier = 0;

        seg.populationIncrease = civ.getFoundingSpecies().getBreedingRate();
        population.addSegment(seg.getReference());

        PopulationSegment seg2 = new PopulationSegment(gameState, civ.getFoundingSpecies().getReference(), culture.getReference());
        seg2.size = selector.nextInt(200_000) + 300_000;
        seg2.size *= city.getAreas().size();
        seg2.workablePopulation = (long) (seg2.size * 0.3);
        seg2.tier = 1;
        seg2.setWealth(100000);
        population.addSegment(seg2.getReference());

        ResidentialAreaFactory residentialAreaFactory = new ResidentialAreaFactory(civ);
        city.addArea(residentialAreaFactory.build(gameState).getReference());

        //Add transportation stuff
        PortAreaFactory factory = new PortAreaFactory(civ);
        factory.setOperatingJobs(10000);
        factory.setMaxJobs(50000);
        city.addArea(factory.build(gameState).getReference());

        PopulationUpkeepAreaFactory upkeepFactory = new PopulationUpkeepAreaFactory(civ);
        upkeepFactory.setOperatingJobs(50000);
        upkeepFactory.setMaxJobs((int) ((double) population.getWorkableSize() * 0.12));
        city.addArea(upkeepFactory.build(gameState).getReference());

        LeisureAreaFactory leisureAreaFactory = new LeisureAreaFactory(civ);
        leisureAreaFactory.setOperatingJobs(10000);
        leisureAreaFactory.setMaxJobs(200000);
        city.addArea(leisureAreaFactory.build(gameState).getReference());

        FinancialAreaFactory financialAreaFactory = new FinancialAreaFactory(civ);
        financialAreaFactory.setOperatingJobs(10000);
        financialAreaFactory.setMaxJobs(75000);
        city.addArea(financialAreaFactory.build(gameState).getReference());
    }

    //Cities whose primary industry relies on manufacturing, not mining or farming
    private void createNormalCities(Planet starting, Civilization civ, Random selector, NameGenerator townGen) {
        for (int i = 0; i < 10; i++) {
            City city = new City(gameState, starting.getReference());
            //Add areas
            if (i == 0) {
                civ.setCapitalCity(city);
                CapitolAreaFactory area = new CapitolAreaFactory(civ);
                city.addArea(area.build(gameState).getReference());
            }

            for (int k = 0; k < civ.getProductionProcesses().size(); k++) {
                //Add random thing
                ProductionProcess proc = civ.getProductionProcesses().get(k);
                //Add new factory
                ManufacturerAreaFactory factory = new ManufacturerAreaFactory(civ);
                factory.setProcess(proc);
                factory.setProductivity(1);

                factory.setMaxJobs(proc.difficulty * 10000);
                factory.setOperatingJobs(proc.difficulty * 5000);
                factory.setWorkingmultiplier(1.2f);
                city.getAreas().add(factory.build(gameState).getReference());
            }

            //Add a custom constructor for fun
            CustomComponentFactoryManufacturerAreaFactory ccfmaf
                    = new CustomComponentFactoryManufacturerAreaFactory(civ);

            ccfmaf.setMaxJobs(10000);
            ccfmaf.setOperatingJobs(5000);
            ccfmaf.setWorkingmultiplier(1.2f);
            city.getAreas().add(ccfmaf.build(gameState).getReference());

            CommercialAreaFactory area = new CommercialAreaFactory(civ);
            area.setMaxJobs(500_000);
            area.setOperatingJobs(1_000);
            area.setTradeValue(50_000);
            city.addArea(area.build(gameState).getReference());

            GeographicPoint pt = getRandomEmptyPoint(starting, selector);
            city.setName(townGen.getName(0, selector));

            starting.addCityDefinition(pt, city);
        }
    }

    private void createResourceMiners(Planet p, Civilization c, Race founding, Random selector, NameGenerator townGen) {
        //Find if vein exists on the planet
        for (int k = 0; k < p.getStrata().size(); k++) {
            Stratum stratum = gameState.getObject(p.getStrata().get(k), Stratum.class);
            for (int i = 0; i < 3; i++) {
                City miner = new City(gameState, p.getReference());
                for (StoreableReference resource : stratum.minerals.keySet()) {
                    MineAreaFactory mineArea = new MineAreaFactory(c);

                    mineArea.setProductivity((float) (10 * (selector.nextDouble() + 0.1)));
                    mineArea.setResourceMined(resource);
                    mineArea.setMiningStratum(stratum.getReference());
                    mineArea.setWorkingmultiplier(1.5f);
                    mineArea.setOperatingJobs(50_000);
                    mineArea.setMaxJobs(100_000);
                    miner.addArea(mineArea.build(gameState).getReference());
                }

                //Add random production process
                ProductionProcess proc = c.getProductionProcesses().get(selector.nextInt(c.getProductionProcesses().size()));
                //Add new factory

                ManufacturerAreaFactory factory = new ManufacturerAreaFactory(c);
                factory.setProcess(proc);
                factory.setProductivity(1);
                factory.setMaxJobs(proc.difficulty * 10000);
                factory.setOperatingJobs(proc.difficulty * 5000);
                factory.setWorkingmultiplier(1.2f);
                miner.getAreas().add(factory.build(gameState).getReference());

                double randR = (stratum.getRadius() * Math.sqrt(selector.nextDouble()));
                double theta = (selector.nextDouble() * 2 * Math.PI);

                int x = (int) (Math.cos(theta) * randR) + stratum.getX();
                int y = (int) (Math.sin(theta) * randR) + stratum.getY();

                x %= p.getPlanetWidth();
                y %= p.getPlanetHeight();

                if (x < 0) {
                    x = p.getPlanetHeight() - x;
                }
                if (y < 0) {
                    y = p.getPlanetHeight() - y;
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
        return potato;
    }

    private void createFarms(Planet starting, Species crop, Civilization civ, Random selector, NameGenerator gen) {
        //Create production process for the food.
        StoreableReference consumableResources = civ.getFoundingSpecies().getConsumableResource();
        ProductionProcess foodProcess = new ProductionProcess(gameState);

        foodProcess.getInput().put(crop.getFoodGood(), 10d);
        foodProcess.getOutput().put(consumableResources, 10d);
        foodProcess.setName(crop.getName() + " to food");
        foodProcess.setDifficulty(1);

        civ.getProductionProcesses().add(foodProcess);

        //Add local life for the crop
        LocalLife localLife = new LocalLife();
        localLife.setSpecies(crop);
        localLife.setBiomass(100_000);
        starting.getLocalLife().add(localLife);

        //Get the thing...
        for (int i = 0; i < 10; i++) {
            City faceBook = new City(gameState, starting.getReference());

            //Add farm fields...
            for (int k = 0; k < 5; k++) {
                FarmFieldFactory field = new FarmFieldFactory(civ);

                field.setGrownCrop(crop.getReference());
                //30 days
                field.setTime(30 * 24);
                field.setProductivity(1);
                field.setFieldSize(5000);
                field.setOperatingJobs(1000);
                field.setMaxJobs(100000);

                faceBook.addArea(field.build(gameState).getReference());
            }

            //Add factory that is quite productive at giving food
            ManufacturerAreaFactory factory = new ManufacturerAreaFactory(civ);
            factory.setProductivity(50);
            factory.setProcess(foodProcess);

            factory.setMaxJobs(10000);
            factory.setOperatingJobs(5000);
            factory.setWorkingmultiplier(1.2f);
            faceBook.getAreas().add(factory.build(gameState).getReference());

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

    private void createUnrecruitedPeople(Civilization c, NameGenerator gen, Random selector) {
        c.getUnrecruitedPeople().clear();
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

            c.getUnrecruitedPeople().add(nerd.getReference());
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

            c.getUnrecruitedPeople().add(dude.getReference());
        }
    }

    private PersonalityTrait getRandomPersonalityTrait(Random selector) {
        return gameState.personalityTraits.get((selector.nextInt(gameState.personalityTraits.size())));
    }

    private void initializeTech(Civilization c, Random selector) {
        //Add fields
        c.fields = (Fields.toField(gameState.fieldNodeRoot));

        addStartingTechnologies(c, selector);

        //Add starting tech points...
        c.setTechPoints(CIV_STARTING_TECH_PTS);

        c.calculateTechLevel();
    }

    private void addStartingTechnologies(Civilization c, Random selector) {
        //Add all starting techs            
        for (Technology tech : Technologies.getTechsByTag(gameState, "Starting")) {
            c.researchTech(gameState, tech);
        }

        //Select one of the space travel sciences
        Technology[] teks = Technologies.getTechsByTag(gameState, "space travel base");

        //To research this
        c.getCivTechs().put(teks[selector.nextInt(teks.length)], 100);

        //Propulsion
        teks = Technologies.getTechsByTag(gameState, "Propulsion");
        //To research this
        c.getCivTechs().put(teks[selector.nextInt(teks.length)], 100);
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
        PeopleProcessor.placePerson(gameState.getObject(c.getCapitalCity(), PersonEnterable.class), r);

        c.getPeople().add(r.getReference());
    }

    //Governmental orgs...
    private void initializeOrgs(Civilization c, Planet planet) {
        Organization org = new Organization(gameState);
        org.setName("Ministry of Economic Planning");
        org.setBehavior(new ResourceManagerBehavior(gameState, org));
        //Sort through city
        for (ObjectReference cityId : planet.getCities()) {
            City city = gameState.getObject(cityId, City.class);

            org.region.bodies.add(city.getReference());
        }

        gameState.addOrganization(org);
        c.addChild(org.getReference());
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
        } while (planet.getCityDistributions().containsKey(pt));
        return pt;
    }

    //Test, will change in the future with a more robust system
    private void initializeGovernment(Civilization c, NameGenerator gen, Random selector) {
        //Create person
        String person = "name";
        person = gen.getName(selector.nextInt(gen.getRulesCount()), selector);
        Administrator dude = new Administrator(gameState, person, 400);
        //nerd.setSkill((int) (Math.random() * 5) + 1);
        dude.traits.add(getRandomPersonalityTrait(selector));
        dude.setPosition(c.getCapitalCity());
        dude.setRole("Ruling " + c.getSpeciesName());

        c.setGovernment(new Government());
        //Because democracy is for noobs
        c.getGovernment().politicalPowerSource = PoliticalPowerSource.Autocracy;
        //Set leader
        HeritableGovernmentPosition leader = new HeritableGovernmentPosition();
        leader.setName("God-Emperor");
        leader.setMethod(PoliticalPowerTransitionMethod.Inherit);
        c.getGovernment().officials.put(leader, dude);
        c.getGovernment().headofGovernment = leader;
        c.getGovernment().headofState = leader;
        PeopleProcessor.placePerson(gameState.getObject(c.getCapitalCity(), PersonEnterable.class), dude);
        dude.governmentPosition = leader;
        c.employ(dude.getReference());

        person = gen.getName(selector.nextInt(gen.getRulesCount()), selector);

        Administrator crownPrince = new Administrator(gameState, person, 0);
        //nerd.setSkill((int) (Math.random() * 5) + 1);
        crownPrince.traits.add(getRandomPersonalityTrait(selector));
        crownPrince.setPosition(c.getCapitalCity());
        crownPrince.setRole("Loafing around -- Lazy brat");
        //Add heir to the throne of the GOD EMPEROR

        GovernmentPosition crownPrincePosition = new GovernmentPosition();
        c.getGovernment().officials.put(crownPrincePosition, crownPrince);
        crownPrincePosition.setName("Crown Prince");
        leader.nextInLine = crownPrincePosition;
        crownPrince.governmentPosition = crownPrincePosition;

        PeopleProcessor.placePerson(gameState.getObject(c.getCapitalCity(), PersonEnterable.class), crownPrince);
        c.employ(crownPrince.getReference());
    }

    private void initVision(Civilization c, Galaxy u) {
        for (int i = 0; i < u.getStarSystemCount(); i++) {
            StarSystem s = u.getStarSystemObject(i);
            c.getVision().put(new UniversePath(i), VisionTypes.UNDISCOVERED);
            for (int h = 0; h < s.getBodyCount(); h++) {
                StarSystemBody b = s.getBodyObject(h);
                //Add planets

                c.getVision().put(new UniversePath(i, b.getIndex()), VisionTypes.UNDISCOVERED);
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
        for (ObjectReference strata : p.getStrata()) {
            Stratum stratum = gameState.getObject(strata, Stratum.class);
            stratum.setName(gen.getName(0, selector));
        }
    }

    private void addResearchInstitution(City city, Civilization civilization, NameGenerator gen, Random selector) {
        //Add to city
        String name = gen.getName(0, selector);
        ResearchAreaFactory researchAreaFactory = new ResearchAreaFactory(civilization);
        researchAreaFactory.setName(name);

        researchAreaFactory.setMaxJobs(30_000);
        researchAreaFactory.setOperatingJobs(15_000);

        ResearchArea area = (ResearchArea) researchAreaFactory.build(gameState);

        //Initialize science
        ArrayList<Field> fields = new ArrayList<>();
        //Remove the first one
        Field toFind = civilization.fields.getNode(0); //Because everyone does science for now, not magic when we add it...
        toFind.getFieldsExclusivse(fields);

        //Choose random field
        Field toAdd = fields.get(selector.nextInt(fields.size()));
        area.focusFields.put(toAdd.getName(), 1d);

        civilization.getScienceLabs().add(area.getReference());

        //Choose random fields
        city.addArea(area.getReference());
    }

    private void addCommercialArea(City c, Civilization civ) {
        CommercialAreaFactory area = new CommercialAreaFactory(civ);
        area.setMaxJobs(400_000);
        area.setOperatingJobs(1_000);
        area.setTradeValue(50_000);
        c.addArea(area.build(gameState).getReference());
    }

    private void addInfrastructure(Civilization civ, City city) {
        PowerPlantAreaFactory powerPlant = new PowerPlantAreaFactory(civ);

        powerPlant.setMaxVolume(5);
        powerPlant.setUsesResource(civ.getTaggedGoods().get("energy"));
        powerPlant.setProduction(5000);
        powerPlant.setOperatingJobs(5000);
        powerPlant.setMaxJobs(10000);

        city.addArea(powerPlant.build(gameState).getReference());
    }

    private void addSupplyLines(Planet p) {
        PlanetarySupplyLineGenerator gen = new PlanetarySupplyLineGenerator();
        gen.generate(gameState, p);
    }

    private void getIndustryChains(Civilization civ) {
        //Get consumer goods
        ArrayList<StoreableReference> consumerGoods = GoodUtil.findGoodsByTag(gameState, "consumer");
        for (StoreableReference consumerGood : consumerGoods) {
            Good good = gameState.getGood(consumerGood);
            //Idk do stuff
            //Get production processes that do this

            ProductionProcess generation = null;
            //Then it's a chain!
            for (ProductionProcess productionProcesse : civ.getProductionProcesses()) {
                if (productionProcesse.getOutput().containsKey(good.getId())) {
                    //Then it's the starting point
                    generation = productionProcesse;
                    break;
                }
            }
            System.out.println(generation.getName());
            System.out.println(findInputs(generation, civ));
            //Get children until it's raw material refining I guess...
        }
    }

    private ArrayList<ProductionProcess> findInputs(ProductionProcess process, Civilization civ) {

        ArrayList<ProductionProcess> inputs = new ArrayList<>();
        if (process.containsTag("raw_processing")) {
            return inputs;
        }
        for (Map.Entry<StoreableReference, Double> en : process.getInput().entrySet()) {
            StoreableReference key = en.getKey();
            Double val = en.getValue();
            //Get ratios too I guess...
            for (ProductionProcess civProcess : civ.getProductionProcesses()) {
                if (civProcess.getOutput().containsKey(key)) {
                    //Then the inputs are these
                    inputs.add(civProcess);

                    //Also process the children
                    inputs.addAll(findInputs(civProcess, civ));
                }
            }
        }
        return inputs;
    }
}
