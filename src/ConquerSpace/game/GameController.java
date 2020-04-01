/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.game;

import ConquerSpace.Globals;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.universe.resources.Element;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.NonElement;
import ConquerSpace.game.universe.resources.ProductionProcess;
import ConquerSpace.game.universe.resources.ResourceDistribution;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.gui.music.MusicPlayer;
import ConquerSpace.util.logging.CQSPLogger;
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
    //public static Resource foodResource = null;
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
    public static HashMap<Good, ResourceDistribution> ores = new HashMap<>();
    public static ArrayList<Good> allGoods;
    public static ArrayList<Good> goods;
    
    public static ArrayList<ProductionProcess> prodProcesses;
    
    public static Civilization playerCiv = null;

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
        updater.updateUniverse(Globals.universe, Globals.date, 0);

        //Load the player
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Globals.universe.getCivilization(i).controller.init(Globals.universe, Globals.date, Globals.universe.getCivilization(0));
        }

        int tickerSpeed = 10;

        ticker = new Timer();
        Runnable action = new Runnable() {
            public void run() {
                ticker.setWait(((PlayerController) playerCiv.controller).getTickCount());
                updater.calculateVision();

                if (!((PlayerController) playerCiv.controller).allowTick()) {
                    tick();
                }
            }
        };
        ticker.setAction(action);

        ticker.setWait(tickerSpeed);

        //Start ticker
        ticker.start();
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

            updater.updateUniverse(Globals.universe, Globals.date, GameRefreshRate);

            for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
                Globals.universe.getCivilization(i).calculateTechLevel();
            }
            //Do tech...
            //Increment tech
            updater.processResearch();

            //Increment resources
            updater.processResources();

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
