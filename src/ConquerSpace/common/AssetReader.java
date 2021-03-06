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
package ConquerSpace.common;

import ConquerSpace.common.game.characters.PersonalityTrait;
import ConquerSpace.common.game.resources.Element;
import ConquerSpace.common.game.resources.Good;
import ConquerSpace.common.game.resources.NonElement;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.ResourceDistribution;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.science.Technologies;
import ConquerSpace.common.game.ships.EngineTechnology;
import ConquerSpace.common.game.ships.LaunchSystem;
import ConquerSpace.common.game.ships.ShipType;
import ConquerSpace.common.util.ResourceLoader;
import ConquerSpace.common.util.logging.CQSPLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.hjson.JsonValue;
import org.hjson.ParseException;
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
    public static <T> ArrayList<T> readHjsonFromDirInArray(String dir, Class<T> x, AssetPasser assetReader) {
        ArrayList<T> elements = new ArrayList<>();
        //Get the launch systems folder
        File resourceFolder = ResourceLoader.getResourceByFile(dir);
        File[] files = resourceFolder.listFiles();
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
                        elements.add((T) assetReader.parseJSONObject(obj));
                    } catch (ClassCastException e) {
                        LOGGER.error("CCE while reading file" + f.getAbsolutePath(), e);
                    } catch (JSONException exception) {
                        LOGGER.error("JSONException while reading file" + f.getAbsolutePath(), exception);
                    } catch (IllegalArgumentException ile) {
                        LOGGER.error("IllegalArgumentException while reading file" + f.getAbsolutePath(), ile);
                    }
                }
            } catch (FileNotFoundException ex) {
                LOGGER.error("File not found while reading file" + f.getAbsolutePath(), ex);
            } catch (IOException ex) {
                LOGGER.error("IO exception while reading file " + f.getAbsolutePath(), ex);
            } catch (JSONException ex) {
                LOGGER.warn("JSON EXCEPTION while reading file " + f.getAbsolutePath(), ex);
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

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> readHjsonFromDirInArray(String dir, Class<T> x, GameState state, AssetPasserWithGameState assetReader) {
        ArrayList<T> elements = new ArrayList<>();
        //Get the launch systems folder
        File resourceFolder = ResourceLoader.getResourceByFile(dir);
        File[] files = resourceFolder.listFiles();
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
                        elements.add((T) assetReader.parseJSONObject(obj, state));
                    } catch (ClassCastException e) {
                        LOGGER.error("CCE while reading file" + f.getAbsolutePath(), e);
                    } catch (JSONException exception) {
                        LOGGER.error("JSONException while reading file" + f.getAbsolutePath(), exception);
                    } catch (IllegalArgumentException ile) {
                        LOGGER.error("IllegalArgumentException while reading file" + f.getAbsolutePath(), ile);
                    }
                }
            } catch (FileNotFoundException ex) {
                LOGGER.error("File not found while reading file" + f.getAbsolutePath(), ex);
            } catch (IOException ex) {
                LOGGER.error("IO exception while reading file " + f.getAbsolutePath(), ex);
            } catch (JSONException ex) {
                LOGGER.warn("JSON EXCEPTION while reading file " + f.getAbsolutePath(), ex);
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

    public static Object processShipType(JSONObject obj) {
        String identifier = obj.getString("identifier");
        JSONArray tags = obj.getJSONArray("tags");
        ShipType shipType = new ShipType();
        shipType.setIdentifier(identifier);
        ArrayList<String> tagsArrayList = new ArrayList<>();
        for (int i = 0; i < tags.length(); i++) {
            tagsArrayList.add(tags.getString(i));
        }
        shipType.setTags(Arrays.copyOf(tagsArrayList.toArray(), tagsArrayList.size(), String[].class));

        return shipType;
    }

    public static Object processElement(JSONObject obj) {
        //Because periodic table number is the id
        int id = obj.getInt("number");
        String name = obj.getString("name");
        Object densityT = obj.get("density");
        //if null, put 0
        double density = 0;
        if (densityT instanceof Double) {
            density = (Double) densityT * 1000d;
        }
        Element e = new Element(name, 1d, density);
        e.setElementNumber(id);
        //Set tags
        e.tags = new String[0];
        return e;
    }

    public static Object processEngineTech(JSONObject obj, GameState state) {
        String name = obj.getString("name");
        String identifier = obj.getString("identifier");
        float efficiency = obj.getFloat("efficiency");
        float power = obj.getFloat("thrust_multiplier");
        EngineTechnology tech = new EngineTechnology(state, name, efficiency, power);
        tech.setIdentifier(identifier);
        return tech;
    }

    public static Object processLaunchSystem(JSONObject obj, GameState state) {
        String name = obj.getString("name");

        //String techName = obj.getString("tech").split(":")[0];
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
            return new LaunchSystem(state, name, Technologies.getTechByID(state, id), size, safety, cost, constructCost, reuseCost, maxCargo);
        } else {
            return new LaunchSystem(state, name, Technologies.getTechByID(state, id), size, safety, cost, constructCost, maxCargo);
        }
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

    //Lol the name
    public static Object processProcess(JSONObject obj, GameState state) {
        String name = obj.getString("name");
        String identifier = obj.getString("identifier");

        HashMap<StoreableReference, Double> input = new HashMap<>();

        JSONArray inputArray = obj.getJSONArray("input");

        for (int i = 0; i < inputArray.length(); i++) {
            String s = inputArray.getString(i);
            String[] content = s.split(":");

            StoreableReference resourceId = state.getGoodId(content[0]);

            if (resourceId != null) {
                //Parse things
                Double value = Double.parseDouble(content[1]);
                input.put(resourceId, value);
            }
        }

        HashMap<StoreableReference, Double> output = new HashMap<>();

        JSONArray outputArray = obj.getJSONArray("output");

        for (int i = 0; i < outputArray.length(); i++) {
            String s = outputArray.getString(i);
            String[] content = s.split(":");

            StoreableReference resourceId = state.getGoodId(content[0]);
            if (resourceId != null) {
                //Parse things
                Double value = Double.parseDouble(content[1]);
                output.put(resourceId, value);
            }
        }

        int diff = obj.getInt("diff");

        //Tags
        JSONArray tagsArray = obj.getJSONArray("tags");

        String[] tags = new String[tagsArray.length()];

        for (int i = 0; i < tagsArray.length(); i++) {
            String s = tagsArray.getString(i);
            tags[i] = s;
        }

        ProductionProcess process = new ProductionProcess(state);
        process.name = name;
        process.identifier = identifier;
        process.getInput().putAll(input);
        process.getOutput().putAll(output);
        process.difficulty = diff;
        process.tags = tags;
        return process;
    }

    public static Object processDistributions(JSONObject obj) {
        ResourceDistribution distribution = new ResourceDistribution();
        //Process distribution
        String name = obj.getString("name");
        JSONArray dist = obj.getJSONArray("distribution");
        int distributionLow = dist.getInt(0);
        int distributionHigh = dist.getInt(1);
        JSONArray depth = obj.getJSONArray("depth");
        int depthLow = depth.getInt(0);
        int depthHigh = depth.getInt(1);
        double rarity = obj.getDouble("rarity");
        int abundance = obj.getInt("abundance");
        int resourceDistDensity = obj.getInt("dist-density");

        distribution.resourceName = name;
        distribution.distributionLow = distributionLow;
        distribution.distributionHigh = distributionHigh;
        distribution.depthLow = depthLow;
        distribution.depthHigh = depthHigh;
        distribution.rarity = rarity;
        distribution.abundance = abundance;
        distribution.density = resourceDistDensity;
        return distribution;
    }

    /**
     * We need a separate function for reading goods, because you need to iterate through it twice.
     */
    public static void processGoods(GameState state) {
        ArrayList<Good> goods = new ArrayList<>();
        HashMap<String, JSONObject> recipieInfo = new HashMap<>();

        File launchSystemsFolder = ResourceLoader.getResourceByFile("dirs.goods");
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
                    JSONObject obj = root.getJSONObject(i);
                    String name = obj.getString("name");
                    String identifier = obj.getString("identifier");
                    double volume = obj.getDouble("volume");
                    double mass = obj.getDouble("mass");
                    NonElement goodElement = new NonElement(name, identifier, volume, mass);

                    //Get tags
                    Object[] tagObjects = obj.getJSONArray("tags").toList().toArray();
                    goodElement.tags = Arrays.copyOf(tagObjects, tagObjects.length, String[].class);

                    goods.add(goodElement);
                    recipieInfo.put(identifier, obj);

                    state.addGood(goodElement);
                }
            } catch (FileNotFoundException ex) {
                LOGGER.error("File not found: " + f.getAbsolutePath() + "!", ex);
            } catch (IOException ex) {
                LOGGER.error("IO exception: " + f.getAbsolutePath() + "!", ex);
            } catch (JSONException ex) {
                LOGGER.warn("JSON EXCEPTION: " + f.getAbsolutePath() + "!", ex);
            } catch (ParseException ex) {
                LOGGER.warn("HJSON EXCEPTION: " + f.getAbsolutePath() + "!", ex);
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

        //Resolve references
        for (int i = 0; i < goods.size(); i++) {
            try {
                Good g = goods.get(i);
                if (g instanceof NonElement) {
                    NonElement resource = (NonElement) g;
                    JSONArray recipie = recipieInfo.get(resource.getIdentifier()).getJSONArray("formula");
                    for (int l = 0; l < recipie.length(); l++) {
                        String rec = recipie.getString(l);
                        String[] text = rec.split(":");

                        String goodIdentifier = text[0];
                        String amount = text[1];

                        double goodAmount = Double.parseDouble(amount);
                        StoreableReference goodId = state.getGoodId(goodIdentifier);
                        resource.recipie.put(goodId, goodAmount);
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("EXCEPTION: " + goods.get(i) + "!", e);
            }
        }
    }

    public static interface AssetPasser {

        public Object parseJSONObject(JSONObject obj);
    }

    public static interface AssetPasserWithGameState {

        public Object parseJSONObject(JSONObject obj, GameState gameState);
    }
}
