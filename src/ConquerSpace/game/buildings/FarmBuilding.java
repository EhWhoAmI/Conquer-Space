package ConquerSpace.game.buildings;

/**
 * Farms have 2 types, a crop one and a livestock one, maybe a combined one. We'll see.
 * @author zyunl
 */
public class FarmBuilding extends Building {
    private FarmType farmType;
    private int productivity = 0;
    public FarmBuilding(FarmType ft) {
        farmType = ft;
    }
    
    public static enum FarmType {
        Livestock, 
        Crop;
    }
}
