package ConquerSpace.game.universe.ships.hull;

/**
 *
 * @author Zyun
 */
public class HullMaterial {
    private String name;
    private int strength;
    //Kg per m3
    private float density;
    
    //cost per kg
    private int cost;
    
    private int id;

    public int getCost() {
        return cost;
    }

    public float getDensity() {
        return density;
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }

    @Override
    public String toString() {
        return name;
    }

    public HullMaterial(String name, int strength, float density, int cost) {
        this.name = name;
        this.strength = strength;
        this.density = density;
        this.cost = cost;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}