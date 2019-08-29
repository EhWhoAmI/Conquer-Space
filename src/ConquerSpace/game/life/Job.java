package ConquerSpace.game.life;

import java.util.HashMap;

/**
 * Job of the population
 * @author zyunl
 */
public class Job {
    /**
     * The resources to add...
     */
    public HashMap<Integer, Integer> resources;

    private String name;
    
    public Job(String name) {
        this.name = name;
        resources = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
