package ConquerSpace.game.universe.resources;

import ConquerSpace.game.universe.UniversePath;

/**
 *
 * @author Zyun
 */
public interface ResourceStockpile {
    public void addResourceTypeStore(int type);
    public int getResourceAmount(int type);
    public void addResource(int type, int amount);
    //Describe position
    public UniversePath getUniversePath();
    public boolean canStore(int type);
    
    public int[] storedTypes();
}
