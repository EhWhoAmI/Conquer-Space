package ConquerSpace.game.save;

import ConquerSpace.ConquerSpace;
import ConquerSpace.game.StarDate;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.BuildingBuilding;
import ConquerSpace.game.buildings.Observatory;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.buildings.ResourceMinerDistrict;
import ConquerSpace.game.buildings.ResourceStorage;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.ships.Orbitable;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.hull.Hull;
import ConquerSpace.game.universe.ships.hull.HullMaterial;
import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import ConquerSpace.game.universe.ships.satellites.NoneSatellite;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.game.universe.ships.satellites.SpaceTelescope;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author zyunl
 */
public class SaveGame {

    private File folder;

    public SaveGame(File file) {
        this.folder = file;
    }

    public SaveGame(String s) {
        folder = new File(s);
    }

    public void save(Universe u, StarDate date) throws IOException {
        //Get the file
        if (!folder.exists()) {
            folder.mkdirs();
        }

        //Check for files, and delete
        if (folder.listFiles().length > 0) {
            deleteFolder(folder);
        }

        //Now create items
        //Create info file 
        {
            File metaFile = new File(folder, "meta");
            //Add content
            metaFile.createNewFile();

            //JSON
            JSONObject root = new JSONObject();

            root.put("date", date.bigint);
            root.put("seed", u.getSeed());
            root.put("size", u.getStarSystemCount());
            root.put("civs", u.getCivilizationCount());
            root.put("version", (ConquerSpace.VERSION.getMajor() + "." + ConquerSpace.VERSION.getMinor() + "." + ConquerSpace.VERSION.getPatch()));

            PrintWriter pw = new PrintWriter(metaFile);
            pw.print(root.toString(2).replace("\n", System.getProperty("line.separator")));
            pw.close();
        }

        {
            File systemFile = new File(folder, "systems");
            //Add content
            systemFile.createNewFile();

            JSONArray systemArray = new JSONArray();
            //Do the universe
            for (int sys = 0; sys < u.getStarSystemCount(); sys++) {
                StarSystem system = u.getStarSystem(sys);

                JSONObject obj = new JSONObject();

                obj.put("id", system.getId());
                obj.put("deg", system.getGalaticLocation().getDegrees());
                obj.put("dist", system.getGalaticLocation().getDistance());
                obj.put("planet-c", system.getPlanetCount());
                obj.put("star-c", system.getStarCount());

                //Now stuff everything into that
                ArrayList<JSONObject> planets = new ArrayList<>();
                for (int p = 0; p < system.getPlanetCount(); p++) {
                    Planet planet = system.getPlanet(p);

                    JSONObject object = new JSONObject();
                    object.put("type", planet.getPlanetType());
                    object.put("size", planet.getPlanetSize());
                    object.put("seed", planet.getTerrainSeed());
                    object.put("name", planet.getName());
                    object.put("dist", planet.getOrbitalDistance());
                    object.put("deg", planet.getPlanetDegrees());

                    //Buildings
                    ArrayList<JSONObject> buildings = new ArrayList<>();
                    for (Map.Entry<GeographicPoint, Building> entry : planet.buildings.entrySet()) {
                        GeographicPoint key = entry.getKey();
                        Building value = entry.getValue();
                        JSONObject building = parseBuilding(key, value);
                        buildings.add(building);
                    }
                    object.put("ings", buildings);

                    //Resource veins
                    ArrayList<JSONObject> veins = new ArrayList<>();
                    for (ResourceVein v : planet.resourceVeins) {
                        JSONObject veinObject = new JSONObject();
                        veinObject.put("id", v.getId());
                        veinObject.put("diff", v.getDifficulty());
                        veinObject.put("radius", v.getRadius());
                        veinObject.put("x", v.getX());
                        veinObject.put("y", v.getY());
                        veinObject.put("res", v.getResourceType().getId());
                        veinObject.put("amt", v.getResourceAmount());
                        veins.add(veinObject);
                    }
                    object.put("veins", veins);

                    //Satellites
                    ArrayList<JSONObject> satellites = new ArrayList<>();
                    for (Orbitable b : planet.getSatellites()) {
                        JSONObject orbitable = new JSONObject();

                        if (b instanceof Satellite) {
                            orbitable.put("type", "satellite");
                            Satellite ship = (Satellite) b;
                            orbitable.put("object", processSatellite(ship));
                        } else if (b instanceof Ship) {
                            orbitable.put("type", "ship");
                            Ship ship = (Ship) b;
                            orbitable.put("object", processShip(ship));
                        }

                        veins.add(orbitable);
                    }
                    object.put("satellites", satellites);
                    planets.add(object);
                }
                obj.put("planets", planets);

                systemArray.put(obj);
            }
            PrintWriter pw = new PrintWriter(systemFile);
            pw.print(systemArray.toString(2).replace("\n", System.getProperty("line.separator")));
            pw.close();
        }

        {
            File civFile = new File(folder, "civ");
            civFile.createNewFile();
            JSONArray civArray = new JSONArray();

            //Civs
            for (int civ = 0; civ < u.getCivilizationCount(); civ++) {
                Civilization civilization = u.getCivilization(civ);
                JSONObject object = new JSONObject();

                //There is a lot to add here.... halp
                object.put("id", civilization.getID());
                object.put("name", civilization.getName());

                JSONArray materialArray = new JSONArray();
                for (HullMaterial mat : civilization.hullMaterials) {
                    JSONObject materialObject = new JSONObject();
                    materialObject.put("id", mat.getId());
                    materialObject.put("cost", mat.getCost());
                    materialObject.put("density", mat.getDensity());
                    materialObject.put("name", mat.getName());
                    materialObject.put("strength", mat.getStrength());
                    materialArray.put(materialObject);
                }
                object.put("hull-materials", materialArray);

                JSONArray hullArray = new JSONArray();
                for (Hull h : civilization.hulls) {
                    JSONObject hullObject = new JSONObject();
                    hullObject.put("mass", h.getMass());
                    hullObject.put("material", h.getMaterial().getId());
                    hullObject.put("space", h.getSpace());
                    hullObject.put("type", h.getShipType());
                    hullObject.put("thrust", h.getThrust());
                    hullObject.put("strength", h.getStrength());
                    hullArray.put(hullObject);
                }
                object.put("hulls", hullArray);

                civArray.put(object);
            }
            PrintWriter pw = new PrintWriter(civFile);
            pw.print(civArray.toString(2).replace("\n", System.getProperty("line.separator")));
            pw.close();
        }
        
        {
            //Politics and relations
        }
    }

