package ConquerSpace.game.universe.ships.components;

/**
 *
 * @author Zyun
 */
public class ShipComponent {
    //Mass in KG
    protected int mass;
    //Cost in credits
    protected int cost;
    protected String name;
    protected int id;
    protected String ratingType;
    protected int rating;

    public ShipComponent(int mass, int cost, String name) {
        this.mass = mass;
        this.cost = cost;
        this.name = name;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public int getMass() {
        return mass;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRatingType() {
        return "";
    }
}