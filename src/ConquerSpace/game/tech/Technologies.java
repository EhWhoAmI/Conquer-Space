package ConquerSpace.game.tech;

import ConquerSpace.game.GameController;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.util.CQSPLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Zyun
 */
public class Technologies {

    private static final Logger LOGGER = CQSPLogger.getLogger(Technologies.class.getName());
    public static ArrayList<Technology> techonologies = new ArrayList<>();
    public static ArrayList<String> fields = new ArrayList<>();

    public static final int RESEARCHED = 101;
    public static final int REVEALED = 100;
    
    public static void readTech() {
        File techFolder = new File(System.getProperty("user.dir") + "/assets/tech/techs");
        File[] tempFiles = techFolder.listFiles();
        for (File f : tempFiles) {
            try {
                readTechFromFile(f);
            } catch (IOException ex) {
                LOGGER.warn("IOException", ex);
            } catch (JSONException jsone) {
            }
        }
    }

    public static void readTechFromFile(File file) throws FileNotFoundException, IOException, JSONException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String text = new String(data);
        JSONObject root = new JSONObject(text);
        JSONArray techList = root.getJSONArray("techonologies");

        //Iterate over the techonologies
        for (int i = 0; i < techList.length(); i++) {
            JSONObject techonology = techList.getJSONObject(i);
            String name = techonology.getString("name");
            JSONArray deplist = techonology.getJSONArray("deps");
            //Loop over deps
            String[] deps = new String[deplist.length()];
            for (int j = 0; j < deplist.length(); j++) {
                deps[j] = deplist.getString(j);
            }

            //Tech level
            int level = techonology.getInt("level");

            int type = -1;
            switch (techonology.getString("type")) {
                case "UNLOCK":
                    type = TechonologyTypes.UNLOCK;
                    break;
                case "UPGRADE":
                    type = TechonologyTypes.UPGRADE;
                    break;
            }

            //Difficulty
            int difficulty = techonology.getInt("difficulty");
            
            JSONArray fieldsArray = techonology.getJSONArray("fields");

            //Loop over fields
            String[] fields = new String[fieldsArray.length()];
            for (int j = 0; j < fieldsArray.length(); j++) {
                fields[j] = fieldsArray.getString(j);
            }

            JSONArray tagsArray = techonology.getJSONArray("tags");

            String[] tags = new String[tagsArray.length()];
            for (int j = 0; j < tagsArray.length(); j++) {
                tags[j] = tagsArray.getString(j);
            }
            
            JSONArray actionsarry = techonology.getJSONArray("action");
            
            String[] actions = new String[actionsarry.length()];
            for (int j = 0; j < actionsarry.length(); j++) {
                actions[j] = actionsarry.getString(j);
            }
            
            //Floor
            int floor = techonology.getInt("floor");

            //ID
            int id = techonology.getInt("id");
            Technology t = new Technology(name, id, deps, type, level, fields, tags, actions, floor, difficulty);
            techonologies.add(t);
        }
    }

    public static Technology getTechByName(String s) {
        return (techonologies.stream().filter(e -> e.getName().toLowerCase().equals(s.toLowerCase()))).findFirst().get();
    }

    public static Technology[] getTechsByTag(String tag) {
        Object[] techList = techonologies.stream().filter(e -> Arrays.asList(e.getTags()).contains(tag)).toArray();
        return (Arrays.copyOf(techList, techList.length, Technology[].class));
    }
    
    public static Technology getTechByID(int id) {
        return (techonologies.stream().filter(e -> e.getId() == id).findFirst().get());
    }
    public static void parseAction(String action, Civilization c) {
        if(action.startsWith("tech")) {
            //Is boosting chance for tech
            action = action.replace("tech(", "");
            action = action.replace(")", "");
            String[] splitAction = action.split(":");
            //Get tech to boost
            String techtoboost = splitAction[0];
            int amount = Integer.parseInt(splitAction[1]);
            Technology tech = getTechByName(techtoboost);
            if(c.civTechs.containsKey(tech)) {
                if(!(c.civTechs.get(tech) > 100)) {
                    //Then add
                    if(c.civTechs.get(tech) > (100 - amount)) {
                        c.civTechs.put(tech, 100);
                    } else {
                        c.civTechs.put(tech, c.civTechs.get(tech) + amount);
                    }
                }
            } else {
                c.civTechs.put(tech, amount);
            }
        }
        else if(action.startsWith("boost")) {
            //Boosts a certain multiplier
            //Get civ multiplier
            action = action.replace("boost(", "");
            action = action.replace(")", "");
            String[] splitAction = action.split(":");
            if(c.multipliers.containsKey(splitAction[0])) {
                //Then add
                c.multipliers.put(splitAction[0], c.multipliers.get(splitAction[0]) + Integer.parseInt(splitAction[1]));
            } else {
                c.multipliers.put(splitAction[0], Integer.parseInt(splitAction[1]));
            }
        } else if(action.startsWith("field")) {
            action = action.replace("field(", "");
            action = action.replace(")", "");
            //Add the field that is mentioned
            //Skip fields for now TODO.
        } else if(action.startsWith("launch")) {
            //unlocks a launch system
            //Get the launch system
            char[] dst = new char[50];
            action.getChars(7, action.length() - 1, dst, 0);
            
            //Remove trailing white space.
            String launchName = (new String(dst).trim());

            LaunchSystem sys = GameController.launchSystems.stream().filter(e -> e.getName().equals(launchName)).findFirst().orElse(null);
            c.launchSystems.add(sys);
        } else if(action.startsWith("orbit")) {
            //Something you put into orbit
            char[] dst = new char[50];
            action.getChars("orbit".length() + 1, action.length() - 1, dst, 0);
            
            //Remove trailing white space.
            String orbitName = (new String(dst).trim());
            //Get the satellite from the name or id.
            //Find the satellite
            //Split it
            String[] orbitSplit = orbitName.split(":");
            int satelliteID = Integer.parseInt(orbitSplit[1]);
            Satellite s = GameController.satellites.stream().
                    filter(e -> e.getId() == satelliteID).findFirst().orElseGet(null);
            if(s != null) {
                //Add it to civ
                c.addSatellite(s);
            }
            //Or else ignore it. there is no need to complain.
        }
    }
    
    /**
     * Estimated finish time in ticks
     * @param t tech
     * @return time to finish in ticks
     */
    public static int estFinishTime(Technology t) {
        return t.getDifficulty() * 1000;
    }
}
