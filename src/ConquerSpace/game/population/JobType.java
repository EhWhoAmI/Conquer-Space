package ConquerSpace.game.population;

/**
 *
 * @author zyunl
 */
public enum JobType {
    Miner("Miner"), 
    Jobless("Jobless"), 
    Administrator("Administrator"), 
    Farmer("Farmer"), 
    Construction("Construction"),
    Infrastructure("Infrastructure"),
    Research("Research"),
    AeronauticalEngineer("Aeronautical Engineer"),
    PopUpkeepWorker("Population Upkeep Worker"),
    SpacePortEngineer("Space Port Engineer"),
    PowerPlantTechnician("Power Plant Technician");
    
    private final String name;

    JobType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

}
