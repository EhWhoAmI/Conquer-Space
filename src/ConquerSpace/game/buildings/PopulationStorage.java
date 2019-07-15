package ConquerSpace.game.buildings;

import ConquerSpace.game.population.PopulationUnit;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author zyunl
 */
public class PopulationStorage extends Building {

    public ArrayList<PopulationUnit> population;

    public PopulationStorage() {
        population = new ArrayList<>();
    }

    public Color getColor() {
        return Color.BLUE;
    }

    public int getPopulations() {
        return 0;
    }
}
