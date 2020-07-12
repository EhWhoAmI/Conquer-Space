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
package ConquerSpace.game.organizations.civilization.controllers;

import ConquerSpace.game.GameState;
import ConquerSpace.game.StarDate;
import ConquerSpace.game.actions.Action;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.events.Event;
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.gui.game.AlertDisplayer;
import ConquerSpace.gui.game.AlertNotification;
import ConquerSpace.gui.game.GameWindow;
import ConquerSpace.util.logging.CQSPLogger;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

/**
 * Controller for player.
 *
 * @author EhWhoAmI
 */
public class PlayerController extends CivilizationController {

    private Civilization c;
    private Universe u;
    private StarDate d;
    private static final Logger LOGGER = CQSPLogger.getLogger(PlayerController.class.getName());
    public GameWindow mainwindow;
    public AlertDisplayer alertDisplayer;
    public ArrayList<Ship> selectedShips = new ArrayList<>();
    
    private GameState state;

    @Override
    public ArrayList<Action> doTurn(Civilization c) {
        return null;
    }

    @Override
    public void alert(Alert a) {
        alertDisplayer.addAlert(a);
        AlertNotification notification = new AlertNotification(a.toString(), a.getDesc());
        notification.setLocation(mainwindow.getWidth() / 2 - notification.getWidth() / 2, 0);
        mainwindow.addFrame(notification);
        notification.setVisible(true);
    }

    @Override
    public void init(GameState state, Civilization civ) {
        this.state = state;
        mainwindow = new GameWindow(state, this, civ);
        alertDisplayer = AlertDisplayer.getInstance();
    }

    @Override
    public void refreshUI() {
        //Reload all windows
        mainwindow.dispose();

        //Then reload
        init(state, c);
    }

    @Override
    public void passEvent(Event e) {
        mainwindow.passEvent(e);
    }

    public boolean allowTick() {
        return mainwindow.allowTick();
    }

    public int getTickCount() {
        return mainwindow.getTickCount();
    }
}
