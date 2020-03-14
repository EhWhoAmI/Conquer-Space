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
import static ConquerSpace.game.GameController.GameRefreshRate;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.actions.ShipAction;
import ConquerSpace.game.buildings.AdministrativeCenter;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.BuildingBuilding;
import ConquerSpace.game.buildings.City;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.buildings.FarmBuilding;
import ConquerSpace.game.buildings.PopulationStorage;
import ConquerSpace.game.buildings.ResourceMinerDistrict;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.buildings.area.industrial.Factory;
import ConquerSpace.game.buildings.area.infrastructure.PowerPlantArea;
import ConquerSpace.game.events.Event;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.jobs.Employer;
import ConquerSpace.game.jobs.Job;
import ConquerSpace.game.jobs.JobRank;
import ConquerSpace.game.jobs.JobType;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.jobs.Workable;
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
import ConquerSpace.game.universe.farm.Crop;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import ConquerSpace.game.universe.bodies.ControlTypes;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.SpaceObject;
import ConquerSpace.game.universe.bodies.Star;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.gui.renderers.RendererMath;
import ConquerSpace.util.logging.CQSPLogger;
import ConquerSpace.util.DistributedRandomNumberGenerator;
import ConquerSpace.util.Warning;
import ConquerSpace.util.names.NameGenerator;
import java.io.IOException;
import java.util.ArrayList;
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

    public GameUpdater(Universe u, StarDate s) {
        universe = u;
        starDate = s;
    }

    public void calculateControl() {
        for (UniversePath p : universe.control.keySet()) {
            SpaceObject spaceObject = universe.getSpaceObject(p);

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
                for (int i = 0; i < starsystem.getPlanetCount(); i++) {
                    Planet planet = starsystem.getPlanet(i);
                    if (owner == -1 && planet.getOwnerID() > -1) {
                        owner = planet.getOwnerID();
                    } else if (owner != planet.getOwnerID() && planet.getOwnerID() != -1) {
                        owner = ControlTypes.DISPUTED;
                    }
                }
                universe.control.put(p, owner);

            }
        }
    }

    public void calculateVision() {
        for (UniversePath p : universe.control.keySet()) {
            //Get the vision, do it...
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

    public void updateUniverse(Universe u, StarDate date) {
        //Loop through star systems
        for (int i = 0; i < u.getStarSystemCount(); i++) {
            updateStarSystem(u.getStarSystem(i), date);
        }
    }

    public void updateStarSystem(StarSystem sys, StarDate date) {
        //Maybe later the objects in space.
        for (int i = 0; i < sys.getPlanetCount(); i++) {
            processPlanet(sys.getPlanet(i), date);
        }

        for (int i = 0; i < sys.getStarCount(); i++) {
            processStar(sys.getStar(i), date);
        }
    }

    public void processPlanet(Planet p, StarDate date) {
        organizePopulation(p);

        processCities(p, date);

        //createPlanetJobs(p, date);
        //assignJobs(p, date);
        processBuildings(p, date);

        processPopulation(p, date);

        //Process locallife
        for (LocalLife localLife : p.localLife) {
            int biomass = localLife.getBiomass();
            float breedingRate = localLife.getSpecies().getBaseBreedingRate();
            localLife.setBiomass((int) (breedingRate * biomass) + biomass);
        }
    }

    public void processStar(Star s, StarDate date) {

    }

    public void storeResource(Good resourceType, int amount, int owner, UniversePath from) {
        //Process
        //Get closest resources storage
        //No matter their alleigence, they will store resource to the closest resource storage...
        //Search planet, because we don't have space storages for now.
        Civilization c = universe.getCivilization(owner);
        for (ResourceStockpile rs : c.resourceStorages) {
            //Get by positon...
            //For now, we process only if it is on the planet or not.

            if (rs.canStore(resourceType)) {
                rs.addResource(resourceType, amount);
                break;
            }
        }
    }

    public boolean removeResource(Good resourceType, int amount, int owner, UniversePath from) {
        //Process
        //Get closest resources storage
        //No matter their alleigence, they will store resource to the closest resource storage...
        //Search planet, because we don't have space storages for now.
        Civilization c = universe.getCivilization(owner);
        for (ResourceStockpile rs : c.resourceStorages) {
            //Get by positon...
            //For now, we process only if it is on the planet or not.
            if (rs.canStore(resourceType) && rs.removeResource(resourceType, amount)) {
                return true;
            }
        }
        return false;
    }

    public void processCities(Planet p, StarDate date) {
        for (City c : p.cities) {
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
        }
    }

    @Warning("Warn")
    public void processResources() {
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Civilization c = Globals.universe.getCivilization(i);
            for (Map.Entry<Good, Integer> entry : c.resourceList.entrySet()) {
                c.resourceList.put(entry.getKey(), 0);
            }
            //Process resources
            for (ResourceStockpile s : c.resourceStorages) {
                //Get resource types allowed, and do stuff
                //c.resourceList.
                for (Good type : s.storedTypes()) {
                    //add to index
                    if (!c.resourceList.containsKey(type)) {
                        c.resourceList.put(type, 0);
                    }
                    int amountToAdd = (c.resourceList.get(type) + s.getResourceAmount(type));
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
            for (int k = 0; k < system.getPlanetCount(); k++) {
                Planet planet = system.getPlanet(k);
                //planet.modDegrees(1f);
                double theta = Math.toRadians(planet.getPlanetDegrees());
                double a = planet.getSemiMajorAxis();
                //Eccentrcity
                double e = planet.getEccentricity();
                double r = (a * (1 - e * e)) / (1 - e * Math.cos(theta - planet.getRotation()));
                RendererMath.Point ppt
                        = RendererMath.polarCoordToCartesianCoord((long) r,
                                planet.getPlanetDegrees(), new RendererMath.Point(0, 0), 1);

                planet.setX(ppt.x);
                planet.setY(ppt.y);
            }
        }
        long end = System.currentTimeMillis();
        //System.out.println((end - start));
    }

    public void createCityJobs(City p, StarDate date) {
        //Add the jobs...
        //Assign everyone an empty job...
        //for(p.)
        float upkeepAmount = 0;
        p.jobs.clear();
        for (Building building : p.buildings) {
            //Get the building type
            //Now get the type of building
            if (building instanceof BuildingBuilding) {
                //Add construction job
                BuildingBuilding constructionWork = (BuildingBuilding) building;
                int scale = constructionWork.getScale();
                for (int i = 0; i < scale; i++) {
                    Job constructionJob = new Job(JobType.Construction);
                    constructionJob.setJobRank(JobRank.Low);
                    constructionJob.setEmployer(constructionWork.getOwner());
                    constructionJob.setWorkingFor(constructionWork);
                    //Set them to use resources for the construction
                    for (Map.Entry<Good, Integer> set : constructionWork.resourcesNeeded.entrySet()) {
                        Good resource = set.getKey();
                        Integer amount = set.getValue();
                        //Add to the job
                        constructionJob.resources.put(resource, -amount);
                    }
                    //Add job to building
                    p.jobs.add(constructionJob);
                }
            } else if (building instanceof CityDistrict) {
                //Get the various jobs in a population storage
                //There are a lot of jobs
                //Check if admin center
                CityDistrict city = (CityDistrict) building;
                if (building instanceof AdministrativeCenter) {
                    //An admin center deals with the planet, maybe the galaxy if necessary
                    //Add jobs that deal with admin
                    AdministrativeCenter center = (AdministrativeCenter) building;
                    Job job = new Job(JobType.Administrator);
                    job.setJobRank(JobRank.High);
                    job.setWorkingFor(center);
                    job.setEmployer(center.getOwner());
                    p.jobs.add(job);
                }
                Job job = new Job(JobType.Infrastructure);
                job.setJobRank(JobRank.Low);
                job.setWorkingFor(city);
                job.setEmployer(building.getOwner());
                p.jobs.add(job);
            } else if (building instanceof FarmBuilding) {
                FarmBuilding farm = (FarmBuilding) building;
                for (int i = 0; i < farm.getManpower(); i++) {
                    Job job = new Job(JobType.Farmer);
                    job.setJobRank(JobRank.Low);
                    job.setWorkingFor(farm);
                    //Set pay
                    job.setPay(1);
                    job.setEmployer(building.getOwner());
                    p.jobs.add(job);
                }
            } else if (building instanceof ResourceMinerDistrict) {
                ResourceMinerDistrict district = (ResourceMinerDistrict) building;
                for (int i = 0; i < district.getScale(); i++) {
                    Job job = new Job(JobType.Miner);
                    job.setJobRank(JobRank.Low);
                    job.setWorkingFor(district);
                    job.setEmployer(building.getOwner());
                    p.jobs.add(job);
                }
            } else if (building instanceof SpacePort) {
                Job job = new Job(JobType.SpacePortEngineer);
                job.setEmployer(building.getOwner());
                job.setJobRank(JobRank.Medium);
                p.jobs.add(job);
            }

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
                processAreas(p, building, a, date);
            }
        }
        //Set the upkeep
        int amount = Math.round(upkeepAmount);
        for (int i = 0; i < amount; i++) {
            Job job = new Job(JobType.PopUpkeepWorker);
            job.setJobRank(JobRank.Medium);
            p.jobs.add(job);
        }
    }

    private void processAreas(City c, Building b, Area a, StarDate date) {
        if (a instanceof PowerPlantArea) {
            PowerPlantArea powerPlant = (PowerPlantArea) a;
            Job job = new Job(JobType.PowerPlantTechnician);

            //job.resources.put(powerPlant.getUsedResource(), -powerPlant.getMaxVolume());
            c.jobs.add(job);
        } else if (a instanceof Factory) {
            Job job = new Job(JobType.FactoryWorker);

            //Process resources used
            ProductionProcess process = ((Factory) a).getProcess();
            for (Map.Entry<Good, Integer> entry : process.input.entrySet()) {
                Good key = entry.getKey();
                Integer val = entry.getValue();

                job.resources.putIfAbsent(key, -val);
            }

            for (Map.Entry<Good, Integer> entry : process.output.entrySet()) {
                Good key = entry.getKey();
                Integer val = entry.getValue();

                job.resources.putIfAbsent(key, val);
            }

            c.jobs.add(job);
        }
    }

    /**
     * Sets the jobs of people...
     *
     * @param p
     * @param date
     */
    public void processBuildings(Planet p, StarDate date) {
        //Process buildings, and jobs
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
            Building building = entry.getValue();
            //Process
            if (building instanceof BuildingBuilding) {
                BuildingBuilding build = (BuildingBuilding) building;
                if (build.getLength() > 0) {
                    build.decrementLength(1);
                } else {
                    //Done!
                    //Replace
                    //Check if can add to city
                    if (build.getToBuild() instanceof PopulationStorage) {
                        //Check for cities near it
                        boolean created = false;
                        cityloop:
                        for (City c : p.cities) {
                            //Check for locations
                            for (Building stor : c.buildings) {
                                //Find the storage
                                GeographicPoint storagePoint = p.buildings
                                        .entrySet()
                                        .stream()
                                        .filter(ent -> stor.equals(ent.getValue()))
                                        .map(Map.Entry::getKey).findFirst().orElse(null);
                                if (storagePoint != null) {
                                    //Check if next to city point
                                    if ((storagePoint.getX() + 1 == key.getX()
                                            || storagePoint.getX() - 1 == key.getX())
                                            && (storagePoint.getY() + 1 == key.getY()
                                            || storagePoint.getY() - 1 == key.getY())) {
                                        //Add to city
                                        c.buildings.add(build.getToBuild());
                                        created = true;
                                        break cityloop;
                                    }
                                }
                            }
                        }
                        //Create city
                        if (!created) {
                            City city = new City(p.getUniversePath());
                            city.setName("Another City");
                            //Set city
                            LOGGER.trace("City created");
                            build.getToBuild().setCity(city);
                        }
                    }
                    //Alert builder
                    build.builder.passEvent(new Event("Building " + build.getToBuild().getType() + " finished"));
                    p.buildings.put(key, build.getToBuild());
                }
            } else if (building instanceof AdministrativeCenter) {

            } else if (building instanceof SpacePort) {
                //Process
                SpacePort build = (SpacePort) building;
                //Iterate through launchpads and process
                for (SpacePortLaunchPad splp : build.launchPads) {
                    splp.ticks += GameController.GameRefreshRate;
                    //Get when to launch...
                }
            } else if (building instanceof ResourceStockpile) {
                //Process...
                ResourceStockpile stockpile = (ResourceStockpile) building;
            } else if (building instanceof ResourceMinerDistrict) {

            } else if (building instanceof FarmBuilding) {
                //Get the resources
                FarmBuilding farmBuilding = (FarmBuilding) building;
                //Calculate productivity
                farmBuilding.setHarvestersNeeded(0);

                int yield = 0;
                for (Crop c : farmBuilding.crops) {
                    c.subtractTime();
                    if (c.getTimeLeft() <= 0) {
                        //Prepare crop for harvesting
                        farmBuilding.harvestable.add(c);

                        //Check for harvesters
                        farmBuilding.setHarvestersNeeded(farmBuilding.getHarvestersNeeded() + 1);
                    }
                }
                farmBuilding.setAmountFarmed(yield);

                //System.out.println("hh" + farmBuilding.getProductivity());
                //System.out.println((resources.get(GameController.foodResource) + farmBuilding.getProductivity()));
            } else if (building instanceof CityDistrict) {
                //Process the various jobs and stuff
                //Get i-hub
            }

            if (building instanceof PopulationStorage) {
                int energy = ((PopulationStorage) building).getPopulationArrayList().size() * 20;
                building.setEnergyUsage(energy);
            }
        }

        //Process the toll on the places...
        for (PopulationUnit unit : p.population) {
            Workable workingFor = unit.getJob().getWorkingFor();
            if (workingFor != null) {
                workingFor.processJob(unit.getJob());
            }
        }
    }

    public void processPopulation(Planet p, StarDate date) {
        //Process population
        for (City city : p.cities) {
            incrementCityPopulation(city);
        }

        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            Building value = entry.getValue();
            if (value instanceof PopulationStorage) {
                PopulationStorage storage = (PopulationStorage) value;
                for (PopulationUnit unit : storage.getPopulationArrayList()) {
                    //Add normal pop upkeep
                    //All people consume food
                    processPopUnit(unit);

                    //Population increment
                    //Fraction it so it does not accelerate at a crazy rate
                    //Do subtractions here in the future, like happiness, and etc.
                    //Process job
                    for (Good r : unit.getJob().resources.keySet()) {
                        storeResource(r, unit.getJob().resources.get(r), p.getOwnerID(), p.getUniversePath());
                    }
                }
            }
        }
    }

    public void assignJobs(City p, StarDate date) {
        //Process through all the population units
        int i = 0;
        for (Building b : p.buildings) {
            if (b instanceof PopulationStorage) {
                PopulationStorage storage = (PopulationStorage) b;
                for (PopulationUnit unit : storage.getPopulationArrayList()) {
                    if (i < p.jobs.size()) {
                        //Set pop job
                        unit.setJob(p.jobs.get(i));
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
        //.out.println(food);
    }

    public void organizePopulation(Planet p) {
        p.population.clear();
        //Sort through all the districts
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            //Point key = entry.getKey();
            Building value = entry.getValue();

            if (value instanceof PopulationStorage) {
                PopulationStorage storage = (PopulationStorage) value;
                for (PopulationUnit unit : storage.getPopulationArrayList()) {
                    p.population.add(unit);
                }
            }
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

    public void incrementCityPopulation(City city) {
        float increment = 0;
        increment += city.getPopulationUnitPercentage();

        HashMap<Race, Integer> species = new HashMap<>();
        ArrayList<PopulationStorage> storages = new ArrayList<>();
        int population = 0;
        for (Building building : city.buildings) {
            if (building instanceof PopulationStorage) {
                PopulationStorage storage = (PopulationStorage) building;
                storages.add(storage);
                for (PopulationUnit unit : storage.getPopulationArrayList()) {
                    //Population increment
                    //Fraction it so it does not accelerate at a crazy rate
                    //Do subtractions here in the future, like happiness, and etc.
                    increment += (unit.getSpecies().getBreedingRate() / 50);
                    //Add to hashmap
                    if (species.containsKey(unit.getSpecies())) {
                        //Add to it...
                        Integer count = species.get(unit.getSpecies());
                        count++;
                        species.put(unit.getSpecies(), count);
                    } else {
                        species.put(unit.getSpecies(), 1);
                    }
                    population++;
                }
            }
        }

        //Increment the value...
        city.setPopulationUnitPercentage(increment);
        if (increment > 100) {
            //Add population to city and stuff.
            //Get the species in the city...

            //Add to storage
            //Sum everything together for random numbers
            DistributedRandomNumberGenerator generator = new DistributedRandomNumberGenerator();
            int i = 0;
            HashMap<Integer, Race> races = new HashMap<>();
            for (Map.Entry<Race, Integer> entry : species.entrySet()) {
                Race key = entry.getKey();
                Integer value = entry.getValue();
                generator.addNumber(i, ((double) value / (double) population));
                races.put(i, key);
                i++;
            }

            //Get the race
            int speciesID = generator.getDistributedRandomNumber();
            Race r = races.get(speciesID);
            PopulationUnit unit = new PopulationUnit(r);

            //Increment population
            int storageID = (int) (Math.random() * storages.size());
            storages.get(storageID).getPopulationArrayList().add(unit);

            city.setPopulationUnitPercentage(0);
        }
    }
}
