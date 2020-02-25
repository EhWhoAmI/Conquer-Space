package ConquerSpace.game.people;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.government.GovernmentPosition;

/**
 *
 * @author
 */
public class Administrator extends Person {
    public GovernmentPosition position;
    public Civilization employer;
    
    public Administrator(String name, int age) {
        super(name, age);
    }

    @Override
    public String getJobName() {
        return "Administrator";
    }
}
