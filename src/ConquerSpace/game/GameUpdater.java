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

import ConquerSpace.game.actions.Action;
import ConquerSpace.game.actions.ActionStatus;
import ConquerSpace.game.actions.Actions;
import static ConquerSpace.game.actions.Actions.removeResource;
import static ConquerSpace.game.actions.Actions.sendResources;
import static ConquerSpace.game.actions.Actions.storeResource;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.actions.ShipAction;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.organizations.civilization.vision.VisionPoint;
import ConquerSpace.game.organizations.civilization.vision.VisionTypes;
import ConquerSpace.game.city.City;
import ConquerSpace.game.city.CityType;
import ConquerSpace.game.city.area.Area;
import ConquerSpace.game.city.area.AreaClassification;
import ConquerSpace.game.city.area.ConstructingArea;
import ConquerSpace.game.city.area.FarmFieldArea;
import ConquerSpace.game.city.area.ManufacturerArea;
import ConquerSpace.game.city.area.MineArea;
import ConquerSpace.game.city.area.ObservatoryArea;
import ConquerSpace.game.city.area.PowerPlantArea;
import ConquerSpace.game.city.area.ResearchArea;
import ConquerSpace.game.city.area.SpacePortArea;
import ConquerSpace.game.city.area.TimedManufacturerArea;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.organizations.Organization;
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.population.PopulationSegment;
import ConquerSpace.game.science.Field;
import ConquerSpace.game.science.ScienceLab;
import ConquerSpace.game.science.tech.Technologies;
import ConquerSpace.game.science.tech.Technology;
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.ships.launch.SpacePortLaunchPad;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.bodies.Body;
import ConquerSpace.game.universe.bodies.ControlTypes;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Star;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.game.resources.ProductionProcess;
import ConquerSpace.game.resources.ResourceStockpile;
import ConquerSpace.game.universe.SpacePoint;
import static ConquerSpace.game.universe.generators.DefaultUniverseGenerator.AU_IN_LTYR;
import ConquerSpace.util.ExceptionHandling;
import ConquerSpace.util.logging.CQSPLogger;
import ConquerSpace.util.names.NameGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.apache.logging.log4j.Logger;

/**
 * This controls the game.
 *
 * @author EhWhoAmI
 */
