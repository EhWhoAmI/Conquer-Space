package ConquerSpace.game.ui;

import ConquerSpace.Globals;
import ConquerSpace.game.Action;
import ConquerSpace.game.universe.civilizations.Civilization;
import ConquerSpace.util.CQSPLogger;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

/**
 * The controller of the game UI.
 *
 * @author Zyun
 */
public class GameController {
    private static final Logger LOGGER = CQSPLogger.getLogger(GameController.class.getName());   /**
     * Constructor. Inits all components.
     */
    public GameController() {
        Globals.turn = 0;
        //Do first process turn to set everything
        Globals.universe.processTurn(Globals.turn);
        System.gc();
        Globals.turn++;
        LOGGER.info("Entering game loop...");
        Thread t = new Thread(new Thread() {

            @Override
            public void run() {
                main:
                //Game loop
                while (true) {
                    LOGGER.info("Turn " + Globals.turn);
                    for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
                        Civilization c = Globals.universe.getCivilization(i);
                        ArrayList<Action> actions = c.controller.doTurn();
                        LOGGER.info("Doing civ " + c.getSpeciesName());
                        System.gc();
                    }
                    Globals.universe.processTurn(Globals.turn);
                    Globals.turn++;
                    System.gc();
                }
            }

        });
        t.start();

    }
}
