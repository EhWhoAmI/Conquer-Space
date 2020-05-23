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
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.actions.ShipAction;
import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.civilization.vision.VisionPoint;
import ConquerSpace.game.civilization.vision.VisionTypes;
import ConquerSpace.game.districts.City;
import ConquerSpace.game.districts.area.Area;
import ConquerSpace.game.districts.area.FarmFieldArea;
import ConquerSpace.game.districts.area.ManufacturerArea;
import ConquerSpace.game.districts.area.MineArea;
import ConquerSpace.game.districts.area.PowerPlantArea;
import ConquerSpace.game.districts.area.ResearchArea;
import ConquerSpace.game.districts.area.TimedManufacturerArea;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.science.Field;
import ConquerSpace.game.science.ScienceLab;
import ConquerSpace.game.science.tech.Technologies;
import ConquerSpace.game.science.tech.Technology;
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.bodies.Body;
import ConquerSpace.game.universe.bodies.ControlTypes;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Star;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.ProductionProcess;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import ConquerSpace.gui.renderers.RendererMath;
import ConquerSpace.util.logging.CQSPLogger;
import ConquerSpace.util.names.NameGenerator;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Logger;

/**
 * This controls the game.
 *
 * @author EhWhoAmI
 */
public class GameUpdater {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameUpdater.class.getName());

    private Universe universe;

    private StarDate starDate;

    private final int GameRefreshRate;

    private long updateTime;

    private GameIndexer indexer;
    private PeopleProcessor peopleProcessor;

    public GameUpdater(Universe u, StarDate s, int GameRefreshRate) {
        universe = u;
        starDate = s;
        indexer = new GameIndexer(u);
        peopleProcessor = new PeopleProcessor(Globals.universe, Globals.date);
        this.GameRefreshRate = GameRefreshRate;
    }

    //Process ingame tick.
    public synchronized void tick() {
        //DO ticks
        starDate.increment(1);
        calculateVision();
        updateObjectPositions();

        //Move ships
        moveShips();

        //Check for month increase
        if (Globals.date.bigint % GameRefreshRate == 0) {
            updateGame();
            for (int i = 0; i < universe.getCivilizationCount(); i++) {
                indexer.index(universe.getCivilization(i));
            }
        }
        //Process people and generate every 1000 ticks, which is about every 41 days
        if (Globals.date.bigint % (GameRefreshRate * 2) == 0) {
            createPeople();
        }
    }

    public synchronized void updateGame() {
        long start = System.currentTimeMillis();
        updateUniverse(Globals.universe, Globals.date, GameRefreshRate);

        //Increment tech
        processResearch();
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Globals.universe.getCivilization(i).calculateTechLevel();
        }

        //Increment resources
        processResources();

        peopleProcessor.processPeople();

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

        //Loop through all the vision points in the universe
        for (int civ = 0; civ < universe.getCivilizationCount(); civ++) {
            Civilization civil = universe.getCivilization(civ);
            for (VisionPoint pt : civil.visionPoints) {
                int range = pt.getRange();
                //Distance between all star systems...
                ConquerSpace.game.universe.Point pos = pt.getPosition();
                for (int g = 0; g < universe.getStarSystemCount(); g++) {
                    //Difference between points...
                    double dist = Math.hypot(pos.getY() - universe.getStarSystem(g).getY(),
                            pos.getX() - universe.getStarSystem(g).getX());
                    if (dist < range) {
                        //Its in!
                        int amount = ((int) ((1 - (dist / (double) range)) * 100));
                        //int previous = universe.getCivilization(pt.getCivilization().vision.get(universe.getStarSystem(g).getUniversePath()));
                        universe.getCivilization(pt.getCivilization()).vision.put(universe.getStarSystem(g).getUniversePath(),
                                (amount > 100) ? 100 : (amount));
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

    public void updateStarSystem(StarSystem sys, StarDate date, int delta) {
        //Maybe later the objects in space.
        for (int i = 0; i < sys.bodies.size(); i++) {
            Body body = sys.bodies.get(i);
            if (body instanceof Planet) {
                processPlanet((Planet) body, date, delta);
            } else if (body instanceof Star) {
                processStar((Star) body, date);
            }
        }
    }

    public void processPlanet(Planet p, StarDate date, int delta) {
        if (p.isHabitated()) {
            processCities(p, date, delta);

            processPopulation(p, date);
        }
        processLocalLife(p, date, delta);
    }

    public void processLocalLife(Planet p, StarDate date, int delta) {
        //Process locallife
        for (LocalLife localLife : p.localLife) {
            int biomass = localLife.getBiomass();
            float breedingRate = localLife.getSpecies().getBaseBreedingRate();
            localLife.setBiomass((int) (breedingRate * biomass) + biomass);
        }
    }

    /**
     * Increments population of city, creates city jobs, and assigns them.
     *
     * @param p
     * @param date
     * @param delta
     */
    public void processCities(Planet p, StarDate date, int delta) {
        for (City c : p.cities) {
            //Population growth
            c.incrementPopulation(date, delta);

            calculateCityJobs(c, date, delta);

            for (Area a : c.areas) {
                processArea(c, a, date, delta);
            }
        }
    }

    /**
     * See the amount of jobs that are filled
     *
     * @param c
     * @param date
     */
    public void calculateCityJobs(City c, StarDate date, int delta) {
        long maxJobsProviding = 0;
        long necessaryJobsProviding = 0;
        long size = c.population.getPopulationSize();
        for (Area a : c.areas) {
            necessaryJobsProviding += a.operatingJobsNeeded();
            maxJobsProviding += a.getMaxJobsProvided();
        }
        if (necessaryJobsProviding < size) {
            //All the jobs ok
            for (Area a : c.areas) {
                a.setCurrentlyManningJobs(a.getMaxJobsProvided());
            }
        }
    }

    public void processArea(City c, Area a, StarDate date, int delta) {
        if (a instanceof FarmFieldArea) {
            FarmFieldArea area = (FarmFieldArea) a;
            int removed = area.tick(delta);
            if (removed > 0 && area.operatingJobsNeeded() < area.getCurrentlyManningJobs()) {
                storeResource(area.getGrown().getFoodGood(), 10d * removed, 0, c);
                area.grow();
            }
        } else if (a instanceof TimedManufacturerArea) {
            //Subtract time
            TimedManufacturerArea area = (TimedManufacturerArea) a;
            int removed = area.tick(delta);

            if (a.operatingJobsNeeded() < a.getCurrentlyManningJobs()) {
                for (Map.Entry<Good, Double> entry : area.getProcess().output.entrySet()) {
                    Good key = entry.getKey();
                    Double val = entry.getValue();

                    storeResource(key, val * removed, 0, c);
                }
            }
        }
        if (a instanceof PowerPlantArea) {
            PowerPlantArea powerPlant = (PowerPlantArea) a;
            if (a.operatingJobsNeeded() < a.getCurrentlyManningJobs()) {
                storeResource(powerPlant.getUsedResource(), Double.valueOf(-powerPlant.getMaxVolume()), 0, c);
            }
        } else if (a instanceof ManufacturerArea) {
            //Process resources used
            ProductionProcess process = ((ManufacturerArea) a).getProcess();
            if (a.operatingJobsNeeded() < a.getCurrentlyManningJobs()) {
                for (Map.Entry<Good, Double> entry : process.input.entrySet()) {
                    Good key = entry.getKey();
                    Double val = entry.getValue();

                    storeResource(key, -val * delta, 0, c);
                }

                for (Map.Entry<Good, Double> entry : process.output.entrySet()) {
                    Good key = entry.getKey();
                    Double val = entry.getValue();

                    storeResource(key, val * delta, 0, c);
                }
            }
        } else if (a instanceof ResearchArea) {

        } else if (a instanceof MineArea) {
            MineArea area = (MineArea) a;
            if (a.operatingJobsNeeded() < a.getCurrentlyManningJobs()) {
                for (Map.Entry<Good, Double> entry : area.getNecessaryGoods().entrySet()) {
                    Good key = entry.getKey();
                    Double val = entry.getValue();

                    storeResource(key, -val * delta, 0, c);
                }
                storeResource(area.getResourceMined(), Double.valueOf(area.getProductivity() * delta), 0, c);
            }

        }
    }

    public void processStar(Star s, StarDate date) {

    }

    public void processResearch() {
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Civilization c = Globals.universe.getCivilization(i);

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
                    c.civResearch.put(t, c.civResearch.get(t) + c.currentlyResearchingTechonologys.get(t).getSkill() * GameRefreshRate);
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

    public void processResources() {
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Civilization c = Globals.universe.getCivilization(i);
            for (Map.Entry<Good, Double> entry : c.resourceList.entrySet()) {
                c.resourceList.put(entry.getKey(), 0d);
            }
            //Process resources
            for (ResourceStockpile s : c.resourceStorages) {
                //Get resource types allowed, and do stuff
                //c.resourceList.
                for (Good type : s.storedTypes()) {
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

    public void moveShips() {
        for (int sys = 0; sys < Globals.universe.getCivilizationCount(); sys++) {
            Civilization c = Globals.universe.getCivilization(sys);
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
            RendererMath.Point pt
                    = RendererMath.polarCoordToCartesianCoord((double) system.getGalaticLocation().getDistance(),
                            system.getGalaticLocation().getDegrees(), new RendererMath.Point(0, 0), 1);

            system.setX(pt.x);
            system.setY(pt.y);
            for (int k = 0; k < system.bodies.size(); k++) {
                Body body = system.bodies.get(k);
                body.setPoint(body.getOrbit().toSpacePoint());
            }
        }
        long end = System.currentTimeMillis();
        //System.out.println((end - start));
    }

    public void processPopulation(Planet p, StarDate date) {
        //Index panet population
        long total = 0;
        for (City c : p.cities) {
            total += c.population.getPopulationSize();
        }
        p.population = total;

        for (Map.Entry<GeographicPoint, City> entry : p.cityDistributions.entrySet()) {
            City city = entry.getValue();

            //Process population upkeep
        }
    }

    public void createPeople() {
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Civilization c = Globals.universe.getCivilization(i);
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

    private void supplyLineWalker() {

    }

    /**
     * Stores goods in the closest resource storage from <code>from</code>
     *
     * @param resourceType
     * @param amount
     * @param owner
     * @param from
     */
    public void storeResource(Good resourceType, Double amount, int owner, UniversePath from) {
        //Get closest resources storage
        //No matter their alleigence, they will store resource to the closest resource storage...
        //Search planet, because we don't have space storages for now.
        if (resourceType != null) {
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
    
    public void storeResource(Good resourceType, Double amount, int owner, City from) {
        if (resourceType != null) {
            if(from.canStore(resourceType)) {
                //Store resource
                from.addResource(resourceType, amount);
            } else {
                storeResource(resourceType, amount, owner, from.getUniversePath());
            }
        }
    }

    public boolean removeResource(Good resourceType, Double amount, int owner, UniversePath from) {
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
}
