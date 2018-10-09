package ConquerSpace.game.universe.civilization.controllers.AIController;

import ConquerSpace.game.GameUniverse;
import ConquerSpace.game.StarDate;
import ConquerSpace.game.actions.Action;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.ResourceLoader;
import ConquerSpace.util.scripts.RunScript;
import java.util.ArrayList;

/**
 * Controller of the AI. Will need to link to scripts.
 *
 * @author Zyun
 */
public class AIController implements CivilizationController {
    private GameUniverse gameUniverse;
    @Override
    public ArrayList<Action> doTurn(Civilization c) {
        RunScript script = new RunScript(ResourceLoader.loadResource("script.python.ai.generation.normal.main"));
        ArrayList<Action> actions = new ArrayList<>();
        script.addVar("civ", c);
        script.addVar("actions", actions);
        script.exec();
        //For now comment it out... Until we do the ai
        //actions = (ArrayList <Action>) script.getObject("actions");
        return actions;
    }

    @Override
    public void alert(Alert a) {
        //Skip for AI, at least for now.
    }

    @Override
    public void init(Universe u, StarDate d, Civilization c) {
        gameUniverse = new GameUniverse(u, c);
    }
}
