package ConquerSpace.game;

import ConquerSpace.Globals;
import static ConquerSpace.game.GameController.GameRefreshRate;
import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.BuildingBuilding;
import ConquerSpace.game.buildings.PopulationStorage;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.science.Fields;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.Vector;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.vision.VisionPoint;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.resources.RawResourceTypes;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import ConquerSpace.game.universe.ships.Orbitable;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.ShipClass;
import ConquerSpace.game.universe.ships.hull.Hull;
import ConquerSpace.game.universe.ships.hull.HullMaterial;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import ConquerSpace.game.universe.spaceObjects.ControlTypes;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.SpaceObject;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.gui.renderers.RendererMath;
import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.ResourceLoader;
import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This actually controls the game. If you take out this class, too bad...
 *
 * @author Zyun
 */
public class GameUpdater {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameUpdater.class.getName());

    private Universe universe;

    private ArrayList<StarSystemStats> visionStats;

    public GameUpdater(Universe u, StarDate s) {
        universe = u;
        visionStats = new ArrayList<>();
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
                    int dist = (int) Math.hypot(pos.getY() - visionStats.get(universe.getStarSystem(g).getId()).position.y,
                            pos.getX() - visionStats.get(universe.getStarSystem(g).getId()).position.x);
                    if (dist < range) {
                        //Its in!
                        int amount = ((int) ((1 - ((float) dist / (float) range)) * 100));
                        //int previous = universe.getCivilization(pt.getCivilization().vision.get(universe.getStarSystem(g).getUniversePath()));
                        universe.getCivilization(pt.getCivilization()).vision.put(universe.getStarSystem(g).getUniversePath(),
                                (amount > 100) ? 100 : (amount));
                    }
                }
            }
        }

        /*for (int k = 0; k < universe.getStarSystemCount(); k++) {
            for (int i = 0; i < universe.getStarSystem(k).getPlanetCount(); i++) {
                Planet p = universe.getStarSystem(k).getPlanet(i);
                //Get satellites
                for (Orbitable s : p.getSatellites()) {
                    if (s instanceof VisionPoint) {
                        //Compute
                        int range = ((VisionPoint) s).getRange();
                        //Distance between all star systems...
                        for (int g = 0; g < universe.getStarSystemCount(); g++) {
                            //Difference between points...
                            int dist = (int) Math.hypot(visionStats.get(k).position.y - visionStats.get(universe.getStarSystem(g).getId()).position.y,
                                    visionStats.get(k).position.x - visionStats.get(universe.getStarSystem(g).getId()).position.x);
                            if (dist < range) {
                                //Its in!
                                int amount = ((int) ((1 - ((float) dist / (float) range)) * 100));
                                int previous = universe.getCivilization(((VisionPoint) s).getCivilization()).vision.get(p.getUniversePath());
                                universe.getCivilization(((VisionPoint) s).getCivilization()).vision.put(universe.getStarSystem(g).getUniversePath(),
                                        ((previous + amount) > 100) ? 100 : (previous + amount));
                            }
                        }
                    }
                }
                //Observetaries
                /*for (PlanetSector sector : p.planetSectors) {
                    if (sector instanceof VisionPoint) {
                        int range = ((VisionPoint) sector).getRange();
                        for (int g = 0; g < universe.getStarSystemCount(); g++) {
                            //Difference between points...
                            int dist = (int) Math.hypot(visionStats.get(k).position.y - visionStats.get(universe.getStarSystem(g).getId()).position.y,
                                    visionStats.get(k).position.x - visionStats.get(universe.getStarSystem(g).getId()).position.x);
                            if (dist < range) {
                                //Its in!
                                int amount = ((int) ((1 - ((float) dist / (float) range)) * 100));
                                int previous = universe.getCivilization(((VisionPoint) sector).getCivilization()).vision.get(p.getUniversePath());

                                universe.getCivilization(((VisionPoint) sector).getCivilization()).vision.put(universe.getStarSystem(g).getUniversePath(),
                                        ((previous + amount) > 100) ? 100 : (previous + amount));
                            }
                        }
                    }

                }
            }*/
    }

    public void initGame() {
        //Init tech and fields
        Fields.readFields();
        Technologies.readTech();

        //All things to load go here!!!
        readLaunchSystems();
        readSatellites();
        readShipTypes();
        readShipComponents();

        //All the home planets of the civs are theirs.
        //Set home planet and sector
        Random selector = new Random(universe.getSeed());

        for (int i = 0; i < universe.getCivilizationCount(); i++) {
            Civilization c = universe.getCivilization(i);
            //Add templates
            //Add all starting techs
            for (Technology tech : Technologies.getTechsByTag("Starting")) {
                c.researchTech(tech);
            }

            //Select one of the space travel sciences
            Technology[] teks = Technologies.getTechsByTag("space travel base");
            //To research this
            c.civTechs.put(teks[selector.nextInt(teks.length)], 100);
            c.calculateTechLevel();

            //Add civ values
            c.putValue("optics.quality", 100);

            //Add researchers
            //Only one. Testing guy
            Scientist r = new Scientist("Person", 20);
            r.setSkill(1);
            c.people.add(r);

            HullMaterial material = new HullMaterial("Testing Hull Material", 100, 5, 12);
            c.hullMaterials.add(material);

            UniversePath p = c.getStartingPlanet();
            if (universe.getSpaceObject(p) instanceof Planet) {
                Planet starting = (Planet) universe.getSpaceObject(p);

                int popStorMas = (selector.nextInt(5) + 3);
                for (int count = 0; count < popStorMas; count++) {
                    PopulationStorage test = new PopulationStorage();
                    //Distribute
                    //Add random positions
                    int x = (selector.nextInt(starting.getPlanetSize() * 2 - 2) + 1);
                    int y = (selector.nextInt(starting.getPlanetSize() - 2) + 1);
                    ConquerSpace.game.universe.Point pt = new ConquerSpace.game.universe.Point(x, y);
                    starting.buildings.put(pt, test);

                    //Expand sector
                    //Choose a direction, and expand...
                    PopulationStorage test2 = new PopulationStorage();
                    int dir = selector.nextInt(4);
                    ConquerSpace.game.universe.Point pt2;
                    switch (dir) {
                        case 0:
                            pt2 = new ConquerSpace.game.universe.Point(pt.getX(), pt.getY() + 1);
                            break;
                        case 1:
                            pt2 = new ConquerSpace.game.universe.Point(pt.getX(), pt.getY() - 1);
                            break;
                        case 2:
                            pt2 = new ConquerSpace.game.universe.Point(pt.getX() + 1, pt.getY());
                            break;
                        case 3:
                            pt2 = new ConquerSpace.game.universe.Point(pt.getX() - 1, pt.getY());
                            break;
                        default:
                            pt2 = new ConquerSpace.game.universe.Point(pt.getX(), pt.getY() + 1);
                    }
                    //= new ConquerSpace.game.universe.Point(pt.getX(), pt.getY() + 1);
                    starting.buildings.put(pt2, test2);
                }

                //resourceStorage.addResource(RawResourceTypes., 0);
                //Add ship
                Ship s = new Ship(new ShipClass("test", new Hull(1, 1, material, 0, 0, "adsdf")), 0, 0, new Vector(0, 0), starting.getUniversePath());
                Actions.launchShip(s, starting, c);
                //Set ownership

                starting.setOwnerID(c.getID());

                c.habitatedPlanets.add(starting);

                //Add Civ initalize values
                c.values.put("haslaunch", 0);
                LOGGER.info("Civ " + c.getName() + " Starting planet: " + starting.getUniversePath());
            }
        }

        calculateControl();

        //Do calculations for system position
        calculateSystemPositions();
        calculateVision();
    }

    public void calculateSystemPositions() {
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            StarSystem sys = universe.getStarSystem(i);
            //Do the position
            visionStats.add(new StarSystemStats(RendererMath.polarCoordToCartesianCoord(sys.getGalaticLocation(), new Point(0, 0), 1), sys.getId()));
        }
    }

    public static void readResources() {
        ArrayList<Resource> resources = new ArrayList<>();
        File launchSystemsFolder = ResourceLoader.getResourceByFile("dirs.resources");

        File[] files = launchSystemsFolder.listFiles();
        for (File f : files) {
            FileInputStream fis = null;
            try {
                //If it is readme, continue
                if (f.getName().endsWith(".txt")) {
                    continue;
                }   //Read, there is only one object
                fis = new FileInputStream(f);
                byte[] data = new byte[(int) f.length()];
                fis.read(data);
                fis.close();
                String text = new String(data);
                JSONArray root = new JSONArray(text);
                for (int i = 0; i < root.length(); i++) {
                    JSONObject obj = root.getJSONObject(i);
                    String name = obj.getString("name");

                    //The tech id will be the second value.
                    int id = obj.getInt("id");

                    float rarity = obj.getFloat("rarity");

                    int value = obj.getInt("value");

                    float density = obj.getFloat("density");

                    int difficulty = obj.getInt("difficulty");

                    JSONArray color = obj.getJSONArray("color");

                    boolean mineable = obj.getBoolean("mineable");

                    Resource res = new Resource(name, value, rarity, id);
                    res.setDensity(density);
                    res.setDifficulty(difficulty);
                    res.setMineable(mineable);
                    res.setColor(color.getInt(0), color.getInt(1), color.getInt(2));
                    resources.add(res);
                }
            } catch (FileNotFoundException ex) {
                LOGGER.error("File not found!", ex);
            } catch (IOException ex) {
                LOGGER.error("IO exception!", ex);
            } catch (JSONException ex) {
                LOGGER.warn("JSON EXCEPTION!", ex);
            } finally {
                try {
                    //Because continue stat
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                }
            }
        }
        GameController.resources = resources;
    }

    public static void readLaunchSystems() {
        ArrayList<LaunchSystem> launchSystems = new ArrayList<>();
        //Get the launch systems folder
        File launchSystemsFolder = ResourceLoader.getResourceByFile("dirs.launch");
        File[] files = launchSystemsFolder.listFiles();
        for (File f : files) {
            FileInputStream fis = null;
            try {
                //If it is readme, continue
                if (f.getName().endsWith(".txt")) {
                    continue;
                }   //Read, there is only one object
                fis = new FileInputStream(f);
                byte[] data = new byte[(int) f.length()];
                fis.read(data);
                fis.close();
                String text = new String(data);
                JSONArray root = new JSONArray(text);
                for (int i = 0; i < root.length(); i++) {
                    JSONObject obj = root.getJSONObject(i);
                    String name = obj.getString("name");

                    String techName = obj.getString("tech").split(":")[0];
                    //The tech id will be the second value.
                    int id = Integer.parseInt(obj.getString("tech").split(":")[1]);

                    int size = obj.getInt("size");

                    int safety = obj.getInt("safety");

                    int cost = obj.getInt("cost");

                    int constructCost = obj.getInt("construct cost");

                    boolean reusable = obj.getBoolean("reusable");

                    int reuseCost = 0;
                    if (reusable) {
                        //Get Reusable cost
                        reuseCost = obj.getInt("reuse cost");
                    }

                    int maxCargo = obj.getInt("cargo");

                    if (reusable) {
                        launchSystems.add(new LaunchSystem(name, Technologies.getTechByID(id), size, safety, cost, constructCost, reuseCost, maxCargo));
                    } else {
                        launchSystems.add(new LaunchSystem(name, Technologies.getTechByID(id), size, safety, cost, constructCost, maxCargo));
                    }
                }
            } catch (FileNotFoundException ex) {
                LOGGER.error("File not found!", ex);
            } catch (IOException ex) {
                LOGGER.error("IO exception!", ex);
            } catch (JSONException ex) {
                LOGGER.warn("JSON EXCEPTION!", ex);
            } finally {
                try {
                    //Because continue stat
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                }
            }
        }
        GameController.launchSystems = launchSystems;
    }

    public static void readSatellites() {
        ArrayList<JSONObject> satellites = new ArrayList<>();
        //Get the launch systems folder
        File launchSystemsFolder = ResourceLoader.getResourceByFile("dirs.satellite.types");
        File[] files = launchSystemsFolder.listFiles();
        for (File f : files) {
            FileInputStream fis = null;
            try {
                //If it is readme, continue
                if (!f.getName().endsWith(".json")) {
                    continue;
                }   //Read, there is only one object
                fis = new FileInputStream(f);
                byte[] data = new byte[(int) f.length()];
                fis.read(data);
                fis.close();
                String text = new String(data);
                //JSONObject root = new JSONObject(text);
                JSONArray content = new JSONArray(text);
                for (int i = 0; i < content.length(); i++) {
                    satellites.add(content.getJSONObject(i));
                }
//                //Read info. This one is a bit different, because the format is different
//                //for each type.
//                String name = root.getString("name");
//
//                String type = root.getString("type");
//                int mass = root.getInt("mass");
//                int distance = root.getInt("dist");
//
//                int typeID = -1;
//                switch (type.toLowerCase()) {
//                    case "none":
//                        //Nothing to read.
//                        typeID = SatelliteTypes.NONE;
//                        break;
//                    case "telescope":
//                        typeID = SatelliteTypes.TELESCOPE;
//                        break;
//                    case "military":
//                        typeID = SatelliteTypes.MILITARY;
//                        break;
//                }
//                //That is it for now
//                int id = root.getInt("id");
//
//                //Get type, and do the thing
//                Satellite s = null;
//                switch (typeID) {
//                    case SatelliteTypes.NONE:
//                        s = new NoneSatellite(distance, mass);
//                        s.setId(id);
//                        s.setName(name);
//                        break;
//                    case SatelliteTypes.TELESCOPE:
//                        s = new SpaceTelescope(distance, mass);
//                        s.setId(id);
//                        s.setName(name);
//                        int range;
//                        //if(root.get("range") instanceof Integer)
//                        range = root.getInt("range");
//                        //else
//                            //range = root.getString("range"));
//                        ((SpaceTelescope) s).setRange(range);
//                }
                //satellites.add(root);
            } catch (FileNotFoundException ex) {
                LOGGER.error("File not found!", ex);
            } catch (IOException ex) {
                LOGGER.error("IO exception!", ex);
            } catch (JSONException ex) {
                LOGGER.warn("JSON EXCEPTION!", ex);
            } finally {
                try {
                    //Because continue stat
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                }
            }
        }
        GameController.satelliteTemplates = satellites;
    }

    public static void readShipTypes() {
        ArrayList<JSONObject> components = new ArrayList<>();

        try {
            //Open file
            Scanner s = new Scanner(new File(System.getProperty("user.dir") + "/assets/data/ship_types/shipTypes.txt"));
            while (s.hasNextLine()) {
                String st = s.nextLine();
                if (st.startsWith("#")) {
                    continue;
                } else if (st.startsWith("\"")) {
                    //Parse string
                    StringBuilder sb = new StringBuilder();
                    int i;
                    for (i = 1; i < st.length() && st.charAt(i) != '\"'; i++) {
                        sb.append(st.charAt(i));
                    }
                    //Get number
                    int number = Integer.parseInt(st.substring(i + 2));
                    GameController.shipTypes.put(sb.toString(), number);
                }
            }
        } catch (FileNotFoundException ex) {
            LOGGER.warn("CAnnot open ship types", ex);
        }
    }

    public static void readShipComponents() {
        File launchSystemsFolder = ResourceLoader.getResourceByFile("dirs.ship.components");
        File[] files = launchSystemsFolder.listFiles();
        for (File f : files) {
            FileInputStream fis = null;
            try {
                //If it is readme, continue
                if (!f.getName().endsWith(".json")) {
                    continue;
                }   //Read, there is only one object
                fis = new FileInputStream(f);
                byte[] data = new byte[(int) f.length()];
                fis.read(data);
                fis.close();
                String text = new String(data);
                JSONArray root = new JSONArray(text);
                for (int i = 0; i < root.length(); i++) {
                    GameController.shipComponentTemplates.add(root.getJSONObject(i));
                }

            } catch (FileNotFoundException ex) {
                LOGGER.error("File not found!", ex);
            } catch (IOException ex) {
                LOGGER.error("IO exception!", ex);
            } catch (JSONException ex) {
                LOGGER.warn("JSON EXCEPTION!", ex);
            } finally {
                try {
                    //Because continue stat
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
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
        }
    }

    //A class to hold the stats and position of a star system for vision.
    static class StarSystemStats {

        Point position;
        int id;

        public StarSystemStats(Point position, int id) {
            this.position = position;
            this.id = id;
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

        //Move ships
        for (Ship ship : sys.spaceShips) {
            double x = ship.getGoingToX() - ship.getX();
            double y = ship.getGoingToY() - ship.getY();

            //Normalize
            double len = Math.sqrt(x * x + y * y);
            if (len > 0) {
                x /= len;
                y /= len;
            }
            ship.translate((long) (x * ship.getMaxSpeed() * 5), (long) (y * ship.getMaxSpeed() * 5));

        }
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
        RendererMath.Point pt
                = RendererMath.polarCoordToCartesianCoord(p.getOrbitalDistance(),
                        p.getPlanetDegrees(), new RendererMath.Point(0, 0), 1);

        p.setX(pt.x);
        p.setY(pt.y);

        //Get the amount of resources to add
        HashMap<Integer, Integer> resources = new HashMap<>();
        resources.put(RawResourceTypes.ROCK, 0);
        resources.put(RawResourceTypes.GAS, 0);
        resources.put(RawResourceTypes.METAL, 0);
        resources.put(RawResourceTypes.FOOD, 0);
        resources.put(RawResourceTypes.ENERGY, 0);
        //Process buildings
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
                    p.buildings.put(key, build.getToBuild());
                }
            } else if (building instanceof SpacePort) {
                //Process
                SpacePort build = (SpacePort) building;
                //Iterate through launchpads and process
                for (SpacePortLaunchPad splp : build.launchPads) {
                    splp.ticks += GameController.GameRefreshRate;
                    //Get when to launch...
                }
            }
        }
        //Process storing of resourcese
        if (p.getOwnerID() >= 0) {
            for (Map.Entry<Integer, Integer> re : resources.entrySet()) {
                Integer key = re.getKey();
                Integer value = re.getValue();
                storeResource(key, value, p.getOwnerID(), p.getUniversePath());
            }
        }

    }

    public void processStar(Star s, StarDate date) {

    }

    public void storeResource(int resourceType, int amount, int owner, UniversePath from) {
        //Process
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
            //Process resources
            for (ResourceStockpile s : c.resourceStorages) {
                //Get resource types allowed, and do stuff
                //c.resourceList.
                for (int type : s.storedTypes()) {
                    //add to index
                    int amountToAdd = (c.resourceList.get(type) + s.getResourceAmount(type));
                    c.resourceList.put(type, amountToAdd);
                }
            }
        }
    }
}
