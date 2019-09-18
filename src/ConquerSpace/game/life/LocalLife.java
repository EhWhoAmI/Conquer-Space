package ConquerSpace.game.life;

import java.util.ArrayList;

/**
 * Life atop a planet.
 *
 * @author zyunl
 */
public class LocalLife {

    private int biomass = 0;
    //Increase of biomass per year under ideal conditions -- enough resources
    private float reproductionRate = 0;
    public ArrayList<LifeTrait> lifetraits;

    private String name = "";

    public LocalLife(float reproductionRate) {
        this.reproductionRate = reproductionRate;
        lifetraits = new ArrayList<>();
    }

    public void setBiomass(int biomass) {
        this.biomass = biomass;
    }

    public int getBiomass() {
        return biomass;
    }

    public void setReproductionRate(float reproductionRate) {
        this.reproductionRate = reproductionRate;
    }

    public float getReproductionRate() {
        return reproductionRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
