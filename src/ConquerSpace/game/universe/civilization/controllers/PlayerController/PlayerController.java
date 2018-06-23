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
    private static final Logger LOGGER = CQSPLogger.getLogger(PlayerController.class.getName());
    
    public UniverseDisplayer displayer;
    public UserInterface userInterface;
    public TurnSaveWindow tsWindow;
    public AlertDisplayer alertDisplayer;
    
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
        tsWindow = new TurnSaveWindow(d, u);
        alertDisplayer = AlertDisplayer.getInstance();
    }
}
