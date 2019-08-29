package ConquerSpace.game.population;

import ConquerSpace.game.life.Job;
import ConquerSpace.game.life.Species;

/**
 * one pop unit is about 10 million-ish.
 *
 * @author zyunl
 */
public class PopulationUnit {

    private byte happiness;
    private Species species;
    private Job job;

    public PopulationUnit(Species species) {
        job = new Job("Jobless");
        this.species = species;
    }
    
    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public byte getHappiness() {
        return happiness;
    }

    public void setHappiness(byte happiness) {
        this.happiness = happiness;
    }
    
    public Job getJob() {
        return job;
    }
}
