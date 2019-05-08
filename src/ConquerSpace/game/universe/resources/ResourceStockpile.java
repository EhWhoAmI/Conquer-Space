package ConquerSpace.game.universe.resources;

/**
 *
 * @author Zyun
 */
public interface ResourceStockpile {
    public void addResourceTypeStore(int type);
    public int getResourceAmount(int type);
    public void addResource(int type, int amount);
}
