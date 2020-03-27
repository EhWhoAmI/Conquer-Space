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
package ConquerSpace.game.save;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.ConstructingBuilding;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.buildings.Observatory;
import ConquerSpace.game.buildings.ResourceMinerDistrict;
import ConquerSpace.game.buildings.ResourceStorage;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class BuildingSaveHandler {
    GeographicPoint key;
    Building what;
    public BuildingSaveHandler(GeographicPoint key, Building what) {
        this.key = key;
        this.what = what;
    }
    
    public JSONObject getJSONObject() {
        JSONObject buildingObject = new JSONObject();
        buildingObject.put("x", key.getX());
        buildingObject.put("y", key.getY());

        //Get type...
        if (what instanceof ConstructingBuilding) {
            //Get type of building
            Building b = ((ConstructingBuilding) what).getToBuild();
            BuildingSaveHandler handler = new BuildingSaveHandler(key, what);
            JSONObject toBuild = handler.getJSONObject();
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
//            for (Resource res : storage.storedTypes()) {
//                JSONObject storedResource = new JSONObject();
//                storedResource.put("type", res.getId());
//                storedResource.put("amount", storage.getResourceAmount(res));
//                arr.put(storedResource);
//            }
            buildingObject.put("resources", arr);
        } else if (what instanceof ResourceMinerDistrict) {
            ResourceMinerDistrict gatherer = (ResourceMinerDistrict) what;
            //Get the stuff
            buildingObject.put("type", "gatherer");
            //buildingObject.put("resource", gatherer.getVeinMining().getId());
            buildingObject.put("amount", gatherer.getAmountMined());
            buildingObject.put("resource-type", gatherer.getResourceMining().getId());
        }
        return buildingObject;
    }
}
