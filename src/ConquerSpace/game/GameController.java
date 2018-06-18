package ConquerSpace.game;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.TurnSaveWindow;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.UniverseDisplayer;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.UserInterface;
import ConquerSpace.util.CQSPLogger;
import java.util.concurrent.atomic.AtomicInteger;
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
        //Process the 0th turn and initalize the universe.
        Globals.turn = 0;
        Globals.universe.processTurn(Globals.turn);
        Globals.turn++;
        //Load the player
//        UniverseDisplayer displayer = new UniverseDisplayer();
//        UserInterface userInterface = new UserInterface();
        TurnSaveWindow tsWindow = new TurnSaveWindow(Globals.date);

        //Atomic integer so that we can edit it in a lambada.
        AtomicInteger lastMonth = new AtomicInteger(1);
        
        int tickerSpeed = 1;
        Timer ticker = new Timer(tickerSpeed, (e) -> {
            if (!tsWindow.isPaused()) {
                //DO ticks, somehow
                Globals.date.increment(1);
                //Check for month increase
                if(Globals.date.getMonthNumber() > lastMonth.get()) {
                    lastMonth.incrementAndGet();
                    if(lastMonth.intValue() == 13) {
                        lastMonth.set(1);
                        Globals.universe.processTurn(0);
                    }
                }
            }
        });
        
        //Start ticker
        ticker.start();
    }
}
