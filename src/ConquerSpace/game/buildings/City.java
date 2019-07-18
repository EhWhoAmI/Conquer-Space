package ConquerSpace.game.buildings;

import java.util.ArrayList;

/**
 *
 * @author zyunl
 */
public class City {

    private String name;
    public ArrayList<PopulationStorage> storages;

    //% to completing a unit
    private float populationUnitPercentage = 0;

    public City() {
        storages = new ArrayList<>();
    }

    public void setPopulationUnitPercentage(float populationUnitPercentage) {
        this.populationUnitPercentage = populationUnitPercentage;
    }

    public float getPopulationUnitPercentage() {
        return populationUnitPercentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
