package ConquerSpace.game;

import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.tech.Fields;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.satellites.NoneSatellite;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.game.universe.ships.satellites.SatelliteTypes;
import ConquerSpace.game.universe.spaceObjects.ControlTypes;
import ConquerSpace.game.universe.spaceObjects.Planet;import ConquerSpace.game.universe.spaceObjects.SpaceObject;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;
import ConquerSpace.util.CQSPLogger;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.apache.logging.log4j.Logger;
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

    HashMap<UniversePath, Point> allsystemsstats;

    public GameUpdater(Universe u, StarDate s) {
        universe = u;
        allsystemsstats = new HashMap<>();
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
                //Set the parent star system visibility to true.
                //Will be back.
//                if (allsystemsstats.containsKey(p)) {
//                    //Get the stars around it.
//                    for (UniversePath path : allsystemsstats.keySet()) {
//
//                        //Get path position relative to the star system
//                        Point a = allsystemsstats.get(p);
//                        Point b = allsystemsstats.get(path);
//                        if (Math.hypot(a.x - b.x, a.y - b.y) < 100) {
//                            //Set visible
//                            universe.getCivilization(civIndex).vision.put(new UniversePath(p.getSectorID(), p.getSystemID()), VisionTypes.EXISTS);
//                        }
//                    }
//                }

            }
        }
    }

    public void calculateSystemPositions() {
        
    }

    public void initGame() {
        //Init tech and fields
        Fields.readFields();
        Technologies.readTech();
        
        //All things to load go here!!!
        readLaunchSystems();
        readSatellites();

        //All the home planets of the civs are theirs.
        //Set home planet and sector
        Random selector = new Random(universe.getSeed());

        for (int i = 0; i < universe.getCivilizationCount(); i++) {
            Civilization c = universe.getCivilization(i);
            //Add templates

            //Add the starting techs
            c.researchTech(Technologies.getTechByName("life"));

            //Add all starting techs
            for (Technology tech : Technologies.getTechsByTag("Starting")) {
                c.researchTech(tech);
            }

            //Select one of the space travel sciences
            Technology[] teks = Technologies.getTechsByTag("space travel base");
            //To research this
            c.civTechs.put(teks[selector.nextInt(teks.length)], 100);
            c.calculateTechLevel();

            //Add researchers
            //Only one.
            Scientist r = new Scientist("Person", 20);
            r.setSkill(1);
            c.people.add(r);

            UniversePath p = c.getStartingPlanet();
            if (universe.getSpaceObject(p) instanceof Planet) {
                Planet starting = (Planet) universe.getSpaceObject(p);
                int sectorCount = starting.getPlanetSectorCount();
                int id = selector.nextInt(sectorCount);
                PopulationStorage storage = new PopulationStorage(100l, 100l, (byte) 100);
                starting.setPlanetSector(id, storage);

                starting.setName(c.getHomePlanetName());

                //Set ownership
                starting.setOwnerID(c.getID());

                LOGGER.info("Civ " + c.getName() + " Starting planet: " + starting.getUniversePath());
            }
        }
        //Calculate all the star system positions.
        calculateSystemPositions();

        calculateControl();
        calculateVision();
    }

    public void readLaunchSystems() {
        ArrayList<LaunchSystem> launchSystems = new ArrayList<>();
        //Get the launch systems folder
        File launchSystemsFolder = new File(System.getProperty("user.dir") + "/assets/data/launch");
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
                JSONObject root = new JSONObject(text);

                String name = root.getString("name");

                String techName = root.getString("tech").split(":")[0];
                //The tech id will be the second value.
                int id = Integer.parseInt(root.getString("tech").split(":")[1]);

                int size = root.getInt("size");

                int safety = root.getInt("safety");

                int cost = root.getInt("cost");

                int constructCost = root.getInt("construct cost");

                boolean reusable = root.getBoolean("reusable");

                int reuseCost = 0;
                if (reusable) {
                    //Get Reusable cost
                    reuseCost = root.getInt("reuse cost");
                }

                int maxCargo = root.getInt("cargo");

                if (reusable) {
                    launchSystems.add(new LaunchSystem(name, Technologies.getTechByID(id), size, safety, cost, constructCost, reuseCost, maxCargo));
                } else {
                    launchSystems.add(new LaunchSystem(name, Technologies.getTechByID(id), size, safety, cost, constructCost, maxCargo));
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

    public void readSatellites() {
        ArrayList<Satellite> satellites = new ArrayList<>();
        //Get the launch systems folder
        File launchSystemsFolder = new File(System.getProperty("user.dir") + "/assets/data/satellite_types");
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
                JSONObject root = new JSONObject(text);

                //Read info. This one is a bit different, because the format is different
                //for each type.
                String name = root.getString("name");

                String type = root.getString("type");
                int mass = root.getInt("mass");
                int distance = root.getInt("dist");
                
                int typeID = -1;
                switch (type.toLowerCase()) {
                    case "none":
                        //Nothing to read.
                        typeID = SatelliteTypes.NONE;
                        break;
                }
                //That is it for now
                int id = root.getInt("id");
                
                //Get type, and do the thing
                Satellite s = null;
                switch(typeID) {
                    case SatelliteTypes.NONE:
                        s = new NoneSatellite(distance, mass);
                        s.setId(id);
                        s.setName(name);
                }
                
                satellites.add(s);
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
        GameController.satellites = satellites;
    }
}
