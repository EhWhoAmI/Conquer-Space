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
package ConquerSpace.server;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameIndexer;
import ConquerSpace.common.GameState;
import ConquerSpace.common.GameTicker;
import ConquerSpace.common.StarDate;
import ConquerSpace.common.actions.Action;
import ConquerSpace.common.actions.ActionStatus;
import ConquerSpace.common.actions.Actions;
import static ConquerSpace.common.actions.Actions.removeResource;
import static ConquerSpace.common.actions.Actions.sendResources;
import static ConquerSpace.common.actions.Actions.storeResource;
import ConquerSpace.common.actions.Alert;
import ConquerSpace.common.actions.ShipAction;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.organizations.civilization.vision.VisionPoint;
import ConquerSpace.common.game.organizations.civilization.vision.VisionTypes;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.CityType;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.AreaClassification;
import ConquerSpace.common.game.city.area.ConstructingArea;
import ConquerSpace.common.game.city.area.FarmFieldArea;
import ConquerSpace.common.game.city.area.ManufacturerArea;
import ConquerSpace.common.game.city.area.MineArea;
import ConquerSpace.common.game.city.area.ObservatoryArea;
import ConquerSpace.common.game.city.area.PowerPlantArea;
import ConquerSpace.common.game.city.area.ResearchArea;
import ConquerSpace.common.game.city.area.SpacePortArea;
import ConquerSpace.common.game.city.area.TimedManufacturerArea;
import ConquerSpace.common.game.life.LocalLife;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.game.characters.Administrator;
import ConquerSpace.common.game.characters.PersonalityTrait;
import ConquerSpace.common.game.characters.Scientist;
import ConquerSpace.common.game.population.Population;
import ConquerSpace.common.game.population.PopulationSegment;
import ConquerSpace.common.game.population.Race;
import ConquerSpace.common.game.science.Field;
import ConquerSpace.common.game.science.ScienceLab;
import ConquerSpace.common.game.science.Technologies;
import ConquerSpace.common.game.science.Technology;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.ships.launch.SpacePortLaunchPad;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.bodies.Body;
import ConquerSpace.common.game.universe.bodies.ControlTypes;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.Star;
import ConquerSpace.common.game.universe.bodies.StarSystem;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.universe.SpacePoint;
import static ConquerSpace.server.generators.DefaultUniverseGenerator.AU_IN_LTYR;
import ConquerSpace.common.util.ExceptionHandling;
import ConquerSpace.common.util.logging.CQSPLogger;
import ConquerSpace.common.util.names.NameGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Logger;

/**
 * This controls the game.
 *
 * @author EhWhoAmI
 */
