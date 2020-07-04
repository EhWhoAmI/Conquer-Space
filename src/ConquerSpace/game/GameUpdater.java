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

import ConquerSpace.Globals;
import ConquerSpace.game.actions.Actions;
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
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.population.PopulationSegment;
import ConquerSpace.game.science.Field;
import ConquerSpace.game.science.ScienceLab;
import ConquerSpace.game.science.tech.Technologies;
import ConquerSpace.game.science.tech.Technology;
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.ships.launch.SpacePortLaunchPad;
import ConquerSpace.game.universe.GeographicPoint;
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
import ConquerSpace.util.logging.CQSPLogger;
import ConquerSpace.util.names.NameGenerator;
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

    private Universe universe;

    private StarDate starDate;

    private final int GameRefreshRate;

    private long updateTime;
    private int ledgerClearTime;

    private GameIndexer indexer;
    private PeopleProcessor peopleProcessor;

    public GameUpdater(Universe u, StarDate s, int GameRefreshRate) {
        universe = u;
        starDate = s;
        indexer = new GameIndexer(u);
        peopleProcessor = new PeopleProcessor(GameController.universe, GameController.date);
        this.GameRefreshRate = GameRefreshRate;
        ledgerClearTime = GameRefreshRate * 10;
    }

    //Process ingame tick.
    @Override
    public synchronized void tick(int tickIncrement) {
        //DO ticks
        starDate.increment(tickIncrement);

        updateObjectPositions();

        //Move ships
        moveShips();

        calculateControl();
        calculateVision();

        //Check for month increase            
        if (GameController.date.bigint % GameRefreshRate == 1) {
            LOGGER.trace("Refreshing all the game objects");
            updateGame();
            for (int i = 0; i < universe.getCivilizationCount(); i++) {
                indexer.index(universe.getCivilization(i));
            }
        }
        //Process people and generate every 1000 ticks, which is about every 41 days
        if (GameController.date.bigint % (GameRefreshRate * 2) == 1) {
            createPeople();
        }
    }

    public synchronized void updateGame() {
        long start = System.currentTimeMillis();
        updateUniverse(GameController.universe, GameController.date, GameRefreshRate);

        //Increment tech
        processResearch(GameRefreshRate);
        for (int i = 0; i < GameController.universe.getCivilizationCount(); i++) {
            GameController.universe.getCivilization(i).calculateTechLevel();
        }

        //Increment resources
        processResources();

        peopleProcessor.processPeople(GameRefreshRate);

        long end = System.currentTimeMillis();

        updateTime = (end - start);
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

        int looping = 0;
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
                        looping++;
                    }
                }
            }
        }
    }

    public void updateUniverse(Universe u, StarDate date, int delta) {
        //Loop through star systems
        for (int i = 0; i < u.getStarSystemCount(); i++) {
            updateStarSystem(u.getStarSystem(i), date, delta);
        }
    }

    private void updateStarSystem(StarSystem sys, StarDate date, int delta) {
        //Maybe later the objects in space.
        for (int i = 0; i < sys.bodies.size(); i++) {
            Body body = sys.bodies.get(i);
            if (body instanceof Planet) {
                processPlanet((Planet) body, date, delta);
            } else if (body instanceof Star) {
                processStar((Star) body, date, delta);
            }
        }
    }

    private void processPlanet(Planet p, StarDate date, int delta) {
        if (p.isHabitated()) {
            //So we know how many people there are
            doPlanetCensus(p);

            processCities(p, date, delta);
        }
        processLocalLife(p, date, delta);
    }

    /**
     * Increments population of city, creates city jobs, and assigns them.
     *
     * @param p
     * @param date
     * @param delta
     */
    private void processCities(Planet p, StarDate date, int delta) {
        for (City c : p.cities) {
            //Clear ledger
            c.resourceLedger.clear();

            //Process city demands
            //Create orders from cities that have stuff
            //Clear demands
            c.resourceDemands.clear();

            processPopulation(c, date, delta);

            //Population growth
            calculateCityJobs(c, date, delta);

            for (Area a : c.areas) {
                processArea(p, c, a, date, delta);
            }

            //Replace constructing areas
            Iterator<Area> areaIterator = c.areas.iterator();
            ArrayList<Area> areasToAdd = new ArrayList<>();
            while (areaIterator.hasNext()) {
                Area a = areaIterator.next();
                if (a instanceof ConstructingArea && ((ConstructingArea) a).getTicksLeft() <= 0) {
                    areasToAdd.add(((ConstructingArea) a).getToBuild());
                    areaIterator.remove();
                }
            }
            c.areas.addAll(areasToAdd);

            CityType type = classifyDistrict(c);
            c.setCityType(type);
        }

        distributeResources(p);
    }

    /**
     * See the amount of jobs that are filled
     *
     * @param c
     * @param date
     */
    private void calculateCityJobs(City c, StarDate date, int delta) {
        long maxJobsProviding = 0;
        long necessaryJobsProviding = 0;
        long size = c.population.getPopulationSize();

        //Sort them out based off piority
        Collections.sort(c.areas);

        for (Area a : c.areas) {
            necessaryJobsProviding += a.operatingJobsNeeded();
            maxJobsProviding += a.getMaxJobsProvided();
        }

        if (maxJobsProviding < size) {
            //Fill necessary jobs if there are not enough people to get the max amount of people
            for (Area a : c.areas) {
                a.setCurrentlyManningJobs(a.getMaxJobsProvided());
            }
        } else if (necessaryJobsProviding < size) {
            //Fill all the jobs needed to operate

            for (Area a : c.areas) {
                size -= a.operatingJobsNeeded();
                a.setCurrentlyManningJobs(a.operatingJobsNeeded());
            }
            //Go through again, and add the jobs
            for (Area a : c.areas) {
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
            for (Area a : c.areas) {
                int jobsToAdd = a.operatingJobsNeeded();
                if ((size - jobsToAdd) > 0) {
                    size -= jobsToAdd;
                    a.setCurrentlyManningJobs(jobsToAdd);
                }
            }
        }
    }

    private void processPopulation(City city, StarDate date, int delta) {
        //Process population upkeep
        for (PopulationSegment seg : city.population.populations) {
            //Request resources
            long amount = (seg.size / 1000);
            double consume = ((double) amount) * 0.5d;

            double foodAmount = 0;
            if (city.resources.containsKey(universe.species.get(seg.species).food)) {
                foodAmount = city.getResourceAmount(universe.species.get(seg.species).food);
            }

            //Request food
            //Append resources
            if (city.resourceDemands.containsKey(universe.species.get(seg.species).food)) {
                city.resourceDemands.put(universe.species.get(seg.species).food, city.resourceDemands.get(universe.species.get(seg.species).food) + consume);
            } else {
                city.resourceDemands.put(universe.species.get(seg.species).food, consume);
            }

            boolean success = removeResource(universe.species.get(seg.species).food, consume, 0, city);
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
            double fraction = ((double) delta) / 10000d;

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

    private void distributeResources(Planet p) {
        //Find stuff
        for (City c : p.cities) {
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
            for (City cit : p.cities) {
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
                                    sendResources(key, toSpendAmount, 0, c, cit);
                                    //Subtract resources
                                    resourcesToSpend.put(key, 0d);
                                } else {
                                    sendResources(key, val, 0, c, cit);
                                    resourcesToSpend.put(key, (toSpendAmount - val));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void processArea(Planet p, City c, Area a, StarDate date, int delta) {
        if (a instanceof FarmFieldArea) {
            FarmFieldArea area = (FarmFieldArea) a;
            int removed = area.tick(delta);
            if (removed > 0 && area.operatingJobsNeeded() < area.getCurrentlyManningJobs()) {
                //Calculate percentage
                storeResource(area.getGrown().getFoodGood(), (removed * (double) area.getFieldSize()), 0, c);

                area.grow();
            }
        } else if (a instanceof TimedManufacturerArea) {
            //Subtract time
            TimedManufacturerArea area = (TimedManufacturerArea) a;
            int removed = area.tick(delta);

            if (a.operatingJobsNeeded() < a.getCurrentlyManningJobs()) {
                for (Map.Entry<Integer, Double> entry : area.getProcess().output.entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();

                    //Get percentage
                    storeResource(key, val * removed, 0, c);
                }
            }
        }
        if (a instanceof PowerPlantArea) {
            PowerPlantArea powerPlant = (PowerPlantArea) a;
            if (a.operatingJobsNeeded() < a.getCurrentlyManningJobs()) {
                removeResource(powerPlant.getUsedResource(), Double.valueOf(powerPlant.getMaxVolume()), 0, c);
            }
        } else if (a instanceof ManufacturerArea) {
            //Process resources used
            ProductionProcess process = ((ManufacturerArea) a).getProcess();
            if (a.operatingJobsNeeded() < a.getCurrentlyManningJobs()) {
                //Query resources
                for (Map.Entry<Integer, Double> entry : process.input.entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();
                    Double amount = c.resources.get(key);
                    removeResource(key, val * delta, 0, c);
                    c.resourceDemands.addValue(key, val);
                }

                for (Map.Entry<Integer, Double> entry : process.output.entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();

                    storeResource(key, val * delta, 0, c);
                }
            }
        } else if (a instanceof ResearchArea) {

        } else if (a instanceof MineArea) {
            MineArea area = (MineArea) a;
            if (a.operatingJobsNeeded() < a.getCurrentlyManningJobs()) {
                for (Map.Entry<Integer, Double> entry : area.getNecessaryGoods().entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();
                    removeResource(key, val * delta, 0, c);
                }

                double multiplier = getMultiplier(a);

                storeResource(area.getResourceMinedId(), Double.valueOf(area.getProductivity() * delta) * multiplier, 0, c);
            }
        } else if (a instanceof SpacePortArea) {
            SpacePortArea area = (SpacePortArea) a;
            for (int i = 0; i < area.launchPads.size(); i++) {
                SpacePortLaunchPad get = area.launchPads.get(i);
                get.ticks -= delta;
                if (get.ticks <= 0) {
                    //Launch!
                    //Get the owner somehow...
                    Actions.launchLaunchable(get.getLaunching(), p);
                }
            }
        } else if (a instanceof ObservatoryArea) {
            ObservatoryArea area = (ObservatoryArea) a;
            //Just slightly inelagant code to get the vision points
            if (!GameController.universe.civs.get(area.getCivilization()).visionPoints.contains(area)) {
                GameController.universe.civs.get(area.getCivilization()).visionPoints.add(area);
            }
        }

        if (a instanceof ConstructingArea) {
            ConstructingArea area = (ConstructingArea) a;
            area.tickConstruction(delta);
        }
    }

    private void processLocalLife(Planet p, StarDate date, int delta) {
        //Process locallife
        for (LocalLife localLife : p.localLife) {
            int biomass = localLife.getBiomass();
            float breedingRate = localLife.getSpecies().getBaseBreedingRate();
            localLife.setBiomass((int) (breedingRate * biomass) + biomass);
        }
    }

    private void processStar(Star s, StarDate date, int delta) {

    }

    private void processResearch(int delta) {
        for (int i = 0; i < GameController.universe.getCivilizationCount(); i++) {
            Civilization c = GameController.universe.getCivilization(i);

            Iterator<Technology> tech = c.currentlyResearchingTechonologys.keySet().iterator();

            while (tech.hasNext()) {
                Technology t = tech.next();

                if ((Technologies.estFinishTime(t) - c.civResearch.get(t)) <= 0) {
                    //Then tech is finished
                    c.researchTech(t);
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
        for (int i = 0; i < GameController.universe.getCivilizationCount(); i++) {
            Civilization c = GameController.universe.getCivilization(i);
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
        for (int sys = 0; sys < GameController.universe.getCivilizationCount(); sys++) {
            Civilization c = GameController.universe.getCivilization(sys);
            //Process ship actions
            for (Ship ship : c.spaceships) {
                ShipAction sa = ship.getActionAndPopIfDone();

                if (!sa.checkIfDone()) {
                    //System.out.println(sa.getClass());
                    sa.doAction();
                } else {
                    //Next action and init
                    ship.getActionAndPopIfDone().initAction();
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
        for (int i = 0; i < GameController.universe.getCivilizationCount(); i++) {
            Civilization c = GameController.universe.getCivilization(i);
            NameGenerator gen = null;
            try {
                gen = NameGenerator.getNameGenerator("us.names");
            } catch (IOException ex) {
                //Ignore
            }
            //Create 5-10 random scientists
            c.unrecruitedPeople.clear();
            int peopleCount = (int) (Math.random() * 5) + 5;
            for (int peep = 0; peep < peopleCount; peep++) {
                int age = (int) (Math.random() * 40) + 20;
                String person = "name";
                person = gen.getName((int) Math.round(Math.random()));
                Scientist nerd = new Scientist(person, age);
                nerd.setSkill((int) (Math.random() * 5) + 1);
                nerd.traits.add(GameController.personalityTraits.get((int) (GameController.personalityTraits.size() * Math.random())));
                nerd.setPosition(c.getCapitalCity());

                c.unrecruitedPeople.add(nerd);
                //Generate personality
            }
            //Admins
            peopleCount = (int) (Math.random() * 5) + 5;

            for (int peep = 0; peep < peopleCount; peep++) {
                int age = (int) (Math.random() * 40) + 20;
                String person = "name";
                person = gen.getName((int) Math.round(Math.random()));
                Administrator dude = new Administrator(person, age);
                dude.traits.add(GameController.personalityTraits.get((int) (GameController.personalityTraits.size() * Math.random())));
                dude.setPosition(c.getCapitalCity());

                //nerd.setSkill((int) (Math.random() * 5) + 1);
                c.unrecruitedPeople.add(dude);
            }
        }
    }

    /**
     * Stores goods in the closest resource storage from <code>from</code>
     *
     * @param resourceType
     * @param amount
     * @param owner
     * @param from
     */
    private void storeResource(Integer resourceType, Double amount, int owner, UniversePath from) {
        //Get closest resources storage
        //No matter their alleigence, they will store resource to the closest resource storage...
        //Search planet, because we don't have space storages for now.
        if (resourceType != null && amount > 0) {
            Body body = universe.getSpaceObject(from);
            if (body instanceof Planet) {
                Planet planet = (Planet) body;
                for (Map.Entry<GeographicPoint, City> entry : planet.cityDistributions.entrySet()) {
                    City val = entry.getValue();
                    if (val.canStore(resourceType)) {
                        val.addResource(resourceType, amount);
                        break;
                    }
                }
            }
        }
    }

    private void storeResource(Integer resourceType, Double amount, int owner, City from) {
        if (resourceType != null && amount > 0) {
            if (from.canStore(resourceType)) {
                //Store resource
                from.addResource(resourceType, amount);
            } else {
                storeResource(resourceType, amount, owner, from.getUniversePath());
            }
        }
    }

    private boolean removeResource(Integer resourceType, Double amount, int owner, City from) {
        if (resourceType != null && amount != 0) {
            if (from.canStore(resourceType)) {
                //Store resource
                return from.removeResource(resourceType, amount);
            } else {
                removeResource(resourceType, amount, owner, from.getUniversePath());
            }
        }
        return false;
    }
    
    private boolean hasSufficientResources(Integer resourceType, Double amount, City from) {
        return false;
    }

    private boolean removeResource(Integer resourceType, Double amount, int owner, UniversePath from) {
        //Get closest resources storage
        //No matter their alleigence, they will store resource to the closest resource storage...
        //Search planet, because we don't have space storages for now.
        Body body = universe.getSpaceObject(from);
        if (body instanceof Planet) {
            Planet planet = (Planet) body;
            for (Map.Entry<GeographicPoint, City> entry : planet.cityDistributions.entrySet()) {
                City val = entry.getValue();
                //Get by positon...
                //For now, we process only if it is on the planet or not.
                if (val.canStore(resourceType) && val.removeResource(resourceType, amount)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean sendResources(Integer resourceType, Double amount, int owner, City from, City to) {
        if (removeResource(resourceType, amount, owner, from)) {
            storeResource(resourceType, amount, owner, to);
            return true;
        }
        return false;
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
