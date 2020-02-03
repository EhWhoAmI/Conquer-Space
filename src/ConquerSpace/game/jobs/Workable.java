package ConquerSpace.game.jobs;

import ConquerSpace.game.jobs.Job;

/**
 *
 * @author zyunl
 */
public interface Workable {
    public Job[] jobsNeeded();
    public void processJob(Job j);
}
