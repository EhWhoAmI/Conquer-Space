package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.Action;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import java.util.ArrayList;

/**
 * Controller for player.
 * @author Zyun
 */
public class PlayerController implements CivilizationController{

    @Override
    public ArrayList<Action> doTurn() {
        UniverseDisplayer displayer = new UniverseDisplayer();
        UserInterface userInterface = new UserInterface();
        TurnSaveWindow tsWindow = new TurnSaveWindow();
        return null;
    }
}