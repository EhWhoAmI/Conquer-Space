package ConquerSpace.game;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
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
        
        //Init universe
        GameUpdater updater = new GameUpdater(Globals.universe, Globals.date);
        updater.initGame();
        //Load the player

        Globals.universe.getCivilization(0).controller.init(Globals.universe, Globals.date);

        //Atomic integer so that we can edit it in a lambada.
        AtomicInteger lastMonth = new AtomicInteger(1);

        int tickerSpeed = 100;
        Timer ticker = new Timer(tickerSpeed, (e) -> {
            if (!((PlayerController) Globals.universe.getCivilization(0).controller).tsWindow.isPaused()) {
                //DO ticks, somehow
                Globals.date.increment(1);
                //Check for month increase
                if (Globals.date.getMonthNumber() > lastMonth.get()) {
                    lastMonth.incrementAndGet();
                    long start = System.currentTimeMillis();
                    Globals.universe.processTurn(0);
                    long end = System.currentTimeMillis();
                    LOGGER.info("Took " + (end - start) + " ms");
                    if (lastMonth.intValue() == 13) {
                        lastMonth.set(1);
                    }
                }
            }
        });

        //Start ticker
        ticker.start();
    }
}
