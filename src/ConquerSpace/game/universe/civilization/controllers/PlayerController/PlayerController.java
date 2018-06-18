package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.actions.Action;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import ConquerSpace.game.universe.civilizations.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.util.ArrayList;
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
    AlertDisplayer alertDisplayer;
    @Override
    public ArrayList<Action> doTurn(Civilization c) {
        displayer.setVisible(false);
        userInterface.setVisible(false);
        return null;
    }

    @Override
    public void alert(Alert a) {
        alertDisplayer.addAlert(a);
    }

    @Override
    public void init(Universe u, StarDate d) {
        displayer = new UniverseDisplayer(u);
        userInterface = new UserInterface(u);
        tsWindow = new TurnSaveWindow(d);
        alertDisplayer = AlertDisplayer.getInstance();
    }
}
