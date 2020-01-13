package ConquerSpace.game.save;

import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.hull.Hull;
import org.json.JSONObject;

/**
 *
 * @author zyunl
 */
public class ShipSaveHandler {
    private Ship what;
    
    public ShipSaveHandler(Ship ship) {
        what = ship;
    }
    
    public JSONObject getJSONObject() {
        //Process ship
        JSONObject object = new JSONObject();
        object.put("name", what.getName());
        object.put("id", what.getId());
        object.put("class", what.getShipClass());
        object.put("x", what.getX());
        object.put("y", what.getY());
        if (what.isOrbiting()) {
            object.put("orbiting", what.getOrbiting());
        }
        object.put("going-x", what.getGoingToX());
        object.put("going-y", what.getGoingToY());
        object.put("throttle", what.getThrottle());
        object.put("thrust", what.getEstimatedThrust());
        object.put("location", what.getLocation().toString());
        object.put("mass", what.getMass());

        Hull h = what.getHull();

        //Hull info
        object.put("hull-mass", h.getMass());
        object.put("hull-material", h.getMaterial().getId());
        object.put("hull-space", h.getSpace());
        object.put("hull-type", h.getShipType());
        object.put("hull-thrust", h.getThrust());
        object.put("hull-strength", h.getStrength());
        return object;
    }
}
