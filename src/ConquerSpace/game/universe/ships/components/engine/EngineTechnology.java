package ConquerSpace.game.universe.ships.components.engine;

/**
 *
 * @author zyunl
 */
public class EngineTechnology {
    private String name;
    private int id;
    /**
    * Efficiency: m/s per kg of propellant
    */
    private float efficiency;
    private float thrust_multiplier;

    public EngineTechnology(String name, float efficiency, float thrust_multiplier) {
        this.name = name;
        this.efficiency = efficiency;
        this.thrust_multiplier = thrust_multiplier;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public String getName() {
        return name;
    }

    public float getThrustMultiplier() {
        return thrust_multiplier;
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThrust_multiplier(float thrust_multiplier) {
        this.thrust_multiplier = thrust_multiplier;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
