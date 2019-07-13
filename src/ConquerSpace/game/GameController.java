package ConquerSpace.game;

import ConquerSpace.Globals;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.gui.music.MusicPlayer;
import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.names.NameGenerator;
import java.io.IOException;
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
    public static ArrayList<Resource> resources;
    public static ArrayList<EngineTechnology> engineTechnologys;
    public static HashMap<String, Integer> shipTypes;
    public static GameUpdater updater;
    public static MusicPlayer musicPlayer;
    
    public static final int AU_IN_LTYR = 63241;

    /**
     * Constructor. Inits all components.
     */
    public GameController() {
        //Init python engine
        satelliteTemplates = new ArrayList<>();
        shipTypes = new HashMap<>();
        shipComponentTemplates = new ArrayList<>();

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

        //Move ships
        updater.moveShips();

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

            long end = System.currentTimeMillis();

            LOGGER.trace("Took " + (end - start) + " ms");
        }
        //Process people and generate every 1000 ticks, which is about every 41 days
        if (Globals.date.bigint % 1000 == 0) {
            for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
                Civilization c = Globals.universe.getCivilization(i);
                NameGenerator gen = null;
                try {
                    gen = NameGenerator.getNameGenerator("us.names");
                } catch (IOException ex) {
                    //Ignore
                }
                //Create 5-10 random scientists
                c.unrecruitedPeople.clear();
                int peopleCount = (int) (Math.random() * 5) + 5;
                for (int peep = 0; peep < peopleCount; peep++) {
                    int age = (int) (Math.random() * 40) + 20;
                    String person = "name";
                    person = gen.getName((int) Math.round(Math.random()));
                    Scientist nerd = new Scientist(person, age);
                    nerd.setSkill((int) (Math.random() * 5) + 1);
                    c.unrecruitedPeople.add(nerd);
                }
            }
        }
    }
}
