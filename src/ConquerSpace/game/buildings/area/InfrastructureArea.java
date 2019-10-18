package ConquerSpace.game.buildings.area;

/**
 *
 * @author zyunl
 */
public class InfrastructureArea extends Area{
    //The jobs provided
    private int jobsProvided;
    private int effectiveness;

    public int getEffectiveness() {
        return effectiveness;
    }

    public void setEffectiveness(int effectiveness) {
        this.effectiveness = effectiveness;
    }

    public int getJobsProvided() {
        return jobsProvided;
    }

    public void setJobsProvided(int jobsProvided) {
        this.jobsProvided = jobsProvided;
    }
}
