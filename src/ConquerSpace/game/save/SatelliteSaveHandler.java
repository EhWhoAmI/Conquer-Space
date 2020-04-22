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

import ConquerSpace.game.ships.satellites.NoneSatellite;
import ConquerSpace.game.ships.satellites.Satellite;
import ConquerSpace.game.ships.satellites.SpaceTelescope;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class SatelliteSaveHandler {
    public Satellite satellite;
    public SatelliteSaveHandler(Satellite satellite) {
        this.satellite = satellite;
    }
   
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        //get type
        //Get info...
        object.put("mass", satellite.getMass());
        object.put("orbiting", satellite.getOrbiting().toString());
        object.put("owner", satellite.getOwner());
        if (satellite instanceof NoneSatellite) {
            object.put("type", "none");
        } else if (satellite instanceof SpaceTelescope) {
            SpaceTelescope telescope = (SpaceTelescope) satellite;
            object.put("type", "telescope");
            object.put("range", telescope.getRange());
        }
        return object;
    }
}