    private JSONObject parseBuilding(GeographicPoint key, Building what) {
        JSONObject buildingObject = new JSONObject();
        buildingObject.put("x", key.getX());
        buildingObject.put("y", key.getY());

        //Get type...
        if (what instanceof BuildingBuilding) {
            //Get type of building
            Building b = ((BuildingBuilding) what).getToBuild();

            JSONObject toBuild = parseBuilding(key, b);
            buildingObject.put("building", toBuild);
            buildingObject.put("type", "building");

        } else if (what instanceof SpacePort) {
            SpacePort port = (SpacePort) what;
            buildingObject.put("type", "port");

            buildingObject.put("size", port.launchPads.size());
            JSONArray arr = new JSONArray();
            for (SpacePortLaunchPad res : port.launchPads) {
                JSONObject storedResource = new JSONObject();
                buildingObject.put("ticks", res.ticks);
                buildingObject.put("type", res.getType().getId());
                arr.put(storedResource);
            }
        } else if (what instanceof CityDistrict) {
            CityDistrict stor = (CityDistrict) what;
            buildingObject.put("type", "population");
            int pop = stor.getPopulations();
            buildingObject.put("population", pop);
        } else if (what instanceof Observatory) {
            Observatory obs = (Observatory) what;
            buildingObject.put("type", "observatory");
            buildingObject.put("lens-size", obs.getLensSize());
            buildingObject.put("range", obs.getRange());
        } else if (what instanceof ResourceStorage) {
            ResourceStorage storage = (ResourceStorage) what;
            buildingObject.put("type", "storage");
            buildingObject.put("upkeep", storage.getUpkeep());
            //Add things
            JSONArray arr = new JSONArray();
            for (Resource res : storage.storedTypes()) {
                JSONObject storedResource = new JSONObject();
                storedResource.put("type", res.getId());
                storedResource.put("amount", storage.getResourceAmount(res));
                arr.put(storedResource);
            }
            buildingObject.put("resources", arr);
        } else if (what instanceof ResourceMinerDistrict) {
            ResourceMinerDistrict gatherer = (ResourceMinerDistrict) what;
            //Get the stuff
            buildingObject.put("type", "gatherer");
            buildingObject.put("resource", gatherer.getVeinMining().getId());
            buildingObject.put("amount", gatherer.getAmountMined());
            buildingObject.put("resource-type", gatherer.getResourceMining().getId());
        }
        return buildingObject;
    }

    private JSONObject processShip(Ship what) {
        //Process ship
        JSONObject object = new JSONObject();
        object.put("name", what.getName());
        object.put("id", what.getId());
        object.put("class", what.getShipClass());
        object.put("x", what.getX());
        object.put("y", what.getY());
        if (what.isOrbiting()) {
            object.put("orbiting", what.getOrbiting());
        }
        object.put("going-x", what.getGoingToX());
        object.put("going-y", what.getGoingToY());
        object.put("throttle", what.getThrottle());
        object.put("thrust", what.getEstimatedThrust());
        object.put("location", what.getLocation().toString());
        object.put("mass", what.getMass());

        Hull h = what.getHull();

        //Hull info
        object.put("hull-mass", h.getMass());
        object.put("hull-material", h.getMaterial().getId());
        object.put("hull-space", h.getSpace());
        object.put("hull-type", h.getShipType());
        object.put("hull-thrust", h.getThrust());
        object.put("hull-strength", h.getStrength());
        return object;
    }

    private JSONObject processSatellite(Satellite what) {
        JSONObject object = new JSONObject();
        //get type
        //Get info...
        object.put("mass", what.getMass());
        object.put("orbiting", what.getOrbiting().toString());
        object.put("owner", what.getOwner());
        if (what instanceof NoneSatellite) {
            object.put("type", "none");
        } else if (what instanceof SpaceTelescope) {
            SpaceTelescope telescope = (SpaceTelescope) what;
            object.put("type", "telescope");
            object.put("range", telescope.getRange());
        }
        return object;
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
    }
}
