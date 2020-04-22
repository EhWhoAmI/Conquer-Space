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
package ConquerSpace.game.ships.satellites;

import ConquerSpace.util.MultiplierProcessor;
import java.util.HashMap;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class Satellites {
    public static Satellite parseSatellite(JSONObject object, HashMap<String, Double> multipliers, HashMap<String, Integer> values) {
        //Parse
        Satellite satellite;
        String name = object.getString("name");
        int mass = object.getInt("mass");
        int distance = object.getInt("dist");
        String type = object.getString("type");
        int id = object.getInt("id");
        switch(getSatelliteTypeFromString(type)) {
            case SatelliteTypes.NONE:
                satellite = new NoneSatellite(distance, mass);
                satellite.setId(id);
                satellite.setName(name);
                break;
            case SatelliteTypes.TELESCOPE:
                satellite = new SpaceTelescope(distance, mass);
                satellite.setId(id);
                satellite.setName(name);
                //Range...
                //Get type
                int range = 0;
                if(object.get("range") instanceof Integer) {
                    range = object.getInt("range");
                } else {
                    //Get string
                    range = MultiplierProcessor.process(object.getString("range"), values, multipliers);
                }
                ((SpaceTelescope)satellite).setRange(range);
                break;
            default:
                //Just a none satellite then
                satellite = new NoneSatellite(0, 0);
                satellite.setId(-1);
                satellite.setName("Unknown satellite");
        }
        return satellite;
    }
    
    public static int getSatelliteTypeFromString(String str) {
        switch(str) {
            case "none":
                return SatelliteTypes.NONE;
            case "telescope":
                return SatelliteTypes.TELESCOPE;
            case "military":
                return SatelliteTypes.MILITARY;
            default:
                return -1;
        }
    }
}