public class GameUpdater extends GameTicker {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameUpdater.class.getName());

    private Galaxy universe;

    private StarDate starDate;

    private GameState gameState;

    private final int GameRefreshRate;

    private long updateTime;
    private int ledgerClearTime;

    private GameIndexer indexer;
    private PeopleProcessor peopleProcessor;

    public GameUpdater(GameState gameState) {
        universe = gameState.getUniverse();
        starDate = gameState.date;
        this.gameState = gameState;
        indexer = new GameIndexer(gameState.getUniverse());
        peopleProcessor = new PeopleProcessor(gameState);
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
        if (starDate.getDate() % GameRefreshRate == 1) {
            LOGGER.trace("Refreshing all the game objects");
            updateGame();
            for (int i = 0; i < gameState.getCivilizationCount(); i++) {
                //int civId = universe.getCivilization(i);
                //indexer.index(((Civilization) universe.organizations.get((universe.getCivilization(i)))));
            }
        }
        //Process people and generate every 1000 ticks, which is about every 41 days
        if (starDate.getDate() % (GameRefreshRate * 2) == 1) {
            createPeople();
        }
    }

    public synchronized void updateGame() {
        long start = System.currentTimeMillis();
        updateUniverse();

        //Increment tech
        processResearch(GameRefreshRate);
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            gameState.getCivilizationObject(i).calculateTechLevel();
        }

        //Increment resources
        processResources();

        peopleProcessor.processPeople(GameRefreshRate);

        long end = System.currentTimeMillis();

        updateTime = (end - start);
    }

    //Will have to check for actions that contradict each other or something in the future..
    public void performActions() {
        for (int i = 0; i < gameState.getOrganizationCount(); i++) {
            Organization org = gameState.getCivilizationObject(i);

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
        for (Integer p : universe.control.keySet()) {
            StarSystem starsystem = gameState.getObject(p, StarSystem.class);
            int owner = -1;
            for (int i = 0; i < starsystem.getBodyCount(); i++) {
                Body body = gameState.getObject(starsystem.getBody(i), Body.class);
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

    public void calculateVision() {
        for (Integer p : universe.control.keySet()) {
            //Vision is always visible when you control it
            int civIndex = universe.control.get(p);
            if (civIndex > -1) {
                Civilization civil = gameState.getObject(civIndex, Civilization.class);
                //Deal with later...
                //civil.vision.put(p, VisionTypes.KNOWS_ALL);
            }
        }

        //Loop through all the vision points in the universe
        for (int civ = 0; civ < gameState.getCivilizationCount(); civ++) {
            int civid = gameState.getCivilization(civ);
            Civilization civil = gameState.getObject(civid, Civilization.class);
            for (Integer ptid : civil.visionPoints) {
                ConquerSpaceGameObject object = gameState.getObject(ptid);
                if (object instanceof VisionPoint) {
                    VisionPoint pt = (VisionPoint) object;
                    int range = pt.getRange();
                    //Distance between all star systems...
                    UniversePath path = pt.getPosition();
                    if (path.getSystemID() > -1) {
                        //Get system position
                        Body body = gameState.getObject(universe.getSpaceObject(new UniversePath(path.getSystemID())), Body.class);
                        SpacePoint pos = body.point;
                        for (int g = 0; g < universe.getStarSystemCount(); g++) {
                            //Difference between points...
                            double dist = Math.hypot(pos.getY() - universe.getStarSystemObject(g).getY(),
                                    pos.getX() - universe.getStarSystemObject(g).getX());
                            if (dist < (range * AU_IN_LTYR)) {
                                //Its in!
                                int amount = ((int) ((1 - (dist / (double) (range * AU_IN_LTYR))) * 100));
                                //int previous = universe.getCivilization(pt.getCivilization().vision.get(universe.getStarSystem(g).getUniversePath()));
                                civil.vision.put(universe.getStarSystemObject(g).getUniversePath(),
                                        (amount > 100) ? 100 : (amount));
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateUniverse() {
        //Loop through star systems
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            updateStarSystem(universe.getStarSystemObject(i));
        }
    }

    private void updateStarSystem(StarSystem sys) {
        //Maybe later the objects in space.
        for (int i = 0; i < sys.getBodyCount(); i++) {
            Body body = sys.getBodyObject(i);
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
        for (Integer cityId : planet.cities) {
            City city = gameState.getObject(cityId, City.class);
            //Clear ledger
            city.resourceLedger.clear();

            //Process city demands
            //Create orders from cities that have stuff
            //Clear demands
            city.resourceDemands.clear();

            processPopulation(city);

            //Population growth
            calculateCityJobs(city);

            for (Integer areaId : city.areas) {
                Area area = gameState.getObject(areaId, Area.class);
                processArea(planet, city, area);
            }

            //Replace constructing areas
            Iterator<Integer> areaIterator = city.areas.iterator();
            ArrayList<Integer> areasToAdd = new ArrayList<>();
            while (areaIterator.hasNext()) {
                Area area = gameState.getObject(areaIterator.next(), Area.class);

                if (area instanceof ConstructingArea && ((ConstructingArea) area).getTicksLeft() <= 0) {
                    areasToAdd.add(((ConstructingArea) area).getToBuild().getId());
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
        long size = gameState.getObject(city.population, Population.class).getPopulationSize();

        //Sort them out based off piority
        Collections.sort(city.areas);

        for (Integer areaId : city.areas) {
            Area area = gameState.getObject(areaId, Area.class);
            necessaryJobsProviding += area.operatingJobsNeeded();
            maxJobsProviding += area.getMaxJobsProvided();
        }

        if (maxJobsProviding < size) {
            //Fill necessary jobs if there are not enough people to get the max amount of people
            for (Integer areaId : city.areas) {
                Area area = gameState.getObject(areaId, Area.class);
                area.setCurrentlyManningJobs(area.getMaxJobsProvided());
            }
        } else if (necessaryJobsProviding < size) {
            //Fill all the jobs needed to operate

            for (Integer areaId : city.areas) {
                Area area = gameState.getObject(areaId, Area.class);
                size -= area.operatingJobsNeeded();
                area.setCurrentlyManningJobs(area.operatingJobsNeeded());
            }
            //Go through again, and add the jobs
            for (Integer areaId : city.areas) {
                Area area = gameState.getObject(areaId, Area.class);
                int toFill = area.getMaxJobsProvided() - area.operatingJobsNeeded();

                if ((size - toFill) > 0) {
                    area.setCurrentlyManningJobs(area.getMaxJobsProvided());
                } else {
                    area.setCurrentlyManningJobs((area.operatingJobsNeeded() + (int) size));
                    break;
                }
            }
        } else {
            //Not enough jobs, so fill stuff according to piority
            for (Integer areaId : city.areas) {
                Area area = gameState.getObject(areaId, Area.class);
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
        Population pop = gameState.getObject(city.population, Population.class);
        for (Integer segid : pop.populations) {
            PopulationSegment seg = gameState.getObject(segid, PopulationSegment.class);
            //Request resources
            long amount = (seg.size / 1000);
            double consume = ((double) amount) * 0.5d;

            double foodAmount = 0;

            if (city.resources.containsKey(gameState.getObject(seg.species, Race.class).food)) {
                foodAmount = city.getResourceAmount(gameState.getObject(seg.species, Race.class).food);
            }

            //Request food
            //Append resources
            city.resourceDemands.addValue(gameState.getObject(seg.species, Race.class).food, consume);

            boolean success = removeResource(gameState.getObject(seg.species, Race.class).food, consume, city);
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
        for (Integer cityId : p.cities) {
            City city = gameState.getObject(cityId, City.class);
            total += gameState.getObject(city.population, Population.class).getPopulationSize();
        }
        p.population = total;
    }

    private void distributeResources(Planet planet) {
        //Find stuff
        for (Integer cityId : planet.cities) {
            City city = gameState.getObject(cityId, City.class);
            //Resources needed, keep
            HashMap<Integer, Double> resourcesToSpend = new HashMap<>();
            for (Map.Entry<Integer, Double> entry : city.resourceDemands.entrySet()) {
                Integer key = entry.getKey();
                Double val = entry.getValue();
                if (city.resources.containsKey(key)) {
                    double amount = city.resources.get(key) - val;
                    if (amount > 0) {
                        resourcesToSpend.put(key, amount);
                    }
                }
            }

            //Then, distribute resources
            for (Integer citId : planet.cities) {
                City cit = gameState.getObject(citId, City.class);
                if (!cit.equals(city)) {
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
                                    sendResources(key, toSpendAmount, city, cit);
                                    //Subtract resources
                                    resourcesToSpend.put(key, 0d);
                                } else {
                                    sendResources(key, val, city, cit);
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
            //Just slightly inelegant code to get the vision points
            //TODO...
            //if (!((Civilization) gameState.universe.organizations.get(gameState.universe.civs.get(observatory.getCivilization()))).visionPoints.contains(observatory)) {
            //((Civilization) gameState.universe.organizations.get(gameState.universe.civs.get(observatory.getCivilization()))).visionPoints.add(observatory);
            //}
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
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);

            Iterator<Technology> tech = civilization.currentlyResearchingTechonologys.keySet().iterator();

            while (tech.hasNext()) {
                Technology t = tech.next();

                if ((Technologies.estFinishTime(t) - civilization.civResearch.get(t)) <= 0) {
                    //Then tech is finished
                    civilization.researchTech(gameState, t);
                    civilization.civResearch.remove(t);
                    //c.currentlyResearchingTechonologys.remove(t);
                    tech.remove();
                    //Alert civ
                    civilization.controller.alert(new Alert(0, 0, "Tech " + t.getName() + " is finished"));
                } else {
                    //Increment by number of ticks
                    civilization.civResearch.put(t, civilization.civResearch.get(t) + civilization.currentlyResearchingTechonologys.get(t).getSkill() * delta);
                }
            }

            //Process science labs
            for (Integer scienceLab : civilization.scienceLabs) {
                //TODO...
//                HashMap<String, Integer> science = scienceLab.scienceProvided();
//                for (Map.Entry<String, Integer> entry : science.entrySet()) {
//                    String key = entry.getKey();
//                    Integer val = entry.getValue();
//
//                    Field f = civilization.fields.findNode(key);
//                    f.incrementLevel(val);
//                }
            }
        }
    }

    private void processResources() {
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);

            for (Map.Entry<Integer, Double> entry : civilization.resourceList.entrySet()) {
                civilization.resourceList.put(entry.getKey(), 0d);
            }
            //Process resources
            for (ResourceStockpile s : civilization.getResourceStorages()) {
                //Get resource types allowed, and do stuff
                //c.resourceList.
                
                for (Integer type : s.storedTypes()) {
                    //add to index
                    if (!civilization.resourceList.containsKey(type)) {
                        civilization.resourceList.put(type, 0d);
                    }
                    Double amountToAdd = (civilization.resourceList.get(type) + s.getResourceAmount(type));
                    civilization.resourceList.put(type, amountToAdd);
                }
            }
        }
    }

    private CityType classifyDistrict(City dis) {
        //Get the type of areas
        HashMap<AreaClassification, Integer> areaType = new HashMap<>();
        for (Integer areaId : dis.areas) {
            Area a = gameState.getObject(areaId, Area.class);
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
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);

            //Process ship actions
            for (Integer shipId : civilization.spaceships) {
                Ship ship = gameState.getObject(shipId, Ship.class);
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
            StarSystem system = universe.getStarSystemObject(i);

            system.setPoint(system.getOrbit().toSpacePoint());

            for (int k = 0; k < system.getBodyCount(); k++) {
                Body body = system.getBodyObject(k);
                body.setPoint(body.getOrbit().toSpacePoint());
            }
        }
        long end = System.currentTimeMillis();
        //System.out.println((end - start));
    }

    private void createPeople() {
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civ = gameState.getCivilizationObject(i);

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
                Scientist nerd = new Scientist(gameState, person, age);
                nerd.setSkill((int) (Math.random() * 5) + 1);
                nerd.traits.add(getRandomPersonalityTrait());
                nerd.setPosition(civ.getCapitalCity());

                civ.unrecruitedPeople.add(nerd.getId());
                //Generate personality
            }
            //Admins
            peopleCount = (int) (Math.random() * 5) + 5;

            for (int peep = 0; peep < peopleCount; peep++) {
                int age = (int) (Math.random() * 40) + 20;
                String person = "name";
                person = gen.getName((int) Math.round(Math.random()));
                Administrator dude = new Administrator(gameState, person, age);
                dude.traits.add(getRandomPersonalityTrait());
                dude.setPosition(civ.getCapitalCity());

                //nerd.setSkill((int) (Math.random() * 5) + 1);
                civ.unrecruitedPeople.add(dude.getId());
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