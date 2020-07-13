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

import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.organizations.civilization.controllers.PlayerController;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.gui.music.MusicPlayer;
import ConquerSpace.util.ExceptionHandling;
import ConquerSpace.util.Timer;
import ConquerSpace.util.logging.CQSPLogger;
import org.apache.logging.log4j.Logger;

/**
 * The controller of the game, says when to tick, initializes everything.
 *
 * @author EhWhoAmI
 */
public class GameController {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameController.class.getName());

    /**
     * This is the whole universe.
     */
    public Universe universe;

    public static GameTicker updater;

    public static Civilization playerCiv = null;

    public static final int AU_IN_LTYR = 63241;

    private PlayerController playerController;

    private GameState gameState;

    //Falls under UI, so have to change position
    public static MusicPlayer musicPlayer;
    
    private Timer ticker;

    /**
     * Constructor. Inits all components.
     */
    public GameController(GameState gs) {
        gameState = gs;
        universe = gs.universe;
        //Init updater
        updater = new GameUpdater(gameState);

        //First run over all the game
        updater.tick(0);

        //Load the players
        for (int i = 0; i < gameState.universe.getCivilizationCount(); i++) {
            int civid = gameState.universe.getCivilization(i);
            Civilization civilization = ((Civilization) universe.organizations.get(civid));
            civilization.controller.init(gameState, civilization);
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
                        updater.tick(1);
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
