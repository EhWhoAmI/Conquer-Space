package ConquerSpace.game.universe.ships.launch;

import ConquerSpace.game.tech.Techonology;

/**
 *
 * @author Zyun
 */
public class LaunchSystem {

    private String name;
    private Techonology tech;
    private int size;
    private int safety;
    private int launchCost;
    private boolean reusability;
    private int reusabilityCost;
    private int maxCargo;
    private int constructCost;
    private int id;
    
    private static int idcounter = 0;

    /**
     * For reusable launch system.
     *
     * @param name
     * @param tech
     * @param size
     * @param safety
     * @param cost
     * @param constructCost
     * @param reusabilityCost
     * @param maxCargo
     */
    public LaunchSystem(String name, Techonology tech, int size, int safety, int cost, int constructCost, int reusabilityCost, int maxCargo) {
        this.name = name;
        this.tech = tech;
        this.size = size;
        this.safety = safety;
        this.launchCost = cost;
        this.constructCost = constructCost;
        this.reusability = true;
        this.reusabilityCost = reusabilityCost;
        this.maxCargo = maxCargo;
        id = idcounter++;
    }

    public LaunchSystem(String name, Techonology tech, int size, int safety, int cost, int constructCost, int maxCargo) {
        this.name = name;
        this.tech = tech;
        this.size = size;
        this.safety = safety;
        this.launchCost = cost;
        this.constructCost = constructCost;
        this.reusability = false;
        this.maxCargo = maxCargo;
        id = idcounter++;
    }

    
    public int getLaunchCost() {
        return launchCost;
    }

    public int getMaxCargo() {
        return maxCargo;
    }

    public String getName() {
        return name;
    }

    public int getReusabilityCost() {
        return reusabilityCost;
    }

    public int getSafety() {
        return safety;
    }

    public int getSize() {
        return size;
    }

    public Techonology getTech() {
        return tech;
    }

    public int getConstructCost() {
        return constructCost;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
