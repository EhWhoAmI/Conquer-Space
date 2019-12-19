package ConquerSpace.game.buildings;

import ConquerSpace.game.universe.resources.Resource;
import java.util.HashMap;

/**
 *
 * @author zyunl
 */
public class BuildingCost {
    public HashMap<Resource, Integer> cost;
    public BuildingCost() {
        cost = new HashMap<>();
    }
}
