package ConquerSpace.game;

import ConquerSpace.Globals;
import ConquerSpace.game.actions.Action;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.TurnSaveWindow;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.UniverseDisplayer;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.UserInterface;
import ConquerSpace.game.universe.civilizations.Civilization;
import ConquerSpace.util.CQSPLogger;
import java.util.ArrayList;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;

/**
 * The controller of the game UI.
 *
 * @author Zyun
 */
public class GameController {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameController.class.getName());

    /**
     * Constructor. Inits all components.
     */
    public GameController() {
        Globals.turn = 0;
        Globals.universe.processTurn(Globals.turn);
        Globals.turn++;
        //Boot the player
        UniverseDisplayer displayer = new UniverseDisplayer();
        UserInterface userInterface = new UserInterface();
        TurnSaveWindow tsWindow = new TurnSaveWindow(Globals.date);

        Timer ticker = new Timer(100, (e) -> {
            if (!tsWindow.isPaused()) {
                //DO ticks, somehow
                Globals.date.increment(1);
            }
        });
        ticker.start();
//        Globals.turn = 0;
//        //Do first process turn to set everything
//        Globals.universe.processTurn(Globals.turn);
//        System.gc();
//        Globals.turn++;
//        LOGGER.info("Entering game loop...");
//        Thread t = new Thread(new Thread() {
//
//            @Override
//            public void run() {
//                main:
//                //Game loop
//                while (true) {
//                    LOGGER.info("Turn " + Globals.turn);
//                    for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
//                        Civilization c = Globals.universe.getCivilization(i);
//                        ArrayList<Action> actions = c.controller.doTurn(c);
//                        LOGGER.info("Doing civ " + c.getSpeciesName());
//                        System.gc();
//                    }
//                    Globals.universe.processTurn(Globals.turn);
//                    Globals.turn++;
//                    System.gc();
//                }
//            }
//        });
//        t.start();
    }
}
