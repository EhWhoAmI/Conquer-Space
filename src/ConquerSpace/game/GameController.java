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
import ConquerSpace.util.ResourceLoader;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import javax.script.ScriptEngineManager;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.python.util.PythonInterpreter;

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
    int GameRefreshRate = (5 * 24);

    public static ArrayList<LaunchSystem> launchSystems;
    private Timer ticker;
    public static ArrayList<Satellite> satellites;
    public static ArrayList<JSONObject> satelliteTemplates;
    public static HashMap<String, Integer> shipTypes;
    /**
     * Constructor. Inits all components.
     */
    public GameController() {
        long begin = System.currentTimeMillis();
        //Init python engine
        satelliteTemplates = new ArrayList<>();
        shipTypes = new HashMap<>();
        

        long finish = System.currentTimeMillis();
        LOGGER.info("Took " + (finish - begin) + "ms to start python interpreter");

        //Process the 0th turn and initalize the universe.
        Globals.universe.processTurn(GameRefreshRate, Globals.date);

        //Init universe
        GameUpdater updater = new GameUpdater(Globals.universe, Globals.date);
        updater.initGame();

        //Load the player
        Globals.universe.getCivilization(0).controller.init(Globals.universe, Globals.date, Globals.universe.getCivilization(0));

        int tickerSpeed = 10;
        ticker  = new Timer(tickerSpeed, (e) -> {
            ticker.setDelay(((PlayerController) Globals.universe.getCivilization(0).controller).tsWindow.getTickCount());
            if (!((PlayerController) Globals.universe.getCivilization(0).controller).tsWindow.isPaused()) {
                tick();
                updater.calculateVision();
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
            Globals.universe.processTurn(GameRefreshRate, Globals.date);
            for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
                Globals.universe.getCivilization(i).calculateTechLevel();
            }
            //Do tech...
            //Increment tech
            processResearch();

            long end = System.currentTimeMillis();

            LOGGER.trace("Took " + (end - start) + " ms");
        }
    }

    public void processResearch() {
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Civilization c = Globals.universe.getCivilization(i);
            for (Technology t : c.currentlyResearchingTechonologys.keySet()) {
                if ((Technologies.estFinishTime(t) - c.civResearch.get(t)) <= 0) {
                    //Then tech is finished
                    c.researchTech(t);
                    c.civResearch.remove(t);
                    c.currentlyResearchingTechonologys.remove(t);
                    //Alert civ
                    c.controller.alert(new Alert(0, 0, "Tech " + t.getName() + " is finished"));
                } else {
                    //Increment by number of ticks
                    c.civResearch.put(t, c.civResearch.get(t) + c.currentlyResearchingTechonologys.get(t).getSkill() * GameRefreshRate);
                }
            }
        }
    }
}
