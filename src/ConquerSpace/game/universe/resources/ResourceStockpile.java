package ConquerSpace.game.universe.resources;

import ConquerSpace.game.universe.UniversePath;

/**
 *
 * @author Zyun
 */
public interface ResourceStockpile {
    public void addResourceTypeStore(Resource type);
    public int getResourceAmount(Resource type);
    public void addResource(Resource type, int amount);
    //Describe position
    public UniversePath getUniversePath();
    public boolean canStore(Resource type);
    
    public Resource[] storedTypes();
}
