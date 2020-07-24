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
package ConquerSpace.common.game.organizations.behavior;

import ConquerSpace.client.gui.game.GameWindow;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.server.GameTickController;

/**
 *
 * @author EhWhoAmI
 */
public class PlayerBehavior extends Behavior implements GameTickController {

    public transient GameWindow mainwindow;
    //UI stuff

    public PlayerBehavior(GameState gameState, Organization org) {
        super(gameState, org);
        if (!(org instanceof Civilization)) {
            throw new IllegalArgumentException("The org has to be a civ!");
        }
    }

    @Override
    public void doBehavior() {
        //Does nothing
    }

    @Override
    public void initBehavior() {
        Civilization civilization = (Civilization) org;
        mainwindow = new GameWindow(gameState, civilization);
        mainwindow.setVisible(true);
    }

    @Override
    public boolean allowTick() {
        return mainwindow.allowTick();
    }

    @Override
    public int getTickCount() {
        return mainwindow.getTickCount();
    }
}