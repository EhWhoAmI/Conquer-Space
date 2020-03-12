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

import ConquerSpace.game.buildings.BuildingCostGetter;
import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.universe.goods.Element;
import ConquerSpace.game.universe.goods.Good;
import ConquerSpace.game.universe.goods.NonElement;
import ConquerSpace.game.universe.goods.Ore;
import ConquerSpace.game.universe.goods.ProductionProcess;
import ConquerSpace.game.universe.goods.ResourceDistribution;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.util.logging.CQSPLogger;
import ConquerSpace.util.ResourceLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import jdk.internal.net.http.common.Pair;
import org.apache.logging.log4j.Logger;
import org.hjson.JsonValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class AssetReader {

    private static final Logger LOGGER = CQSPLogger.getLogger(AssetReader.class.getName());

    //This is kinda hacked together, but it's not too fancy
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> readHjsonFromDirInArray(String dir, Class<T> x, PassThingy thing) {
        ArrayList<T> elements = new ArrayList<>();
        //Get the launch systems folder
        File launchSystemsFolder = ResourceLoader.getResourceByFile(dir);
        File[] files = launchSystemsFolder.listFiles();
        for (File f : files) {
            FileInputStream fis = null;
            try {
                //If it is readme, continue
                if (f.getName().endsWith("readme.txt")) {
                    continue;
                }
                fis = new FileInputStream(f);
                byte[] data = new byte[(int) f.length()];
                fis.read(data);
                fis.close();
                String text = new String(data);
                text = JsonValue.readHjson(text).toString();
                JSONArray root = new JSONArray(text);
                for (int i = 0; i < root.length(); i++) {
                    try {
                        JSONObject obj = root.getJSONObject(i);
                        elements.add((T) thing.thingy(obj));
                    } catch (ClassCastException e) {
                        LOGGER.error("CCE!", e);
                    } catch (JSONException exception) {
                        LOGGER.error("JSONException!", exception);
                    } catch (IllegalArgumentException ile) {
                        LOGGER.error("IllegalArgumentException!", ile);
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
        return elements;
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

    //This one is a little different...
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
            LOGGER.warn("Cannot open ship types", ex);
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

    public static void readBuildingCosts() {
        BuildingCostGetter.initializeBuildingCosts();
    }

    public static Object processElement(JSONObject obj) {
        int id = obj.getInt("id");
        String name = obj.getString("name");
        double density = obj.getDouble("density");
        Element e = new Element(name, id, 1d, density);
        return e;
    }

    public static Object processEngineTech(JSONObject obj) {
        String name = obj.getString("name");
        int id = obj.getInt("id");
        float efficiency = obj.getFloat("efficiency");
        float power = obj.getFloat("thrust_multiplier");
        EngineTechnology tech = new EngineTechnology(name, efficiency, power);
        tech.setId(id);
        return tech;
    }

    public static Object processLaunchSystem(JSONObject obj) {
        String name = obj.getString("name");

        String techName = obj.getString("tech").split(":")[0];
        //The tech id will be the second value.
        int id = Integer.parseInt(obj.getString("tech").split(":")[1]);

        int size = obj.getInt("size");

        int safety = obj.getInt("safety");

        int cost = obj.getInt("cost");

        int constructCost = obj.getInt("construct-cost");

        boolean reusable = obj.getBoolean("reusable");

        int reuseCost = 0;
        if (reusable) {
            //Get Reusable cost
            reuseCost = obj.getInt("reuse cost");
        }

        int maxCargo = obj.getInt("cargo");

        if (reusable) {
            return new LaunchSystem(name, Technologies.getTechByID(id), size, safety, cost, constructCost, reuseCost, maxCargo);
        } else {
            return new LaunchSystem(name, Technologies.getTechByID(id), size, safety, cost, constructCost, maxCargo);
        }
    }

    public static Object processResource(JSONObject obj) {
        String name = obj.getString("name");

        //The tech id will be the second value.
        int id = obj.getInt("id");

        float rarity = obj.getFloat("rarity");

        int value = obj.getInt("value");

        float density = obj.getFloat("density");

        int difficulty = obj.getInt("difficulty");

        JSONArray distribution = obj.getJSONArray("distribution");

        JSONArray color = obj.getJSONArray("color");

        boolean mineable = obj.getBoolean("mineable");

        JSONArray tags = obj.getJSONArray("tags");

        JSONObject attributes = obj.getJSONObject("attributes");

        Resource res = new Resource(name, value, rarity, id);
        res.setDensity(density);
        res.setDifficulty(difficulty);
        res.setMineable(mineable);
        res.setDistributionLow(distribution.getInt(0));
        res.setDistributionHigh(distribution.getInt(1));
        res.setColor(color.getInt(0), color.getInt(1), color.getInt(2));
        //res.setTags(tags.);
        String[] list = new String[tags.length()];
        for (int n = 0; n < tags.length(); n++) {
            list[n] = tags.getString(n);
        }
        res.setTags(list);

        //Attributes
        Iterator it = attributes.keys();
        while (it.hasNext()) {
            //Process it
            String label = (String) it.next();
            Integer number = (Integer) attributes.get(label);
            res.getAttributes().put(label, number);
        }

        if (name.equals("food")) {
            GameController.foodResource = res;
        }
        return res;
    }

    public static Object processSatellite(JSONObject obj) {
        //Literally the easiest.
        return obj;
    }

    public static Object processPersonalityTraits(JSONObject obj) {
        String name = obj.getString("name");
        PersonalityTrait trait = new PersonalityTrait();
        trait.setName(name);
        return trait;
    }

    public static Object processGood(JSONObject obj) {
        return new NonElement("sadf", 0, 12, 4);
    }

    public static Object processOre(JSONObject obj) {
        String name = obj.getString("name");
        double density = obj.getDouble("density");
        Ore element = new Ore(name, 0, 1, density);
        //Process formula
        obj.getJSONArray("formula");

        //Process distribution
        JSONArray dist = obj.getJSONArray("distribution");
        int distributionLow = dist.getInt(0);
        int distributionHigh = dist.getInt(1);
        JSONArray depth = obj.getJSONArray("depth");
        int depthLow = depth.getInt(0);
        int depthHigh = depth.getInt(1);
        double rarity = obj.getDouble("rarity");
        int abundance = obj.getInt("abundance");
        int resourceDistDensity = obj.getInt("dist-density");

        element.dist.distributionLow = distributionLow;
        element.dist.distributionHigh = distributionHigh;
        element.dist.depthLow = depthLow;
        element.dist.depthHigh = depthHigh;
        element.dist.rarity = rarity;
        element.dist.abundance = abundance;
        element.dist.density = resourceDistDensity;

        return (element);
    }

    //Lol the name
    public static Object processProcess(JSONObject obj) {
        String name = obj.getString("name");

        HashMap<Good, Integer> input = new HashMap<>();

        JSONArray inputArray = obj.getJSONArray("input");

        for (int i = 0; i < inputArray.length(); i++) {
            String s = inputArray.getString(i);
            String[] content = s.split(":");

            Good toFind = null;
            //Find good
            for (int k = 0; k < GameController.allGoods.size(); k++) {
                Good g = GameController.allGoods.get(k);
                if (g.getName().equals(content[0])) {
                    toFind = g;
                    break;
                }
            }

            if (toFind != null) {
                //Parse things
                int value = Integer.parseInt(content[1]);
                input.put(toFind, value);
            }
        }

        HashMap<Good, Integer> output = new HashMap<>();

        JSONArray outputArray = obj.getJSONArray("output");

        for (int i = 0; i < outputArray.length(); i++) {
            String s = outputArray.getString(i);
            String[] content = s.split(":");

            Good toFind = null;
            //Find good
            for (int k = 0; k < GameController.allGoods.size(); k++) {
                Good g = GameController.allGoods.get(k);
                System.out.println(content[0]);
                if (g.getName().equals(content[0])) {
                    toFind = g;
                    break;
                }
            }

            if (toFind != null) {
                //Parse things
                int value = Integer.parseInt(content[1]);
                output.put(toFind, value);
            }
        }

        int diff = obj.getInt("diff");

        ProductionProcess process = new ProductionProcess();
        process.name = name;
        process.input = input;
        process.output = output;
        process.diff = diff;

        return process;
    }
}

//Something to pass the parameters
interface PassThingy {

    public Object thingy(JSONObject obj);
}
