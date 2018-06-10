package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.actions.Action;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import ConquerSpace.game.universe.civilizations.Civilization;
import ConquerSpace.util.CQSPLogger;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;

/**
 * Controller for player.
 *
 * @author Zyun
 */
public class PlayerController implements CivilizationController {
    public static boolean isOpen = true;
    private static final Logger LOGGER = CQSPLogger.getLogger(PlayerController.class.getName());
    UniverseDisplayer displayer;
    UserInterface userInterface;
    TurnSaveWindow tsWindow;

    @Override
    public ArrayList<Action> doTurn(Civilization c) {

        displayer.setVisible(false);
        userInterface.setVisible(false);
        return null;
    }
}
