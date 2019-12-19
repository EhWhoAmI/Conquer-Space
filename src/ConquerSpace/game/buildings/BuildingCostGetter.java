package ConquerSpace.game.buildings;

import ConquerSpace.game.GameController;
import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.util.ResourceLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author zyunl
 */
public class BuildingCostGetter {

    private static HashMap<String, BuildingCost> buildingCosts = new HashMap<>();

    public static void initializeBuildingCosts() {
        //Get folder
        File costDir = ResourceLoader.getResourceByFile("dirs.construction.costs");
        //Read info
        for (File f : costDir.listFiles()) {
            FileInputStream fis = null;

            try {
                if (!f.getName().endsWith(".json")) {
                    continue;
                }   //Read, there is only one object
                fis = new FileInputStream(f);
                byte[] data = new byte[(int) f.length()];
                fis.read(data);
                fis.close();
                String text = new String(data);
                JSONObject object = new JSONObject(text);
                Iterator<String> name = object.keys();
                while (name.hasNext()) {
                    String next = name.next();
                    JSONObject overallcost = object.getJSONObject(next);
                    //Create new object
                    JSONObject resources = overallcost.getJSONObject("base-cost");

                    Iterator<String> resourceNames = resources.keys();
                    BuildingCost buildingCost = new BuildingCost();
                    while (resourceNames.hasNext()) {
                        //Find resources
                        String resourceName = resourceNames.next();
                        //Search for resource
                        for (Resource res : GameController.resources) {
                            if (res.getName().toLowerCase().equals(resourceName.toLowerCase())) {
                                //Set the value

                                buildingCost.cost.put(res, resources.getInt(resourceName));
                            }
                        }
                    }
                    buildingCosts.put(next, buildingCost);
                }
            } catch (FileNotFoundException ex) {

            } catch (IOException ex) {

            }
        }
    }

    public static BuildingCost getCost(Building building, Civilization c) {
        //Return cost
        //Get the thing...
        BuildingCost actualCost = new BuildingCost();
        //Search for it...
        BuildingCost theoreticalCost = buildingCosts.get(building.getType());
        //Compute the things...
        //
        return null;
    }

    public static BuildingCost getCost(String name, Civilization c) {
        //Return cost
        BuildingCost actualCost = new BuildingCost();

        BuildingCost theoreticalCost = buildingCosts.get(name);
        //Compute the things
        if (theoreticalCost != null) {
            //Loop through theoretical cost
            for (Map.Entry<Resource, Integer> entry : theoreticalCost.cost.entrySet()) {
                Resource key = entry.getKey();
                Integer value = entry.getValue();

                String resName = name + "." + key.getName();
                Double multiplier = c.multipliers.get(resName);
                if (multiplier != null) {
                    //Then multiply it
                    value = (int) (value * multiplier);
                }
                actualCost.cost.put(key, value);
            }

        }
        return actualCost;
    }
}
