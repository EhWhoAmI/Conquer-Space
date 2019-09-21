package ConquerSpace.game.buildings;

import ConquerSpace.game.life.LocalLife;
import java.util.ArrayList;

/**
 * Farms have 2 types, a crop one and a livestock one, maybe a combined one. We'll see.
 * @author zyunl
 */
public class FarmBuilding extends Building {
    private FarmType farmType;
    public ArrayList<LocalLife> farmCreatures;
    private int productivity = 0;
    
    public FarmBuilding(FarmType ft) {
        farmType = ft;
        farmCreatures = new ArrayList<>();
    }
    
    public static enum FarmType {
        Livestock, 
        Crop;
    }

    public int getProductivity() {
        return productivity;
    }

    public void setProductivity(int productivity) {
        this.productivity = productivity;
    }

    public FarmType getFarmType() {
        return farmType;
    }

    public void setFarmType(FarmType farmType) {
        this.farmType = farmType;
    }
}
