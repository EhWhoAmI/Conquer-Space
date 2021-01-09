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

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.actions.Alert;
import ConquerSpace.common.game.organizations.Organization;
import java.io.Serializable;

/**
 * Behavior for orgs, what they do. Probably have to change to a class in the
 * future, but no idea on how to implement.
 *
 * @author EhWhoAmI
 */
public abstract class Behavior extends ConquerSpaceGameObject implements Serializable {

    Organization org;

    public Behavior(GameState gameState, Organization org) {
        super(gameState);
        this.org = org;
    }

    public abstract void doBehavior();

    public abstract void initBehavior();

    //Purely for the player civ
    public void alert(Alert alert) {
        //No default behavior
    }
}
