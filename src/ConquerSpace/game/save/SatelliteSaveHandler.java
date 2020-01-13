package ConquerSpace.game.save;

import ConquerSpace.game.universe.ships.satellites.NoneSatellite;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.game.universe.ships.satellites.SpaceTelescope;
import org.json.JSONObject;

/**
 *
 * @author zyunl
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
