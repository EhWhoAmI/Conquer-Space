package ConquerSpace.game;

import ConquerSpace.Globals;
import static ConquerSpace.game.GameController.GameRefreshRate;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.actions.ShipAction;
import ConquerSpace.game.buildings.AdministrativeCenter;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.BuildingBuilding;
import ConquerSpace.game.buildings.City;
import ConquerSpace.game.buildings.FarmBuilding;
import ConquerSpace.game.buildings.PopulationStorage;
import ConquerSpace.game.buildings.ResourceMinerDistrict;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.population.JobRank;
import ConquerSpace.game.population.JobType;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.vision.VisionPoint;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import ConquerSpace.game.universe.spaceObjects.ControlTypes;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.SpaceObject;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.gui.renderers.RendererMath;
import ConquerSpace.util.CQSPLogger;
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
                //System.out.println("Putting vision for civ " + civIndex + " at " + p);
                //System.out.println("Planet size: " + ((Planet)universe.getSpaceObject(p)).planetSectors.length);
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
                        int amount = ((int) ((1 - ((double) dist / (double) range)) * 100));
                        //int previous = universe.getCivilization(pt.getCivilization().vision.get(universe.getStarSystem(g).getUniversePath()));
                        universe.getCivilization(pt.getCivilization()).vision.put(universe.getStarSystem(g).getUniversePath(),
                                (amount > 100) ? 100 : (amount));
                    }
                }
            }
        }
    }

    //Here is where a lot of the math is gonna be held.
    public static class Calculators {

        public static class Optics {

            public static int getRange(int quality, int size) {
                return (int) (Math.log(Math.PI * (size) * (size) + 1) * 2);
            }

            public static int getLensMass(int quality, int size) {
                return (int) (((double) quality / 100d) * size * size * Math.PI);
            }

            public static int getLensSize(int quality, int range) {
                //Math.PI * (size) * (size) + 1

                return (int) (Math.sqrt((Math.pow(Math.E, range / 2) - 1) / Math.PI));
            }
        }

        public static class Engine {

            public static int getEngineMass(int thrust, EngineTechnology tech) {
                return (int) (tech.getThrustMultiplier() * thrust);
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
        //Update the position
        RendererMath.Point pt
                = RendererMath.polarCoordToCartesianCoord(sys.getGalaticLocation().getDistance(),
                        sys.getGalaticLocation().getDegrees(), new RendererMath.Point(0, 0), 1);

        sys.setX(pt.x);
        sys.setY(pt.y);

        //Process turn of the planets then the stars.
        //Maybe later the objects in space.
        for (int i = 0; i < sys.getPlanetCount(); i++) {
            processPlanet(sys.getPlanet(i), date);
        }

        for (int i = 0; i < sys.getStarCount(); i++) {
            processStar(sys.getStar(i), date);
        }
    }

    public void processPlanet(Planet p, StarDate date) {
        //Calculate position
        //Increase degrees
        //Calculate degrees to mod

        p.modDegrees(p.getDegreesPerTurn());
        RendererMath.Point pt
                = RendererMath.polarCoordToCartesianCoord(p.getOrbitalDistance(),
                        p.getPlanetDegrees(), new RendererMath.Point(0, 0), 1);

        p.setX(pt.x);
        p.setY(pt.y);

        //Get the amount of resources to add
        HashMap<Resource, Integer> resources = new HashMap<>();

        for (Resource r : GameController.resources) {
            resources.put(r, 0);
        }

        //Process buildings, and jobs
        for (Map.Entry<ConquerSpace.game.universe.Point, Building> entry : p.buildings.entrySet()) {
            ConquerSpace.game.universe.Point key = entry.getKey();
            Building building = entry.getValue();
            //Process
            if (building instanceof BuildingBuilding) {
                BuildingBuilding build = (BuildingBuilding) building;
                if (build.getLength() > 0) {
                    //build.incrementTick();
                    build.decrementLength(GameController.GameRefreshRate);
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
                            for (PopulationStorage stor : c.storages) {
                                //Find the storage
                                ConquerSpace.game.universe.Point storagePoint = p.buildings
                                        .entrySet()
                                        .stream()
                                        .filter(ent -> stor.equals(ent.getValue()))
                                        .map(Map.Entry::getKey).findFirst().get();
                                if (storagePoint != null) {
                                    //Check if next to city point
                                    if (storagePoint.getX() + 1 == key.getX()
                                            || storagePoint.getX() - 1 == key.getX()
                                            || storagePoint.getY() + 1 == key.getY()
                                            || storagePoint.getY() - 1 == key.getY()) {
                                        //Add to city
                                        c.storages.add((PopulationStorage) build.getToBuild());
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
                        }
                    }
                    p.buildings.put(key, build.getToBuild());
                }
            } else if (building instanceof AdministrativeCenter) {
                //Set jobs
                AdministrativeCenter adminCenter = (AdministrativeCenter) building;
                for (PopulationUnit j : adminCenter.population) {
                    j.getJob().setJobType(JobType.Administrator);
                    j.getJob().setJobRank(JobRank.High);
                }

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
                ResourceMinerDistrict gatherer = (ResourceMinerDistrict) building;
                ResourceVein vein = gatherer.getVeinMining();
                if (vein != null && vein.getResourceAmount() > 0) {
                    //Get the resource stockpiles
                    //Just stuff the resource into the jobs
                    for (PopulationUnit j : gatherer.getPopulationArrayList()) {
                        vein.removeResources((int) gatherer.getAmountMined());
                        j.getJob().resources.put(vein.getResourceType(), (int) gatherer.getAmountMined());
                        j.getJob().setJobType(JobType.Miner);
                        j.getJob().setJobRank(JobRank.Low);
                    }
                    resources.put(gatherer.getResourceMining(), (int) (resources.get(gatherer.getResourceMining()) + gatherer.getAmountMined()));
                }

            } else if (building instanceof FarmBuilding) {
                //Get the resources
                FarmBuilding farmBuilding = (FarmBuilding) building;
                //System.out.println("hh" + farmBuilding.getProductivity());
                for (PopulationUnit j : farmBuilding.getPopulationArrayList()) {
                    j.getJob().resources.put(GameController.foodResource, farmBuilding.getProductivity());
                    j.getJob().setJobType(JobType.Farmer);
                    j.getJob().setJobRank(JobRank.Low);
                }
                resources.put(GameController.foodResource, (resources.get(GameController.foodResource) + farmBuilding.getProductivity()));
                //System.out.println((resources.get(GameController.foodResource) + farmBuilding.getProductivity()));
            }
        }
        //Process storing of resourcese
//        if (p.getOwnerID() >= 0) {
//            for (Map.Entry<Resource, Integer> re : resources.entrySet()) {
//                Resource key = re.getKey();
//                Integer value = re.getValue();
//                storeResource(key, value, p.getOwnerID(), p.getUniversePath());
//            }
//        }

        //Process population
        for (City city : p.cities) {
            float increment = 0;
            increment += city.getPopulationUnitPercentage();

            for (PopulationStorage storage : city.storages) {
                for (PopulationUnit unit : storage.getPopulationArrayList()) {
                    //Population increment
                    //Fraction it so it does not accelerate at a crazy rate
                    //Do subtractions here in the future, like happiness, and etc.
                    increment += (unit.getSpecies().getBreedingRate() / 50);
                }
            }
            //Increment the value...
            city.setPopulationUnitPercentage(increment);
            if (increment > 100) {
                //Add population to city and stuff. Danm you have to get the civ. that is annoying
                int owner = p.getOwnerID();
                Civilization c = universe.getCivilization(owner);
                //Add to storage
                PopulationUnit unit = new PopulationUnit(c.getFoundingSpecies());
                unit.setSpecies(c.getFoundingSpecies());
                c.population.add(unit);
                //Add to random population storage
                //TODO: do something that actually calculates the probalities
                city.storages.get((int) (Math.random() * city.storages.size())).getPopulationArrayList().add(unit);
                city.setPopulationUnitPercentage(0);
            }
        }

        for (Map.Entry<ConquerSpace.game.universe.Point, Building> entry : p.buildings.entrySet()) {
            ConquerSpace.game.universe.Point key = entry.getKey();
            Building value = entry.getValue();
            if (value instanceof PopulationStorage) {
                PopulationStorage storage = (PopulationStorage) value;
                for (PopulationUnit unit : storage.getPopulationArrayList()) {
                    //Add normal pop upkeep
                    int foodToDealWith = -unit.getSpecies().getFoodPerMonth();
                    if (unit.getJob().resources.containsKey(GameController.foodResource)) {
                        foodToDealWith += unit.getJob().resources.get(GameController.foodResource);
                    }
                    unit.getJob().resources.put(GameController.foodResource, foodToDealWith);
                    //Population increment
                    //Fraction it so it does not accelerate at a crazy rate
                    //Do subtractions here in the future, like happiness, and etc.
                    //Process job
                    for (Resource r : unit.getJob().resources.keySet()) {
                        storeResource(r, unit.getJob().resources.get(r), p.getOwnerID(), p.getUniversePath());
                    }
                    //All people consume food
                }
            }
        }
        //Process locallife
        for (LocalLife localLife : p.localLife) {
            int biomass = localLife.getBiomass();
            float breedingRate = localLife.getReproductionRate();
            localLife.setBiomass((int) (breedingRate * biomass) + biomass);
        }
    }

    public void processStar(Star s, StarDate date) {

    }

    public void storeResource(Resource resourceType, int amount, int owner, UniversePath from) {
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

    public boolean removeResource(Resource resourceType, int amount, int owner, UniversePath from) {
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

    public void processResources() {
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Civilization c = Globals.universe.getCivilization(i);
            for (Map.Entry<Resource, Integer> entry : c.resourceList.entrySet()) {
                c.resourceList.put(entry.getKey(), 0);
            }
            //Process resources
            for (ResourceStockpile s : c.resourceStorages) {
                //Get resource types allowed, and do stuff
                //c.resourceList.
                for (Resource type : s.storedTypes()) {
                    //add to index
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
        //Loop through star systems
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            StarSystem system = universe.getStarSystem(i);
            RendererMath.Point pt
                    = RendererMath.polarCoordToCartesianCoord(system.getGalaticLocation().getDistance(),
                            system.getGalaticLocation().getDegrees(), new RendererMath.Point(0, 0), 1);

            system.setX(pt.x);
            system.setY(pt.y);
            for (int k = 0; k < system.getPlanetCount(); k++) {
                Planet planet = system.getPlanet(k);
                RendererMath.Point ppt
                        = RendererMath.polarCoordToCartesianCoord(planet.getOrbitalDistance(),
                                planet.getPlanetDegrees(), new RendererMath.Point(0, 0), 1);

                planet.setX(ppt.x);
                planet.setY(ppt.y);
            }
        }
    }
}