public class GameUpdater extends GameTicker {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameUpdater.class.getName());

    private Universe universe;

    private StarDate starDate;

    private GameState gameState;

    private final int GameRefreshRate;

    private long updateTime;
    private int ledgerClearTime;

    private GameIndexer indexer;
    private PeopleProcessor peopleProcessor;

    public GameUpdater(GameState gameState) {
        universe = gameState.universe;
        starDate = gameState.date;
        this.gameState = gameState;
        indexer = new GameIndexer(gameState.universe);
        peopleProcessor = new PeopleProcessor(gameState.universe, gameState.date);
        this.GameRefreshRate = gameState.GameRefreshRate;
        ledgerClearTime = GameRefreshRate * 10;
    }

    //Process ingame tick.
    @Override
    public synchronized void tick(int tickIncrement) {
        //DO ticks
        starDate.increment(tickIncrement);

        updateObjectPositions();

        //Execute org actions
        performActions();

        //Move ships
        moveShips();

        calculateControl();
        calculateVision();

        //Check for month increase            
        if (starDate.bigint % GameRefreshRate == 1) {
            LOGGER.trace("Refreshing all the game objects");
            updateGame();
            for (int i = 0; i < universe.getCivilizationCount(); i++) {
                indexer.index(universe.getCivilization(i));
            }
        }
        //Process people and generate every 1000 ticks, which is about every 41 days
        if (starDate.bigint % (GameRefreshRate * 2) == 1) {
            createPeople();
        }
    }

    public synchronized void updateGame() {
        long start = System.currentTimeMillis();
        updateUniverse();

        //Increment tech
        processResearch(GameRefreshRate);
        for (int i = 0; i < gameState.universe.getCivilizationCount(); i++) {
            gameState.universe.getCivilization(i).calculateTechLevel();
        }

        //Increment resources
        processResources();

        peopleProcessor.processPeople(GameRefreshRate);

        long end = System.currentTimeMillis();

        updateTime = (end - start);
    }

    //Will have to check for actions that contradict each other or something in the future..
    public void performActions() {
        for (Map.Entry<Integer, Organization> entry : universe.organizations.entrySet()) {
            Integer key = entry.getKey();
            Organization org = entry.getValue();

            //Run code...
            org.getBehavior().doBehavior();
            for (int k = 0; k < org.actionList.size(); k++) {
                try {
                    Action act = org.actionList.get(k);
                    ActionStatus status = act.doAction(gameState); //???
                } catch (Exception e) {
                    ExceptionHandling.ExceptionMessageBox("Exception while executing actions!", e);
                }
            }
            org.actionList.clear();
        }
    }

    public void calculateControl() {
        for (UniversePath p : universe.control.keySet()) {
            Body spaceObject = universe.getSpaceObject(p);

            //Do a run through of all planets.
            if (spaceObject instanceof Planet) {
                Planet planet = (Planet) spaceObject;
                //Get the control.
                universe.control.put(p, planet.getOwnerID());
            } else if (spaceObject instanceof Star) {
                Star star = (Star) spaceObject;
                //Get the control.
                universe.control.put(p, star.getOwnerID());
            } else if (spaceObject instanceof StarSystem) {
                StarSystem starsystem = (StarSystem) spaceObject;
                int owner = -1;
                for (int i = 0; i < starsystem.bodies.size(); i++) {
                    Body body = starsystem.bodies.get(i);
                    if (body instanceof Planet) {
                        Planet planet = (Planet) body;
                        if (owner == -1 && planet.getOwnerID() > -1) {
                            owner = planet.getOwnerID();
                        } else if (owner != planet.getOwnerID() && planet.getOwnerID() != -1) {
                            owner = ControlTypes.DISPUTED;
                        }
                    }
                }
                universe.control.put(p, owner);
            }
        }
    }

    public void calculateVision() {
        for (UniversePath p : universe.control.keySet()) {
            //Vision is always visible when you control it
            int civIndex = universe.control.get(p);
            if (civIndex > -1) {
                universe.getCivilization(civIndex).vision.put(p, VisionTypes.KNOWS_ALL);
            }
        }

        //Loop through all the vision points in the universe
        for (int civ = 0; civ < universe.getCivilizationCount(); civ++) {
            Civilization civil = universe.getCivilization(civ);
            for (VisionPoint pt : civil.visionPoints) {
                int range = pt.getRange();
                //Distance between all star systems...
                UniversePath path = pt.getPosition();
                if (path.getSystemID() > -1) {
                    //Get system position
                    SpacePoint pos = universe.getSpaceObject(new UniversePath(path.getSystemID())).point;
                    for (int g = 0; g < universe.getStarSystemCount(); g++) {
                        //Difference between points...
                        double dist = Math.hypot(pos.getY() - universe.getStarSystem(g).getY(),
                                pos.getX() - universe.getStarSystem(g).getX());
                        if (dist < (range * AU_IN_LTYR)) {
                            //Its in!
                            int amount = ((int) ((1 - (dist / (double) (range * AU_IN_LTYR))) * 100));
                            //int previous = universe.getCivilization(pt.getCivilization().vision.get(universe.getStarSystem(g).getUniversePath()));
                            universe.getCivilization(civ).vision.put(universe.getStarSystem(g).getUniversePath(),
                                    (amount > 100) ? 100 : (amount));
                        }
                    }
                }
            }
        }
    }

    public void updateUniverse() {
        //Loop through star systems
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            updateStarSystem(universe.getStarSystem(i));
        }
    }

    private void updateStarSystem(StarSystem sys) {
        //Maybe later the objects in space.
        for (int i = 0; i < sys.bodies.size(); i++) {
            Body body = sys.bodies.get(i);
            if (body instanceof Planet) {
                processPlanet((Planet) body);
            } else if (body instanceof Star) {
                processStar((Star) body);
            }
        }
    }

    private void processPlanet(Planet planet) {
        if (planet.isHabitated()) {
            //So we know how many people there are
            doPlanetCensus(planet);

            processCities(planet);
        }
        processLocalLife(planet);
    }

    /**
     * Increments population of city, creates city jobs, and assigns them.
     *
     * @param planet
     * @param date
     * @param delta
     */
    private void processCities(Planet planet) {
        for (City city : planet.cities) {
            //Clear ledger
            city.resourceLedger.clear();

            //Process city demands
            //Create orders from cities that have stuff
            //Clear demands
            city.resourceDemands.clear();

            processPopulation(city);

            //Population growth
            calculateCityJobs(city);

            for (Area a : city.areas) {
                processArea(planet, city, a);
            }

            //Replace constructing areas
            Iterator<Area> areaIterator = city.areas.iterator();
            ArrayList<Area> areasToAdd = new ArrayList<>();
            while (areaIterator.hasNext()) {
                Area area = areaIterator.next();
                if (area instanceof ConstructingArea && ((ConstructingArea) area).getTicksLeft() <= 0) {
                    areasToAdd.add(((ConstructingArea) area).getToBuild());
                    areaIterator.remove();
                }
            }
            city.areas.addAll(areasToAdd);

            CityType type = classifyDistrict(city);
            city.setCityType(type);
        }

        //distributeResources(p);
    }

    /**
     * See the amount of jobs that are filled
     *
     * @param city
     * @param date
     */
    private void calculateCityJobs(City city) {
        long maxJobsProviding = 0;
        long necessaryJobsProviding = 0;
        long size = city.population.getPopulationSize();

        //Sort them out based off piority
        Collections.sort(city.areas);

        for (Area a : city.areas) {
            necessaryJobsProviding += a.operatingJobsNeeded();
            maxJobsProviding += a.getMaxJobsProvided();
        }

        if (maxJobsProviding < size) {
            //Fill necessary jobs if there are not enough people to get the max amount of people
            for (Area a : city.areas) {
                a.setCurrentlyManningJobs(a.getMaxJobsProvided());
            }
        } else if (necessaryJobsProviding < size) {
            //Fill all the jobs needed to operate

            for (Area a : city.areas) {
                size -= a.operatingJobsNeeded();
                a.setCurrentlyManningJobs(a.operatingJobsNeeded());
            }
            //Go through again, and add the jobs
            for (Area a : city.areas) {
                int toFill = a.getMaxJobsProvided() - a.operatingJobsNeeded();

                if ((size - toFill) > 0) {
                    a.setCurrentlyManningJobs(a.getMaxJobsProvided());
                } else {
                    a.setCurrentlyManningJobs((a.operatingJobsNeeded() + (int) size));
                    break;
                }
            }
        } else {
            //Not enough jobs, so fill stuff according to piority
            for (Area area : city.areas) {
                int jobsToAdd = area.operatingJobsNeeded();
                if ((size - jobsToAdd) > 0) {
                    size -= jobsToAdd;
                    area.setCurrentlyManningJobs(jobsToAdd);
                }
            }
        }
    }

    private void processPopulation(City city) {
        //Process population upkeep
        for (PopulationSegment seg : city.population.populations) {
            //Request resources
            long amount = (seg.size / 1000);
            double consume = ((double) amount) * 0.5d;

            double foodAmount = 0;
            if (city.resources.containsKey(universe.races.get(seg.species).food)) {
                foodAmount = city.getResourceAmount(universe.races.get(seg.species).food);
            }

            //Request food
            //Append resources
            city.resourceDemands.addValue(universe.races.get(seg.species).food, consume);

            boolean success = removeResource(universe.races.get(seg.species).food, consume, city);
            //Not enough food
            boolean starving = false;
            if (!success) {
                //can probably calculate other stuff, but who cares for now
                //Calculate ratio of food
                double populationConsumption = foodAmount * 2;
                double percentage = (populationConsumption / consume);
                percentage = 1d - percentage;
                city.tags.put("Starvation", (int) (percentage * 100d));
                starving = true;
            } else {
                city.tags.remove("Starvation");
            }

            //Increment population
            double fraction = ((double) GameRefreshRate) / 10000d;

            //if starving, then don't increment
            if (!starving) {
                seg.size = (long) ((double) seg.size * ((1 + seg.populationIncrease * fraction)));
            }
        }
    }

    private void doPlanetCensus(Planet p) {
        //Index panet population
        long total = 0;
        for (City c : p.cities) {
            total += c.population.getPopulationSize();
        }
        p.population = total;
    }

    private void distributeResources(Planet planet) {
        //Find stuff
        for (City c : planet.cities) {
            //Resources needed, keep
            HashMap<Integer, Double> resourcesToSpend = new HashMap<>();
            for (Map.Entry<Integer, Double> entry : c.resourceDemands.entrySet()) {
                Integer key = entry.getKey();
                Double val = entry.getValue();
                if (c.resources.containsKey(key)) {
                    double amount = c.resources.get(key) - val;
                    if (amount > 0) {
                        resourcesToSpend.put(key, amount);
                    }
                }
            }

            //Then, distribute resources
            for (City cit : planet.cities) {
                if (!cit.equals(c)) {
                    //Send the resources to other places
                    for (Map.Entry<Integer, Double> entry : cit.resourceDemands.entrySet()) {
                        Integer key = entry.getKey();
                        Double val = entry.getValue();
                        //If have enough resources to put in
                        if (resourcesToSpend.containsKey(key)) {
                            double toSpendAmount = resourcesToSpend.get(key);
                            if (val > 0 && toSpendAmount > 0) {
                                if (val > toSpendAmount) {
                                    //Send the resources
                                    sendResources(key, toSpendAmount, c, cit);
                                    //Subtract resources
                                    resourcesToSpend.put(key, 0d);
                                } else {
                                    sendResources(key, val, c, cit);
                                    resourcesToSpend.put(key, (toSpendAmount - val));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void processArea(Planet planet, City city, Area area) {
        if (area instanceof FarmFieldArea) {
            FarmFieldArea farm = (FarmFieldArea) area;
            int removed = farm.tick(GameRefreshRate);
            if (removed > 0 && farm.operatingJobsNeeded() < farm.getCurrentlyManningJobs()) {
                //Calculate percentage
                storeResource(farm.getGrown().getFoodGood(), (removed * (double) farm.getFieldSize()), city);

                farm.grow();
            }
        } else if (area instanceof TimedManufacturerArea) {
            //Subtract time
            TimedManufacturerArea timedFactory = (TimedManufacturerArea) area;
            int removed = timedFactory.tick(GameRefreshRate);

            if (area.operatingJobsNeeded() < area.getCurrentlyManningJobs()) {
                for (Map.Entry<Integer, Double> entry : timedFactory.getProcess().output.entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();

                    //Get percentage
                    storeResource(key, val * removed, city);
                }
            }
        }
        if (area instanceof PowerPlantArea) {
            PowerPlantArea powerPlant = (PowerPlantArea) area;
            if (area.operatingJobsNeeded() < area.getCurrentlyManningJobs()) {
                removeResource(powerPlant.getUsedResource(), Double.valueOf(powerPlant.getMaxVolume()), city);
            }
        } else if (area instanceof ManufacturerArea) {
            //Process resources used
            ProductionProcess process = ((ManufacturerArea) area).getProcess();
            if (area.operatingJobsNeeded() < area.getCurrentlyManningJobs()) {
                //Query resources
                for (Map.Entry<Integer, Double> entry : process.input.entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();
                    Double amount = city.resources.get(key);
                    removeResource(key, val * GameRefreshRate, city);
                    city.resourceDemands.addValue(key, val);
                }

                for (Map.Entry<Integer, Double> entry : process.output.entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();

                    storeResource(key, val * GameRefreshRate, city);
                }
            }
        } else if (area instanceof ResearchArea) {

        } else if (area instanceof MineArea) {
            MineArea mine = (MineArea) area;
            if (mine.operatingJobsNeeded() < mine.getCurrentlyManningJobs()) {
                for (Map.Entry<Integer, Double> entry : mine.getNecessaryGoods().entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();
                    removeResource(key, val * GameRefreshRate, city);
                }

                double multiplier = getMultiplier(area);

                storeResource(mine.getResourceMinedId(), Double.valueOf(mine.getProductivity() * GameRefreshRate) * multiplier, city);
            }
        } else if (area instanceof SpacePortArea) {
            SpacePortArea spacePort = (SpacePortArea) area;
            for (int i = 0; i < spacePort.launchPads.size(); i++) {
                SpacePortLaunchPad pad = spacePort.launchPads.get(i);
                pad.ticks -= GameRefreshRate;
                if (pad.ticks <= 0) {
                    //Launch!
                    //Get the owner somehow...
                    Actions.launchLaunchable(pad.getLaunching(), planet);
                }
            }
        } else if (area instanceof ObservatoryArea) {
            ObservatoryArea observatory = (ObservatoryArea) area;
            //Just slightly inelagant code to get the vision points
            if (!gameState.universe.civs.get(observatory.getCivilization()).visionPoints.contains(observatory)) {
                gameState.universe.civs.get(observatory.getCivilization()).visionPoints.add(observatory);
            }
        }

        if (area instanceof ConstructingArea) {
            ConstructingArea constructingArea = (ConstructingArea) area;
            constructingArea.tickConstruction(GameRefreshRate);
        }
    }

    private void processLocalLife(Planet p) {
        //Process locallife
        for (LocalLife localLife : p.localLife) {
            int biomass = localLife.getBiomass();
            float breedingRate = localLife.getSpecies().getBaseBreedingRate();
            localLife.setBiomass((int) (breedingRate * biomass) + biomass);
        }
    }

    private void processStar(Star s) {

    }

    private void processResearch(int delta) {
        for (int i = 0; i < gameState.universe.getCivilizationCount(); i++) {
            Civilization c = gameState.universe.getCivilization(i);

            Iterator<Technology> tech = c.currentlyResearchingTechonologys.keySet().iterator();

            while (tech.hasNext()) {
                Technology t = tech.next();

                if ((Technologies.estFinishTime(t) - c.civResearch.get(t)) <= 0) {
                    //Then tech is finished
                    c.researchTech(gameState, t);
                    c.civResearch.remove(t);
                    //c.currentlyResearchingTechonologys.remove(t);
                    tech.remove();
                    //Alert civ
                    c.controller.alert(new Alert(0, 0, "Tech " + t.getName() + " is finished"));
                } else {
                    //Increment by number of ticks
                    c.civResearch.put(t, c.civResearch.get(t) + c.currentlyResearchingTechonologys.get(t).getSkill() * delta);
                }
            }

            //Process science labs
            for (ScienceLab scienceLab : c.scienceLabs) {
                HashMap<String, Integer> science = scienceLab.scienceProvided();
                for (Map.Entry<String, Integer> entry : science.entrySet()) {
                    String key = entry.getKey();
                    Integer val = entry.getValue();

                    Field f = c.fields.findNode(key);
                    f.incrementLevel(val);
                }
            }
        }
    }

    private void processResources() {
        for (int i = 0; i < gameState.universe.getCivilizationCount(); i++) {
            Civilization c = gameState.universe.getCivilization(i);
            for (Map.Entry<Integer, Double> entry : c.resourceList.entrySet()) {
                c.resourceList.put(entry.getKey(), 0d);
            }
            //Process resources
            for (ResourceStockpile s : c.resourceStorages) {
                //Get resource types allowed, and do stuff
                //c.resourceList.
                for (Integer type : s.storedTypes()) {
                    //add to index
                    if (!c.resourceList.containsKey(type)) {
                        c.resourceList.put(type, 0d);
                    }
                    Double amountToAdd = (c.resourceList.get(type) + s.getResourceAmount(type));
                    c.resourceList.put(type, amountToAdd);
                }
            }
        }
    }

    private CityType classifyDistrict(City dis) {
        //Get the type of areas
        HashMap<AreaClassification, Integer> areaType = new HashMap<>();
        for (Area a : dis.areas) {
            if (areaType.containsKey(a.getAreaType())) {
                Integer num = areaType.get(a.getAreaType());
                num++;
                areaType.put(a.getAreaType(), num);
            } else {
                areaType.put(a.getAreaType(), 1);
            }
        }

        //Calulate stuff
        int highest = 0;
        AreaClassification highestArea = AreaClassification.Generic;
        for (Map.Entry<AreaClassification, Integer> entry : areaType.entrySet()) {
            AreaClassification key = entry.getKey();
            Integer val = entry.getValue();
            if (val > highest && key != AreaClassification.Generic) {
                highest = val;
                highestArea = key;
            }
        }

        switch (highestArea) {
            case Financial:
                return CityType.City;
            case Generic:
                return CityType.Generic;
            case Infrastructure:
                return CityType.Infrastructure;
            case Research:
                return CityType.Research;
            case Residential:
                return CityType.City;
            case Manufacturing:
                //City because it represents it better
                return CityType.City;
            case Farm:
                return CityType.Farm;
            case Mine:
                return CityType.Mine;
        }
        return CityType.Generic;
    }

    private void moveShips() {
        for (int sys = 0; sys < gameState.universe.getCivilizationCount(); sys++) {
            Civilization c = gameState.universe.getCivilization(sys);
            //Process ship actions
            for (Ship ship : c.spaceships) {
                ShipAction sa = ship.getActionAndPopIfDone(gameState);

                if (!sa.checkIfDone(gameState)) {
                    //System.out.println(sa.getClass());
                    sa.doAction(gameState);
                } else {
                    //Next action and init
                    ship.getActionAndPopIfDone(gameState).initAction(gameState);
                }
            }
        }
    }

    public void updateObjectPositions() {
        long start = System.currentTimeMillis();
        //Loop through star systems
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            StarSystem system = universe.getStarSystem(i);

            system.setPoint(system.getOrbit().toSpacePoint());

            for (int k = 0; k < system.bodies.size(); k++) {
                Body body = system.bodies.get(k);
                body.setPoint(body.getOrbit().toSpacePoint());
            }
        }
        long end = System.currentTimeMillis();
        //System.out.println((end - start));
    }

    private void createPeople() {
        for (int i = 0; i < gameState.universe.getCivilizationCount(); i++) {
            Civilization civ = gameState.universe.getCivilization(i);
            NameGenerator gen = null;
            try {
                gen = NameGenerator.getNameGenerator("us.names");
            } catch (IOException ex) {
                //Ignore
            }
            //Create 5-10 random scientists
            civ.unrecruitedPeople.clear();
            int peopleCount = (int) (Math.random() * 5) + 5;
            for (int peep = 0; peep < peopleCount; peep++) {
                int age = (int) (Math.random() * 40) + 20;
                String person = "name";
                person = gen.getName((int) Math.round(Math.random()));
                Scientist nerd = new Scientist(person, age);
                nerd.setSkill((int) (Math.random() * 5) + 1);
                nerd.traits.add(getRandomPersonalityTrait());
                nerd.setPosition(civ.getCapitalCity());

                civ.unrecruitedPeople.add(nerd);
                //Generate personality
            }
            //Admins
            peopleCount = (int) (Math.random() * 5) + 5;

            for (int peep = 0; peep < peopleCount; peep++) {
                int age = (int) (Math.random() * 40) + 20;
                String person = "name";
                person = gen.getName((int) Math.round(Math.random()));
                Administrator dude = new Administrator(person, age);
                dude.traits.add(getRandomPersonalityTrait());
                dude.setPosition(civ.getCapitalCity());

                //nerd.setSkill((int) (Math.random() * 5) + 1);
                civ.unrecruitedPeople.add(dude);
            }
        }
    }

    private PersonalityTrait getRandomPersonalityTrait() {
        return gameState.personalityTraits.get((int) (gameState.personalityTraits.size() * Math.random()));
    }

    private double getMultiplier(Area area) {
        double extraJobs = area.getCurrentlyManningJobs() - area.operatingJobsNeeded();

        double multiplier = 1;
        if (extraJobs > 0) {
            double maxExtraJobs = area.getMaxJobsProvided() - area.operatingJobsNeeded();

            float workingMultiplier = area.getWorkingmultiplier() - 1f;

            double extraJobsRatio = extraJobs / maxExtraJobs;
            if (extraJobsRatio > 1) {
                extraJobsRatio = 1;
            }
            double multiplyBy = 1d + extraJobsRatio * workingMultiplier;

            multiplier = multiplyBy;
        }
        return multiplier;
    }
}
