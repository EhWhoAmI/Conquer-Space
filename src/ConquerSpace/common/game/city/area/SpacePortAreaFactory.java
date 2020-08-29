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
package ConquerSpace.common.game.city.area;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.civilization.Civilization;

/**
 *
 * @author EhWhoAmI
 */
public class SpacePortAreaFactory extends AreaFactory {

    private ObjectReference launchSystem = ObjectReference.INVALID_REFERENCE;
    private int launchSystemCount = 0;

    public SpacePortAreaFactory(Civilization civ) {
        super(civ);
    }

    public void setLaunchSystem(ObjectReference launchSystem) {
        this.launchSystem = launchSystem;
    }

    public void setLaunchSystemCount(int launchSystemCount) {
        this.launchSystemCount = launchSystemCount;
    }

    public ObjectReference getLaunchSystem() {
        return launchSystem;
    }

    public int getLaunchSystemCount() {
        return launchSystemCount;
    }

    @Override
    public Area build(GameState gameState) {
        return new SpacePortArea(gameState, launchSystem, launchSystemCount);
    }
}
