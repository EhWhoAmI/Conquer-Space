package ConquerSpace.game.jobs;

import ConquerSpace.game.buildings.City;
import java.util.ArrayList;

/**
 *
 * @author zyunl
 */
public class JobProcessor {
    private City city;
    public ArrayList<Workable> workables;

    public JobProcessor(City city) {
        this.city = city;
        workables = new ArrayList<>();
    }
    
    public void processJobs() {
       
    }
}
