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
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.ConstructingBuilding;
import ConquerSpace.game.buildings.City;
import ConquerSpace.game.buildings.PopulationStorage;
import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.buildings.area.ResearchArea;
import ConquerSpace.game.buildings.area.industrial.Factory;
import ConquerSpace.game.buildings.area.infrastructure.PowerPlantArea;
import ConquerSpace.game.events.Event;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.population.jobs.Employer;
import ConquerSpace.game.population.jobs.Job;
import ConquerSpace.game.population.jobs.JobRank;
import ConquerSpace.game.population.jobs.JobType;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.science.Field;
import ConquerSpace.game.science.ScienceLab;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.vision.VisionPoint;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.ProductionProcess;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.bodies.ControlTypes;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Body;
import ConquerSpace.game.universe.bodies.Star;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.gui.renderers.RendererMath;
import ConquerSpace.util.logging.CQSPLogger;
import ConquerSpace.util.Warning;
import ConquerSpace.util.names.NameGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Logger;

/**
 * This actually controls the game. If you take out this class, too bad...
 *
 * @author Zyun
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
        
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Globals.universe.getCivilization(i).calculateTechLevel();
        }
        //Do tech...
        //Increment tech
        processResearch();

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
    
    public void updateUniverse(Universe u, StarDate date, long delta) {
        //Loop through star systems
        for (int i = 0; i < u.getStarSystemCount(); i++) {
            updateStarSystem(u.getStarSystem(i), date, delta);
        }
    }
    
    public void updateStarSystem(StarSystem sys, StarDate date, long delta) {
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
    
    public void processPlanet(Planet p, StarDate date, long delta) {
        if (p.isHabitated()) {
            processCities(p, date, delta);
            
            processBuildings(p, date, delta);
            
            processPopulation(p, date);
        }
        //Process locallife
        for (LocalLife localLife : p.localLife) {
            int biomass = localLife.getBiomass();
            float breedingRate = localLife.getSpecies().getBaseBreedingRate();
            localLife.setBiomass((int) (breedingRate * biomass) + biomass);
        }
    }

    /**
     * Sets the jobs of people...
     *
     * @param p
     * @param date
     * @param delta
     */
    public void processBuildings(Planet p, StarDate date, long delta) {
        //Process buildings, and jobs
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
            Building building = entry.getValue();
            //Process
            if (building instanceof ConstructingBuilding) {
                ConstructingBuilding build = (ConstructingBuilding) building;
                if (build.getLength() > 0) {
                    build.decrementLength((int) delta);
                } else {
                    //Done construction!
                    //Check for cities near it
                    if (build.getToBuild() == null) {
                        p.buildings.remove(key);
                    } else {
                        p.addBuildingToPlanet(key, building);

                        //Alert builder
                        build.builder.passEvent(new Event("Building " + build.getToBuild().getType() + " finished"));
                        p.buildings.put(key, build.getToBuild());
                    }
                }
            } else {
                building.tick(date, delta);
            }
            
            if (building instanceof PopulationStorage) {
                int energy = ((PopulationStorage) building).getPopulationArrayList().size() * 20;
                building.setEnergyUsage(energy);
            }
        }
    }
    
    private void processAreaJobs(City c, Building b, Area a, StarDate date) {
        if (a instanceof PowerPlantArea) {
            PowerPlantArea powerPlant = (PowerPlantArea) a;
            Job job = new Job(JobType.PowerPlantTechnician);
            
            job.setJobRank(JobRank.Low);
            job.setWorkingFor(a);
            //Set pay
            job.setPay(1);
            job.setEmployer(b.getOwner());
            
            job.resources.put(powerPlant.getUsedResource(), Double.valueOf(-powerPlant.getMaxVolume()));
            c.jobs.add(job);
        } else if (a instanceof Factory) {
            Job job = new Job(JobType.FactoryWorker);

            //Process resources used
            ProductionProcess process = ((Factory) a).getProcess();
            for (Map.Entry<Good, Integer> entry : process.input.entrySet()) {
                Good key = entry.getKey();
                Integer val = entry.getValue();
                
                job.resources.putIfAbsent(key, Double.valueOf(-val));
            }
            
            for (Map.Entry<Good, Integer> entry : process.output.entrySet()) {
                Good key = entry.getKey();
                Integer val = entry.getValue();
                
                job.resources.putIfAbsent(key, Double.valueOf(val));
            }
            
            job.setJobRank(JobRank.Low);
            job.setWorkingFor(a);
            //Set pay
            job.setPay(1);
            job.setEmployer(b.getOwner());
            
            c.jobs.add(job);
        } else if (a instanceof ResearchArea) {
            Job researchJob = new Job(JobType.Researcher);
            
            researchJob.setJobRank(JobRank.Medium);
            researchJob.setWorkingFor(a);
            //Set pay
            researchJob.setPay(1);
            researchJob.setEmployer(b.getOwner());
            c.jobs.add(researchJob);
            
            Job educationJob = new Job(JobType.Educator); //Improves education, and in the long run, improves science gain
            educationJob.setJobRank(JobRank.Medium);
            educationJob.setWorkingFor(a);
            //Set pay
            educationJob.setPay(1);
            educationJob.setEmployer(b.getOwner());
            c.jobs.add(educationJob);
        }
    }
    
    public void processStar(Star s, StarDate date) {
        
    }
    
    public void storeResource(Good resourceType, Double amount, int owner, UniversePath from) {
        //Get closest resources storage
        //No matter their alleigence, they will store resource to the closest resource storage...
        //Search planet, because we don't have space storages for now.
        Body body = universe.getSpaceObject(from);
        if (body instanceof Planet) {
            Planet planet = (Planet) body;
            for (Map.Entry<GeographicPoint, Building> entry : planet.buildings.entrySet()) {
                Building val = entry.getValue();
                if (val.canStore(resourceType)) {
                    val.addResource(resourceType, amount);
                    break;
                }
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
            for (Map.Entry<GeographicPoint, Building> entry : planet.buildings.entrySet()) {
                Building val = entry.getValue();
                //Get by positon...
                //For now, we process only if it is on the planet or not.
                if (val.canStore(resourceType) && val.removeResource(resourceType, amount)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void processCities(Planet p, StarDate date, long delta) {
        for (City c : p.cities) {
            //Population growth
            c.incrementPopulation(date, delta);
            
            createCityJobs(c, date);
            //Assign jobs
            assignJobs(c, date);
        }
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
    
    @Warning("Warn")
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
    
    public void createCityJobs(City c, StarDate date) {
        //Add the jobs...
        //Assign everyone an empty job...
        float upkeepAmount = 0;
        c.jobs.clear();
        for (Building building : c.buildings) {
            //Get the building type
            Job[] jobs = building.jobsNeeded();
            Collections.addAll(c.jobs, jobs);

            //Get number of people and add support jobs
            if (building instanceof PopulationStorage) {
                PopulationStorage storage = (PopulationStorage) building;
                ArrayList<PopulationUnit> population = storage.getPopulationArrayList();
                for (PopulationUnit unit : population) {
                    upkeepAmount += unit.getSpecies().getUpkeep();
                }
            }

            //Sort through areas
            for (Area a : building.areas) {
                processAreaJobs(c, building, a, date);
            }
        }
        //Set the upkeep
        int amount = Math.round(upkeepAmount);
        for (int i = 0; i < amount; i++) {
            Job job = new Job(JobType.PopUpkeepWorker);
            job.setJobRank(JobRank.Medium);
            c.jobs.add(job);
        }
    }
    
    public void processPopulation(Planet p, StarDate date) {
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            Building value = entry.getValue();
            if (value instanceof PopulationStorage) {
                PopulationStorage storage = (PopulationStorage) value;
                for (PopulationUnit unit : storage.getPopulationArrayList()) {
                    //Add normal pop upkeep
                    //All people consume food
                    processPopUnit(unit);

                    //Do subtractions here in the future, like happiness, and etc.
                    //Process affect on building that it is working for
                    Workable workingFor = unit.getJob().getWorkingFor();
                    if (workingFor != null) {
                        workingFor.processJob(unit.getJob());
                    }

                    //Process job resources
                    for (Good r : unit.getJob().resources.keySet()) {
                        if (r != null) {
                            storeResource(r, unit.getJob().resources.get(r), p.getOwnerID(), p.getUniversePath());
                        }
                    }
                }
            }
        }
    }
    
    public void assignJobs(City c, StarDate date) {
        //Process through all the population units
        int i = 0;
        for (Building b : c.buildings) {
            if (b instanceof PopulationStorage) {
                PopulationStorage storage = (PopulationStorage) b;
                for (PopulationUnit unit : storage.getPopulationArrayList()) {
                    if (i < c.jobs.size()) {
                        //Set pop job
                        unit.setJob(c.jobs.get(i));
                    } else {
                        unit.getJob().setJobType(JobType.Jobless);
                        unit.getJob().setJobRank(JobRank.Low);
                    }
                    i++;
                }
            }
        }
    }
    
    public void processPopUnit(PopulationUnit unit) {
        Job popJob = unit.getJob();
        popJob.setPay(100);
        //TODO do food consumption

        Employer employer = popJob.getEmployer();
        if (employer != null) {
            employer.changeMoney(-popJob.getPay());
        }
        //food -= unit.getSpecies().getFoodPerMonth();
        //popJob.resources.put(GameController.foodResource, food);
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
}
