package ConquerSpace.game;

import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.science.Fields;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.vision.VisionPoint;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.ships.hull.HullMaterial;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.game.universe.spaceObjects.ControlTypes;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.SpaceObject;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.game.universe.spaceObjects.pSectors.Observatory;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;
import ConquerSpace.gui.renderers.RendererMath;
import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.ResourceLoader;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
//                if (universe.control.containsKey(new UniversePath(p.getSectorID(), p.getSystemID()))) {
//                    if (planet.getOwnerID() > -1) {
//                        if (planet.getOwnerID() != universe.control.get(new UniversePath(p.getSectorID(), p.getSystemID()))) {
//                            universe.control.put(new UniversePath(p.getSectorID(), p.getSystemID()), -1);
//                        }
//                    }
//                } else {
//                    universe.control.put(new UniversePath(p.getSectorID(), p.getSystemID()), planet.getOwnerID());
//                }
            } else if (spaceObject instanceof Star) {
                Star star = (Star) spaceObject;
                //Get the control.
                universe.control.put(p, star.getOwnerID());
//                if (universe.control.containsKey(new UniversePath(p.getSectorID(), p.getSystemID()))) {
//                    if (star.getOwnerID() > -1) {
//                        if (star.getOwnerID() != universe.control.get(new UniversePath(p.getSectorID(), p.getSystemID()))) {
//                            universe.control.put(new UniversePath(p.getSectorID(), p.getSystemID()), ControlTypes.NONE_CONTROLLED);
//                        }
//                        //numofcivsinsystem.put(new UniversePath(p.getSectorID(), p.getSystemID()), numofcivsinsystem.get(new UniversePath(p.getSectorID(), p.getSystemID())) | star.getOwnerID());
//                    }
//                } else {
//                    universe.control.put(new UniversePath(p.getSectorID(), p.getSystemID()), star.getOwnerID());
//                }
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

        for (int k = 0; k < universe.getStarSystemCount(); k++) {
            for (int i = 0; i < universe.getStarSystem(k).getPlanetCount(); i++) {
                Planet p = universe.getStarSystem(k).getPlanet(i);
                //Get satellites
                for (Satellite s : p.getSatellites()) {
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
                for (PlanetSector sector : p.planetSectors) {
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
            }
        }
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
                int sectorCount = starting.getPlanetSectorCount();
                int id = selector.nextInt(sectorCount);
                PopulationStorage storage = new PopulationStorage(100l, 100l, (byte) 100);
                starting.setPlanetSector(id, storage);
                //Add observetary
                Observatory obs = new Observatory(15);
                obs.setOwner(c.getID());
                id++;
                id %= sectorCount;
                starting.setPlanetSector(id, obs);
                starting.setName(c.getHomePlanetName());

                //Set ownership
                starting.setOwnerID(c.getID());

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

    public void readLaunchSystems() {
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

    public void readShipTypes() {
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

    public void readShipComponents() {
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
                return (int)(((double)quality/100d) * size * size * Math.PI);
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
}
