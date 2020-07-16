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
package ConquerSpace.common.game.ships.satellites;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.organizations.civilization.vision.VisionPoint;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.save.SerializeClassName;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("space-telescope")
public class SpaceTelescope extends Satellite implements VisionPoint{

    private int civilization = -1;
    private int range = 0;

    public SpaceTelescope(GameState gameState) {
        super(gameState);
    }

    public void setCivilization(int civ) {
        owner = civ;
    }
    
    @Override
    public int getCivilization() {
        return getOwner();
    }

    public void setRange(int range) {
        this.range = range;
    }

    
    @Override
    public int getRange() {
        return range;
    }

    public void setPosition(UniversePath position) {
        this.orbiting = position;
    }

    @Override
    public UniversePath getPosition() {
        return this.orbiting;
    }
}
