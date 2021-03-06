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
package ConquerSpace.common.game.science;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.ships.EngineTechnology;
import ConquerSpace.common.game.ships.LaunchSystem;
import ConquerSpace.common.util.ResourceLoader;
import ConquerSpace.common.util.logging.CQSPLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.logging.log4j.Logger;
import org.hjson.JsonValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class Technologies implements Serializable {

    private static final Logger LOGGER = CQSPLogger.getLogger(Technologies.class.getName());

    public static final int RESEARCHED = 101;
    public static final int REVEALED = 100;

    public static ArrayList<Technology> readTech() {
        ArrayList<Technology> techs = new ArrayList<>();

        File techFolder = ResourceLoader.getResourceByFile("text.tech.techs");

        File[] tempFiles = techFolder.listFiles();
        for (File f : tempFiles) {
            if (!f.getName().equals("readme.txt")) {
                try {
                    readTechFromFile(f, techs);
                } catch (IOException ex) {
                    LOGGER.warn("IOException", ex);
                } catch (JSONException jsone) {
                    //Ignore
                    LOGGER.warn("Formatting issue with " + f.getName(), jsone);
                }
            }
        }
        return techs;
    }

    public static void readTechFromFile(File file, ArrayList<Technology> techs) throws FileNotFoundException, IOException, JSONException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String text = new String(data);
        text = JsonValue.readHjson(text).toString();

        JSONArray techList = new JSONArray(text);
        //Iterate over the techonologies
        for (int i = 0; i < techList.length(); i++) {
            JSONObject techonology = techList.getJSONObject(i);

            //Catch exceptions
            try {
                Technology t = parseTechnology(techonology);
                techs.add(t);
            } catch (JSONException jsone) {
                LOGGER.warn("Issue with " + techonology.getString("name"), jsone);
            }
        }
    }

    public static Technology parseTechnology(JSONObject techonology) {
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
            default:
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

        Technology t = new Technology(name, deps, type, level, fields, tags, actions, floor, difficulty);
        return t;
    }

    public static Technology getTechByName(GameState state, String s) {
        return (state.techonologies.stream().filter(e -> e.getName().equalsIgnoreCase(s))).findFirst().get();
    }

    public static Technology[] getTechsByTag(GameState state, String tag) {
        Object[] techList = state.techonologies.stream().filter(e -> Arrays.asList(e.getTags()).contains(tag)).toArray();
        return (Arrays.copyOf(techList, techList.length, Technology[].class));
    }

    public static Technology getTechByID(GameState state, int id) {
        return (state.techonologies.stream().filter(e -> e.getId() == id).findFirst().get());
    }

    public static void parseAction(String action, GameState gameState, Civilization c) {
        if (action.startsWith("tech")) {
            //Is boosting chance for tech
            //action = action.replace("tech(", "");
            //action = action.replace(")", "");
            String[] splitAction = action.split(":");
            //Get tech to boost
            String techtoboost = splitAction[1];
            int amount = Integer.parseInt(splitAction[2]);
            Technology tech = getTechByName(gameState, techtoboost);
            if (c.getCivTechs().containsKey(tech)) {
                if (c.getCivTechs().get(tech) <= 100) {
                    //Then add
                    if (c.getCivTechs().get(tech) > (100 - amount)) {
                        c.getCivTechs().put(tech, 100);
                    } else {
                        c.getCivTechs().put(tech, c.getCivTechs().get(tech) + amount);
                    }
                }
            } else {
                c.getCivTechs().put(tech, amount);
            }
        } else if (action.startsWith("boost")) {
            //Boosts a certain multiplier
            //Get civ multiplier
            String[] splitAction = action.split(":");
            if (c.getMultipliers().containsKey(splitAction[1])) {
                //Then add
                c.getMultipliers().put(splitAction[1], c.getMultipliers().get(splitAction[0]) + Double.parseDouble(splitAction[1]));
            } else {
                c.getMultipliers().put(splitAction[1], Double.parseDouble(splitAction[1]));
            }
        } else if (action.startsWith("value")) {
            //Sets a certain value
            String[] splitAction = action.split(":");
            c.getValues().put(splitAction[0], Integer.parseInt(splitAction[1]));
        } else if (action.startsWith("field")) {
            //Add the field that is mentioned
            String field = action.toLowerCase();
            String[] text = field.split(":");
            //Loop through the things
            //Use recursion
            c.upgradeField(text[1], Double.parseDouble(text[2]));
        } else if (action.startsWith("launch")) {
            //Set civ has launchpads
            c.getValues().put("haslaunch", 1);
            //unlocks a launch system
            //Get the launch system
            String[] text = action.split(":");

            //Remove trailing white space.
            String launchName = (text[1].trim());

            LaunchSystem launchSystem = gameState.launchSystems.stream().filter(e -> e.getName().equals(launchName)).findFirst().orElse(null);
            if (launchSystem != null) {
                c.getLaunchSystems().add(launchSystem.getReference());
            }

        } else if (action.startsWith("orbit")) {
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

            //TODO, preinitialized satellites
//            JSONObject s = GameController.satelliteTemplates.stream().
//                    filter(e -> e.getInt("id") == satelliteID).findFirst().orElseGet(null);
//            if (s != null) {
//                //Add it to civ
//                c.addSatelliteTemplate(s);
//            }
            //Or else ignore it. there is no need to complain.
        } else if (action.startsWith("component")) {
            //Do component
            char[] dst = new char[50];

            action.getChars("component".length() + 1, action.length() - 1, dst, 0);

            //String compName = (new String(dst).trim());
            //if (s != null) {
            //TODO add preinstalled templates
            //c.addShipComponent(s);
            //}
        } else if (action.startsWith("thrust")) {
            //Add engine tech
            //Do component
            String[] content = action.split(":");

            String compName = (content[1].trim());
            //int id = Integer.parseInt(compName);

            //Compare
            EngineTechnology t = gameState.engineTechnologys.stream().filter(
                    a -> gameState.getObject(a.getReference(), EngineTechnology.class)
                            .getIdentifier().equals(compName)).findFirst().orElse(null);
            if (t != null) {
                c.getEngineTechs().add(t.getReference());
            }
        } else if (action.startsWith("process")) {
            //Unlocks process
            String[] text = action.split(":");
            String content = text[1].trim();

            ProductionProcess process = gameState.prodProcesses.get(content);
            if (process != null) {
                c.getProductionProcesses().add(process);
            } else {
                LOGGER.trace("Could not find process " + content);
            }
        } else if (action.startsWith("mine")) {
            //Able to mine
            String[] text = action.split(":");
            String content = text[1].trim();

            StoreableReference goodId = gameState.getGoodId(content);
            if (goodId != null) {
                c.getMineableGoods().add(goodId);
            }
        } else if (action.startsWith("energy")) {
            //Add Energy source
            action.split(":");
        }
    }

    /**
     * Estimated finish time in ticks
     *
     * @param t tech
     * @return time to finish in ticks
     */
    public static int estFinishTime(Technology t) {
        return t.getDifficulty() * 1000;
    }

    public static FieldNode findNode(FieldNode n, String s) {
        if (n == null) {
            return null;
        }
        if (n.getName().equals(s)) {
            return n;
        }
        for (int i = 0; i < n.getChildCount(); i++) {
            FieldNode node = findNode(n.getNode(i), s);
            if (node == null) {
                continue;
            }
            if (node.getName().equals(s)) {
                return findNode(n.getNode(i), s);
            }
        }
        return null;
    }
}
