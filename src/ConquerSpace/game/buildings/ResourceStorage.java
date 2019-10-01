package ConquerSpace.game.buildings;

import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import ConquerSpace.game.universe.spaceObjects.Planet;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author zyunl
 */
public class ResourceStorage extends Building implements ResourceStockpile {

    private int upkeep;

    private HashMap<Resource, Integer> resources;

    private int system;
    private int planet;

    public ResourceStorage(Planet parent) {
        resources = new HashMap<>();
        upkeep = 0;
        planet = parent.getId();
        system = parent.getParentStarSystem();
    }

    @Override
    public void addResourceTypeStore(Resource type) {
        resources.put(type, 0);
    }

    public boolean getHasResource(int type) {
        return resources.containsKey(type);
    }

    @Override
    public int getResourceAmount(Resource type) {
        return resources.get(type);
    }

    @Override
    public void addResource(Resource type, int amount) {
        resources.put(type, resources.get(type) + amount);
    }

    public void setUpkeep(int upkeep) {
        this.upkeep = upkeep;
    }

    public int getUpkeep() {
        return upkeep;
    }

    @Override
    public UniversePath getUniversePath() {
        return new UniversePath(system, planet);
    }

    @Override
    public boolean canStore(Resource type) {
        return (resources.containsKey(type));
    }

    @Override
    public Resource[] storedTypes() {
        Iterator<Resource> res = resources.keySet().iterator();
        Resource[] arr = new Resource[resources.size()];
        int i = 0;
        while (res.hasNext()) {
            Resource next = res.next();
            arr[i] = next;
            i++;
        }
        return arr;
    }

    @Override
    public Color getColor() {
        return Color.CYAN;
    }

    @Override
    public boolean removeResource(Resource type, int amount) {
        //Get the amount in the place
        int currentlyStored = resources.get(type);
        if(amount > currentlyStored)
            return false;
        
        resources.put(type, currentlyStored-amount);
        return true;
    }
}
