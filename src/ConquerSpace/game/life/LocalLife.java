package ConquerSpace.game.life;

/**
 * Life on a planet.
 *
 * @author zyunl
 */
public class LocalLife {

    private int biomass = 0;
    
    private Species species;

    public LocalLife() {
    }   

    public void setBiomass(int biomass) {
        this.biomass = biomass;
    }

    public int getBiomass() {
        return biomass;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Species getSpecies() {
        return species;
    }

    @Override
    public String toString() {
        return species.getName();
    }
}
