package ConquerSpace.game.universe.goods;

/**
 * Type of good that can be transported
 * @author zyunl
 */
public abstract class Good {
    String name;
    int id;
    double volume; // volume, m^3
    double mass; //mass, kg

    public Good(String name, int id, double volume, double mass) {
        this.name = name;
        this.id = id;
        this.volume = volume;
        this.mass = mass;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getMass() {
        return mass;
    }

    public double getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return name;
    }
}
