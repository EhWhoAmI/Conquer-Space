package ConquerSpace.game.universe.generators;

import ConquerSpace.game.universe.UniverseConfig;
import ConquerSpace.game.universe.civilization.CivilizationConfig;
import ConquerSpace.game.universe.spaceObjects.Universe;

/**
 *
 * @author User
 */
public abstract class UniverseGenerator {
    public abstract Universe generate(UniverseConfig u, CivilizationConfig c, long seed);
}
