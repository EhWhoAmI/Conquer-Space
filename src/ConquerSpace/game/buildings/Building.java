package ConquerSpace.game.buildings;

import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.population.Employer;
import ConquerSpace.game.population.Job;
import java.awt.Color;
import java.util.ArrayList;

/**
 * A building is defined as a series of points
 *
 * @author zyunl
 */
public abstract class Building {
    private Color color;
    public ArrayList<Area> areas;
    public ArrayList<Job> jobs;
    private Employer owner;
    private String type;
    private int energyUsage;
    public ArrayList<InfrastructureBuilding> infrastructure;

    public Building() {
        areas = new ArrayList<>();
        jobs = new ArrayList<>();
        infrastructure = new ArrayList<>();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setOwner(Employer owner) {
        this.owner = owner;
    }

    public Employer getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    public int getEnergyUsage() {
        return energyUsage;
    }

    public void setEnergyUsage(int energyUsage) {
        this.energyUsage = energyUsage;
    }
}
