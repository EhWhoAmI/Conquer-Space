package ConquerSpace.game.population;

import ConquerSpace.game.jobs.Job;
import ConquerSpace.game.jobs.JobType;

/**
 * one pop unit is about 10 million-ish.
 *
 * @author zyunl
 */
public class PopulationUnit {

    public byte happiness;
    public Race species;
    public Job job;

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
