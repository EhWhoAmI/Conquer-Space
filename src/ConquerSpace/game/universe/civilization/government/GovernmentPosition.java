package ConquerSpace.game.universe.civilization.government;

/**
 *
 * @author zyunl
 */
public class GovernmentPosition {
    String name;
    int power;
    PoliticalPowerTransitionMethod method;

    public GovernmentPosition() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setMethod(PoliticalPowerTransitionMethod method) {
        this.method = method;
    }

    public PoliticalPowerTransitionMethod getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return name;
    }
}
