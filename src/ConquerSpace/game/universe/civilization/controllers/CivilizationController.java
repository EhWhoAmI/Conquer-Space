package ConquerSpace.game.universe.civilization.controllers;

import ConquerSpace.game.actions.Action;
import ConquerSpace.game.universe.civilizations.Civilization;
import java.util.ArrayList;

/**
 * This is the interface of the civilization controller.
 * @author Zyun
 */
public interface CivilizationController {
    public ArrayList<Action> doTurn(Civilization c);
}
