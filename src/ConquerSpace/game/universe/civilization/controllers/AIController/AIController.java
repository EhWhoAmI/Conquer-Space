package ConquerSpace.game.universe.civilization.controllers.AIController;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.actions.Action;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.events.Event;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

/**
 * Controller of the AI. Will need to link to scripts.
 *
 * @author Zyun
 */
public class AIController implements CivilizationController {

    private static final Logger LOGGER = CQSPLogger.getLogger(AIController.class.getName());

    @Override
    public ArrayList<Action> doTurn(Civilization c) {
        //For now comment it out... Until we do the ai
        //actions = (ArrayList <Action>) script.getObject("actions");
        ArrayList<Action> actions = new ArrayList<>();
        return actions;
    }

    @Override
    public void alert(Alert a) {
        //Skip for AI, at least for now.
    }

    @Override
    public void init(Universe u, StarDate d, Civilization c) {
        LOGGER.info("initialized the ai");
    }

    @Override
    public void refreshUI() {
        //Ignore
    }

    @Override
    public void passEvent(Event e) {
    }
}
