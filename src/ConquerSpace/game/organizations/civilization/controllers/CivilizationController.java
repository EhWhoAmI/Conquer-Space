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

import ConquerSpace.game.StarDate;
import ConquerSpace.game.actions.Action;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.events.Event;
import ConquerSpace.game.universe.bodies.Universe;
import java.util.ArrayList;

/**
 * This is the interface of the civilization controller.
 * @author EhWhoAmI
 */
public abstract class CivilizationController{
    public abstract ArrayList<Action> doTurn(Civilization c);
    public abstract void alert(Alert a);
    public abstract void init(Universe u, StarDate d, Civilization c);
    public abstract void refreshUI();
    public abstract void passEvent(Event e);
}
