package ConquerSpace.game.universe.resources;

/**
 *
 * @author zyunl
 */
public class FoodResource extends Resource {

    private FoodResourceType type;

    public FoodResource(String name, FoodResourceType type, int value, float rarity, int id) {
        super(name, value, rarity, id);
        this.type = type;
    }

    public FoodResourceType getFoodType() {
        return type;
    }

    public void setFoodType(FoodResourceType type) {
        this.type = type;
    }
}
