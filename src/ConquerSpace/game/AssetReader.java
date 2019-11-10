package ConquerSpace.game;

import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.ResourceLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author zyunl
 */
public class AssetReader {

    private static final Logger LOGGER = CQSPLogger.getLogger(AssetReader.class.getName());

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

                    if (name.equals("food")) {
                        GameController.foodResource = res;
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
        GameController.resources = resources;
    }

    public static void readPopulationEvents() {
        ArrayList<JSONObject> events = new ArrayList<>();
        File launchSystemsFolder = ResourceLoader.getResourceByFile("dirs.events.population");

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
                    //int id = obj.getInt("id");
                    //String eventText = obj.getString("text");
                    //events.add(new PopulationEvent(id, eventText));
                    events.add(obj);
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
        GameController.events = events;
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
        try {
            //Open file
            Scanner s = new Scanner(ResourceLoader.getResourceByFile("text.ship.types.types"));
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
            //Open file
            Scanner s2 = new Scanner(ResourceLoader.getResourceByFile("text.ship.types.classification"));
            while (s2.hasNextLine()) {
                String st = s2.nextLine();
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
                    GameController.shipTypeClasses.put(sb.toString(), number);
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

    public static void readEngineTechs() {
        GameController.engineTechnologys = new ArrayList<>();
        File launchSystemsFolder = ResourceLoader.getResourceByFile("dirs.ship.engine.tech");
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
                    JSONObject obj = root.getJSONObject(i);
                    String name = obj.getString("name");
                    int id = obj.getInt("id");
                    float efficiency = obj.getFloat("efficiency");
                    float power = obj.getFloat("thrust_multiplier");
                    EngineTechnology tech = new EngineTechnology(name, efficiency, power);
                    tech.setId(id);
                    GameController.engineTechnologys.add(tech);
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

    public static void readPersonalityTraits() {
        ArrayList<PersonalityTrait> traits = new ArrayList<>();
        File traitsDir = ResourceLoader.getResourceByFile("dirs.traits");
        File[] files = traitsDir.listFiles();
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
                    JSONObject obj = root.getJSONObject(i);
                    String name = obj.getString("name");
                    PersonalityTrait trait = new PersonalityTrait();
                    trait.setName(name);
                    traits.add(trait);
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
        GameController.personalityTraits = traits;
    }
}
