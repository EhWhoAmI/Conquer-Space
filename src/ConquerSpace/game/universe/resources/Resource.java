package ConquerSpace.game.universe.resources;

/**
 *
 * @author Zyun
 */
public class Resource {
    public int amount;
    public int type;

    public Resource(int type, int amount) {
        this.amount = amount;
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
