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
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import javax.script.ScriptEngineManager;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;
import org.python.util.PythonInterpreter;

/**
 * The controller of the game UI.
 *
 * @author Zyun
 */
public class GameController {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameController.class.getName());

    //For evals...
    public static PythonInterpreter pythonEngine;

    //Rate the game refreshes buildings and stuff like that
    //Set to 5 days
    int GameRefreshRate = (5 * 24);

    public static ArrayList<LaunchSystem> launchSystems;
    
    public static ArrayList<Satellite> satellites;
    
    /**
     * Constructor. Inits all components.
     */
    public GameController() {
        long begin = System.currentTimeMillis();
        //Init python engine
        ScriptEngineManager manager = new ScriptEngineManager();
        pythonEngine = new PythonInterpreter();
        //Load python methods
        try {
            Scanner scriptReader = new Scanner(ResourceLoader.getResourceByFile("script.python.processing.files"));
            int count = 0;
            while (scriptReader.hasNextLine()) {
                String script = scriptReader.nextLine();
                pythonEngine.execfile(ResourceLoader.loadResource(script));
                count++;
            }
            LOGGER.info("Loaded " + count + " python scripts");
        } catch (FileNotFoundException ex) {
        }
        
        long finish = System.currentTimeMillis();
        LOGGER.info("Took " + (finish - begin) + "ms to start python interpreter");
        
        //Process the 0th turn and initalize the universe.
        Globals.universe.processTurn(GameRefreshRate, Globals.date);

        //Init universe
        GameUpdater updater = new GameUpdater(Globals.universe, Globals.date);
        updater.initGame();
        
        //Load the player
        Globals.universe.getCivilization(0).controller.init(Globals.universe, Globals.date, Globals.universe.getCivilization(0));

        //Atomic integer so that we can edit it in a lambada.
        AtomicInteger lastTick = new AtomicInteger(Globals.date.getMonthNumber());

        int tickerSpeed = 1;
        Timer ticker = new Timer(tickerSpeed, (e) -> {
            if (!((PlayerController) Globals.universe.getCivilization(0).controller).tsWindow.isPaused()) {
                //DO ticks
                Globals.date.increment(1);
                //Check for month increase

                if (Globals.date.bigint.mod(BigInteger.valueOf(GameRefreshRate)) == BigInteger.ZERO) {
                    lastTick.set(Globals.date.getMonthNumber());
                    long start = System.currentTimeMillis();

                    Globals.universe.processTurn(GameRefreshRate, Globals.date);
                    for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
                        Globals.universe.getCivilization(i).calculateTechLevel();
                    }
                    //Do tech...
                    //Increment tech

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
                    long end = System.currentTimeMillis();

                    LOGGER.trace("Took " + (end - start) + " ms");
                }
            }
        });

        //Start ticker
        ticker.start();
    }
}
