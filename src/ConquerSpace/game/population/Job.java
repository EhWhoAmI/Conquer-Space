package ConquerSpace.game.population;

import ConquerSpace.game.universe.resources.Resource;
import java.util.HashMap;

/**
 * Job of the population
 * @author zyunl
 */
public class Job {
    /**
     * The resources to add...
     */
    public HashMap<Resource, Integer> resources;

    private JobType jobType;
    private JobRank jobRank = JobRank.Low;
    
    public Job(JobType jobType) {
        this.jobType = jobType;
        resources = new HashMap<>();
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType name) {
        this.jobType = name;
    }

    @Override
    public String toString() {
        return jobType.getName();
    }

    public void setJobRank(JobRank jobRank) {
        this.jobRank = jobRank;
    }

    public JobRank getJobRank() {
        return jobRank;
    }
}
