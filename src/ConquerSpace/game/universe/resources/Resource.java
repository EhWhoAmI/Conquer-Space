package ConquerSpace.game.universe.resources;

/**
 *
 * @author Zyun
 */
public class Resource {

    public String name;
    public int amount;
    public int type;
    public int renewability = 0;
    public int max;

    public Resource(int type, int amount) {
        this.amount = amount;
        this.type = type;
        switch (type) {
            case RawResourceTypes.GAS:
                name = "Gas";
                break;
            case RawResourceTypes.ROCK:
                name = "Rock";
                break;

            case RawResourceTypes.METAL:
                name = "Metal";
                break;
            case RawResourceTypes.FOOD:
                name = "Food";
                break;
            case RawResourceTypes.ENERGY:
                name = "Energy";
                break;
        }
    }

    public Resource(String name, int amount, int type) {
        this.name = name;
        this.amount = amount;
        this.type = type;
        switch (type) {
            case RawResourceTypes.GAS:
                name = "Gas";
                break;
            case RawResourceTypes.ROCK:
                name = "Rock";
                break;

            case RawResourceTypes.METAL:
                name = "Metal";
                break;
            case RawResourceTypes.FOOD:
                name = "Food";
                break;
            case RawResourceTypes.ENERGY:
                name = "Energy";
                break;
        }
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

    public void setRenewability(int renewability) {
        this.renewability = renewability;
    }

    public int getRenewability() {
        return renewability;
    }

    public String getName() {
        return name;
    }
    
    public void renew() {
        //If there are no resources, it will not renew.
        if((amount + renewability) >= max) {
            amount = max;
        } else if(amount != 0){
            amount += renewability;
        }
    }
}
