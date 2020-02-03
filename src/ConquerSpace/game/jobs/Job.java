package ConquerSpace.game.jobs;

import ConquerSpace.game.economy.Currency;
import ConquerSpace.game.universe.resources.Resource;
import java.util.HashMap;

/**
 * Job of the population
 *
 * @author zyunl
 */
public class Job {

    /**
     * The resources to add...
     */
    public HashMap<Resource, Integer> resources;

    private JobType jobType;
    private JobRank jobRank = JobRank.Low;
    private Workable workingFor;
    private Currency currency;
    private Employer employer;
    private int pay;

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

    public Workable getWorkingFor() {
        return workingFor;
    }

    public void setWorkingFor(Workable workingFor) {
        this.workingFor = workingFor;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public int getPay() {
        return pay;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Employer getEmployer() {
        return employer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Job) {
            //Just check job name
            return (((Job) obj).jobType == jobType);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return jobType.hashCode();
    }
}
