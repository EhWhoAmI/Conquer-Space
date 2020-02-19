package ConquerSpace.game.universe.ships.launch;

/**
 * Class of launch vehicle 
 * @author zyunl
 */
public class LaunchVehicle {
    public LaunchSystem systemType;
    public int costPerLaunch;
    public boolean reusability;
    public int reuseCost;
    public String name;
    public float reliability;
    public int maximumMass;

    public LaunchVehicle() {
    }

    public void setCostPerLaunch(int costPerLaunch) {
        this.costPerLaunch = costPerLaunch;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReliability(float reliability) {
        this.reliability = reliability;
    }

    public void setReusability(boolean reusability) {
        this.reusability = reusability;
    }

    public void setReuseCost(int reuseCost) {
        this.reuseCost = reuseCost;
    }

    public void setSystemType(LaunchSystem systemType) {
        this.systemType = systemType;
    }

    public int getCostPerLaunch() {
        return costPerLaunch;
    }

    public String getName() {
        return name;
    }

    public float getReliability() {
        return reliability;
    }

    public int getReuseCost() {
        return reuseCost;
    }

    public LaunchSystem getSystemType() {
        return systemType;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setMaximumMass(int maximumMass) {
        this.maximumMass = maximumMass;
    }

    public int getMaximumMass() {
        return maximumMass;
    }
}
