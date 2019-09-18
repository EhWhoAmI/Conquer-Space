package ConquerSpace.game.population;

/**
 *
 * @author zyunl
 */
public enum JobType {
    Miner("Miner"), Jobless("Jobless"), Administrator("Administrator");
    
    
    private final String name;

    JobType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

}
