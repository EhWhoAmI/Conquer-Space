package ConquerSpace.game.universe.civilization.controllers;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.actions.Action;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.util.ArrayList;

/**
 * This is the interface of the civilization controller.
 * @author Zyun
 */
public interface CivilizationController{
    public ArrayList<Action> doTurn(Civilization c);
    public void alert(Alert a);
    public void init(Universe u, StarDate d);
}
