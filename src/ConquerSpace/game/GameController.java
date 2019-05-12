package ConquerSpace.game;

import ConquerSpace.Globals;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.util.CQSPLogger;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * The controller of the game UI.
 *
 * @author Zyun
 */
public class GameController {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameController.class.getName());

    //For evals...
    //Rate the game refreshes buildings and stuff like that
    //Set to 5 days
    public static int GameRefreshRate = (5 * 24);

    public static ArrayList<LaunchSystem> launchSystems;
    private Timer ticker;
    public static ArrayList<Satellite> satellites;
    public static ArrayList<JSONObject> satelliteTemplates;
    public static ArrayList<JSONObject> shipComponentTemplates;
    public static HashMap<String, Integer> shipTypes;
    public static GameUpdater updater;

    /**
     * Constructor. Inits all components.
     */
    public GameController() {
        long begin = System.currentTimeMillis();
        //Init python engine
        satelliteTemplates = new ArrayList<>();
        shipTypes = new HashMap<>();
        shipComponentTemplates = new ArrayList<>();

        long finish = System.currentTimeMillis();
        LOGGER.info("Took " + (finish - begin) + "ms to start python interpreter");

        //Init universe
        updater = new GameUpdater(Globals.universe, Globals.date);
        updater.initGame();

        //Process the 0th turn and initalize the universe.
        updater.updateUniverse(Globals.universe, Globals.date);

        //Globals.universe.processTurn(GameRefreshRate, Globals.date);
        //Load the player
        Globals.universe.getCivilization(0).controller.init(Globals.universe, Globals.date, Globals.universe.getCivilization(0));

        int tickerSpeed = 10;
        ticker = new Timer(tickerSpeed, (e) -> {
            ticker.setDelay(((PlayerController) Globals.universe.getCivilization(0).controller).tsWindow.getTickCount());
            updater.calculateVision();

            if (!((PlayerController) Globals.universe.getCivilization(0).controller).tsWindow.isPaused()) {
                tick();
            }
        }
        );
        //Start ticker
        ticker.start();
    }

    //Process ingame tick.
    public synchronized void tick() {
        //DO ticks
        Globals.date.increment(1);
        //Check for month increase

        if (Globals.date.bigint % GameRefreshRate == 0) {
            long start = System.currentTimeMillis();
            //Globals.universe.processTurn(GameRefreshRate, Globals.date);

            updater.updateUniverse(Globals.universe, Globals.date);

            for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
                Globals.universe.getCivilization(i).calculateTechLevel();
            }
            //Do tech...
            //Increment tech
            updater.processResearch();

            //Increment resources
            updater.processResources();

            long end = System.currentTimeMillis();

            LOGGER.trace("Took " + (end - start) + " ms");
        }
    }
}
