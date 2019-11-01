package ConquerSpace.game;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.gui.music.MusicPlayer;
import ConquerSpace.util.CQSPLogger;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 *    The controller of the game UI.
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
    public static ArrayList<Resource> resources;
    public static Resource foodResource = null;
    public static ArrayList<EngineTechnology> engineTechnologys;
    public static ArrayList<JSONObject> events;

    public static HashMap<String, Integer> shipTypes;
    public static HashMap<String, Integer> shipTypeClasses;
    public static GameUpdater updater;
    public static GameInitializer initer;
    public static MusicPlayer musicPlayer;

    public static final int AU_IN_LTYR = 63241;

    /**
     * Constructor. Inits all components.
     */
    public GameController() {
        //Init python engine
        satelliteTemplates = new ArrayList<>();
        shipTypes = new HashMap<>();
        shipTypeClasses = new HashMap<>();
        shipComponentTemplates = new ArrayList<>();

        //Init universe
        updater = new GameUpdater(Globals.universe, Globals.date);
        initer = new GameInitializer(Globals.universe, Globals.date, updater);

        initer.initGame();

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

        //Move ships
        updater.moveShips();

        //Check for month increase
        if (Globals.date.bigint % GameRefreshRate == 0) {
            long start = System.currentTimeMillis();

            updater.updateObjectPositions();
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
        //Process people and generate every 1000 ticks, which is about every 41 days
        if (Globals.date.bigint % 1000 == 0) {
            updater.createPeople();
        }
    }
}
