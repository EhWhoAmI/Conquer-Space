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
public class PlayerController implements CivilizationController, Runnable {
    public static boolean isOpen = true;
    private static final Logger LOGGER = CQSPLogger.getLogger(PlayerController.class.getName());
    UniverseDisplayer displayer;
    UserInterface userInterface;
    TurnSaveWindow tsWindow;

    @Override
    public ArrayList<Action> doTurn(Civilization c) {
        isOpen = true;
        ArrayList<Action> actions = new ArrayList<>();
        Thread t = new Thread(this, "Game-Thread");
        t.setName("Game-Thread");
        t.start();
        LOGGER.info("Begin init");
        displayer = new UniverseDisplayer(actions);
        userInterface = new UserInterface();
        tsWindow = new TurnSaveWindow();
        LOGGER.info("Done init");
        LOGGER.info("Thread name: " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        while (isOpen) {
            //Need this for some reason, so that the turn can work.
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
        displayer.setVisible(false);
            userInterface.setVisible(false);
        return actions;
    }

    @Override
    public void run() {
    }
    
    
}
