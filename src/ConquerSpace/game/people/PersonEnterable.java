package ConquerSpace.game.people;

import ConquerSpace.game.universe.UniversePath;
import java.util.ArrayList;

/**
 *
 * @author zyunl
 */
public interface PersonEnterable {
    public UniversePath getUniversePath();
    public String getName();
    public ArrayList<Person> getPeopleArrayList();
}
