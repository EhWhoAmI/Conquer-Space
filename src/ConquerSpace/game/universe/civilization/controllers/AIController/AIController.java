package ConquerSpace.game.universe.civilization.controllers.AIController;

import ConquerSpace.game.actions.Action;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import ConquerSpace.game.universe.civilizations.Civilization;
import ConquerSpace.util.scripts.RunScript;
import java.util.ArrayList;

/**
 * Controller of the AI. Will need to link to scripts.
 * @author Zyun
 */
public class AIController implements CivilizationController{

    @Override
    public ArrayList<Action> doTurn(Civilization c) {
        RunScript script = new RunScript(System.getProperty("user.dir") + "/assets/scripts/ai/normal/main.py");
        ArrayList<Action> actions = new ArrayList<>();
        script.addVar("civ", c);
        script.addVar("actions", actions);
        script.exec();
        //For now comment it out... Until we do the ai
        //actions = (ArrayList <Action>) script.getObject("actions");
        return actions;
    }   
}