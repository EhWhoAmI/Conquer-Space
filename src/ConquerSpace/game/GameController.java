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
import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.logistics.SupplyChain;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.ships.components.engine.EngineTechnology;
import ConquerSpace.game.ships.launch.LaunchSystem;
import ConquerSpace.game.ships.satellites.Satellite;
import ConquerSpace.game.universe.resources.Element;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.NonElement;
import ConquerSpace.game.universe.resources.ProductionProcess;
import ConquerSpace.game.universe.resources.ResourceDistribution;
import ConquerSpace.gui.music.MusicPlayer;
import ConquerSpace.util.ExceptionHandling;
import ConquerSpace.util.Timer;
import ConquerSpace.util.logging.CQSPLogger;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * The controller of the game, says when to tick, initializes everything.
 *
 * @author EhWhoAmI
 */
public class GameController {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameController.class.getName());

    //For evals... 
    //Rate the game refreshes buildings and stuff like that
    //Set to 5 days
    public static int GameRefreshRate = (5 * 24);

    //All variables...
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
    public static MusicPlayer musicPlayer;

    public static ArrayList<Element> elements;
    public static ArrayList<NonElement> rawMaterials;
    public static HashMap<Good, ResourceDistribution> ores = new HashMap<>();
    public static ArrayList<Good> allGoods;
    public static ArrayList<Good> goods;

    public static ArrayList<SupplyChain> supplyChains = new ArrayList<>();

    public static ArrayList<ProductionProcess> prodProcesses;

    public static Civilization playerCiv = null;

    public static final int AU_IN_LTYR = 63241;

    private PlayerController playerController;

    /**
     * Constructor. Inits all components.
     */
    public GameController() {
        //Init universe
        updater = new GameUpdater(Globals.universe, Globals.date, GameRefreshRate);
        initer = new GameInitializer(Globals.universe, Globals.date, updater);

        //Will need a different initializer later...
        initer.initGame();

        //Process the 0th turn and initalize the universe.
        updater.updateUniverse(Globals.universe, Globals.date, 0);

        //Load the players
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Globals.universe.getCivilization(i).controller.init(Globals.universe, Globals.date, Globals.universe.getCivilization(i));
        }

        int tickerSpeed = 10;

        ticker = new Timer();
        playerController = (PlayerController) playerCiv.controller;

        Runnable action = new Runnable() {
            public void run() {
                //Ensure that the game does not stop on a crash
                try {
                    ticker.setWait(playerController.getTickCount());
                    if (!playerController.allowTick()) {
                        updater.tick();
                    }
                } catch (Exception e) {
                    ExceptionHandling.ExceptionMessageBox("Exception: " + e.getClass() + ", " + e.getMessage(), e);
                }
            }
        };
        ticker.setAction(action);

        ticker.setWait(tickerSpeed);

        //Start ticker
        ticker.start();
    }
}
