package ConquerSpace.game.buildings;

import ConquerSpace.game.population.Job;
import ConquerSpace.game.population.Workable;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.resources.Resource;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author zyunl
 */
public class BuildingBuilding extends Building implements Workable{

    private Building toBuild;
    private GeographicPoint pt;
    private int length;
    private int scale = 1;
    //Set the resources needed to build over time
    public HashMap<Resource, Integer> resourcesNeeded;
    private int cost;

    public BuildingBuilding(Building toBuild, GeographicPoint pt, int length) {
        this.toBuild = toBuild;
        this.pt = pt;
        this.length = length;
        resourcesNeeded = new HashMap<>();
    }

    public void setToBuild(Building toBuild) {
        this.toBuild = toBuild;
    }

    public GeographicPoint getPt() {
        return pt;
    }

    public Building getToBuild() {
        return toBuild;
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    public void setPt(GeographicPoint pt) {
        this.pt = pt;
    }

    public int getLength() {
        return length;
    }

    public void decrementLength(int amount) {
        length -= amount;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

    @Override
    public void processJob(Job j) {
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
