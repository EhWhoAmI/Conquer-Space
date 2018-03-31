package ConquerSpace.game.universe.civilization.controllers;

import ConquerSpace.game.Action;
import java.util.ArrayList;

/**
 * This is the interface of the civilization controller.
 * @author Zyun
 */
public interface CivilizationController {
    public ArrayList<Action> doTurn();
}
