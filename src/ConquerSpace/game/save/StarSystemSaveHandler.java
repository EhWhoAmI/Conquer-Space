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

import ConquerSpace.game.universe.bodies.StarSystem;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class StarSystemSaveHandler {

    private StarSystem system;

    public StarSystemSaveHandler(StarSystem system) {
        this.system = system;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();

        obj.put("id", system.getId());
        obj.put("deg", system.getGalaticLocation().getDegrees());
        obj.put("dist", system.getGalaticLocation().getDistance());
        obj.put("planet-c", system.getPlanetCount());

        //Now stuff everything into that
        ArrayList<JSONObject> planets = new ArrayList<>();
//        for (int p = 0; p < system.getPlanetCount(); p++) {
//            Planet planet = system.getPlanet(p);
//
//            JSONObject object = new JSONObject();
//            object.put("type", planet.getPlanetType());
//            object.put("size", planet.getPlanetSize());
//            object.put("seed", planet.getTerrainSeed());
//            object.put("name", planet.getName());
//
//            //Buildings
//            ArrayList<JSONObject> buildings = new ArrayList<>();
//            for (Map.Entry<GeographicPoint, Building> entry : planet.buildings.entrySet()) {
//                GeographicPoint key = entry.getKey();
//                Building value = entry.getValue();
//                BuildingSaveHandler handler = new BuildingSaveHandler(key, value);
//                JSONObject building = handler.getJSONObject();
//                buildings.add(building);
//            }
//            object.put("ings", buildings);
//
//            //Resource veins
//            ArrayList<JSONObject> veins = new ArrayList<>();
//            for (Stratum v : planet.strata) {
//                JSONObject veinObject = new JSONObject();
////                veinObject.put("id", v.getId());
////                veinObject.put("diff", v.getDifficulty());
////                veinObject.put("radius", v.getRadius());
////                veinObject.put("x", v.getX());
////                veinObject.put("y", v.getY());
////                veinObject.put("res", v.getResourceType().getId());
////                veinObject.put("amt", v.getResourceAmount());
//                veins.add(veinObject);
//            }
//            object.put("veins", veins);
//
//            //Satellites
//            ArrayList<JSONObject> satellites = new ArrayList<>();
//            for (Orbitable b : planet.getSatellites()) {
//                JSONObject orbitable = new JSONObject();
//
//                if (b instanceof Satellite) {
//                    orbitable.put("type", "satellite");
//                    Satellite ship = (Satellite) b;
//                    SatelliteSaveHandler handler = new SatelliteSaveHandler(ship);
//                    orbitable.put("object", handler.getJSONObject());
//                } else if (b instanceof Ship) {
//                    orbitable.put("type", "ship");
//                    Ship ship = (Ship) b;
//                    ShipSaveHandler handler = new ShipSaveHandler(ship);
//                    orbitable.put("object", handler.getJSONObject());
//                }
//
//                veins.add(orbitable);
//            }
//            object.put("satellites", satellites);
//            planets.add(object);
//        }
        return obj;
    }
}
