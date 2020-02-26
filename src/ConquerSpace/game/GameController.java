package ConquerSpace.game;

import ConquerSpace.Globals;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.universe.goods.Element;
import ConquerSpace.game.universe.goods.NonElement;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.gui.music.MusicPlayer;
import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.Timer;
import java.util.ArrayList;
import java.util.HashMap;
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
    public static ArrayList<Resource> resources;
    public static Resource foodResource = null;
    public static ArrayList<EngineTechnology> engineTechnologys;
    public static ArrayList<JSONObject> events;
    public static ArrayList<PersonalityTrait> personalityTraits;
    public static ArrayList<Person> people = new ArrayList<>();

    public static HashMap<String, Integer> shipTypes;
    public static HashMap<String, Integer> shipTypeClasses;
    public static GameUpdater updater;
    public static GameInitializer initer;
    public static PeopleProcessor peopleProcessor;
    public static MusicPlayer musicPlayer;
    
    public static ArrayList<Element> elements;
    public static ArrayList<NonElement> rawMaterials;
    
    public static final int AU_IN_LTYR = 63241;

    /**
     * Constructor. Inits all components.
     */
    public GameController() {
        //Init universe
        updater = new GameUpdater(Globals.universe, Globals.date);
        initer = new GameInitializer(Globals.universe, Globals.date, updater);

        initer.initGame();

        peopleProcessor = new PeopleProcessor(Globals.universe, Globals.date);

        //Process the 0th turn and initalize the universe.
        updater.updateUniverse(Globals.universe, Globals.date);

        //Globals.universe.processTurn(GameRefreshRate, Globals.date);
        //Load the player
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Globals.universe.getCivilization(i).controller.init(Globals.universe, Globals.date, Globals.universe.getCivilization(0));
        }

        int tickerSpeed = 10;

        ticker = new Timer();
        Runnable action = new Runnable() {
            public void run() {
                ticker.setWait(((PlayerController) Globals.universe.getCivilization(0).controller).tsWindow.getTickCount());
                updater.calculateVision();

                if (!((PlayerController) Globals.universe.getCivilization(0).controller).tsWindow.isPaused()) {
                    tick();
                }
            }
        };
        ticker.setAction(action);

        ticker.setWait(tickerSpeed);
        ticker.start();

        //Start ticker
        //ticker.start();
    }

    //Process ingame tick.
    public synchronized void tick() {
        //DO ticks
        Globals.date.increment(1);

        //Move ships
        updater.moveShips();

        updater.updateObjectPositions();

        //Check for month increase
        if (Globals.date.bigint % GameRefreshRate == 0) {
            long start = System.currentTimeMillis();

            updater.updateUniverse(Globals.universe, Globals.date);

            for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
                Globals.universe.getCivilization(i).calculateTechLevel();
            }
            //Do tech...
            //Increment tech
            updater.processResearch();

            //Increment resources
            updater.processResources();

            peopleProcessor.createPeople();

            peopleProcessor.processPeople();

            long end = System.currentTimeMillis();

            LOGGER.trace("Took " + (end - start) + " ms");
        }
        //Process people and generate every 1000 ticks, which is about every 41 days
        if (Globals.date.bigint % 1000 == 0) {
            updater.createPeople();
        }
    }
}
