package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.Action;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
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

    private static final Logger LOGGER = CQSPLogger.getLogger(PlayerController.class.getName());
    UniverseDisplayer displayer;
    UserInterface userInterface;
    TurnSaveWindow tsWindow;

    @Override
    public ArrayList<Action> doTurn() {
        ArrayList<Action> actions = new ArrayList<>();
        Thread t = new Thread(this, "Game-Thread");
        t.setName("Game-Thread");
        t.start();
        System.out.println(t.isAlive());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        while (tsWindow.isVisible()) {
            //Wait until dpne
            
        }
        displayer.setVisible(false);
            userInterface.setVisible(false);
        return actions;
    }

    @Override
    public void run() {
        LOGGER.info("Begin init");
        displayer = new UniverseDisplayer();
        userInterface = new UserInterface();
        tsWindow = new TurnSaveWindow();
        LOGGER.info("Done init");
        LOGGER.info("Thread name: " + Thread.currentThread().getName());
    }
}
