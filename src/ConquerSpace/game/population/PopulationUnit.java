package ConquerSpace.game.population;

/**
 * one pop unit is about 10 million-ish.
 *
 * @author zyunl
 */
public class PopulationUnit {

    private byte happiness;
    private Race species;
    private Job job;

    public PopulationUnit(Race species) {
        job = new Job(JobType.Jobless);
        this.species = species;
    }
    
    public Race getSpecies() {
        return species;
    }

    public void setSpecies(Race species) {
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

    public void setJob(Job job) {
        this.job = job;
    }
}
