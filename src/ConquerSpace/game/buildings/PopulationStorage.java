package ConquerSpace.game.buildings;

import ConquerSpace.game.population.PopulationUnit;
import java.util.ArrayList;

/**
 *
 * @author zyunl
 */
public interface PopulationStorage {
    public ArrayList<PopulationUnit> getPopulationArrayList();
    public int getMaxStorage();
}
