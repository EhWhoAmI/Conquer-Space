package ConquerSpace.game.people;

/**
 *
 * @author zyunl
 */
public class PersonalityTrait {
    private String name;  

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
