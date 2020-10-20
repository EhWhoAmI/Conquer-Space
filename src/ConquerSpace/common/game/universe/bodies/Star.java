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
package ConquerSpace.common.game.universe.bodies;

import ConquerSpace.common.GameState;
import ConquerSpace.common.save.SerializeClassName;

/**
 * A star.
 *
 * @author EhWhoAmI
 */
@SerializeClassName("star")
public class Star extends StarSystemBody {

    public StarType type;
    /**
     * Radius of star in kilometers.
     * Largest star can be about 1.7k solar radii, where the sun is about 695,700km.
     * Neutron stars can be tiny, you know.
     */
    public int starSize;
    private int parentStarSystem;
    
    private int ownerID = -1;

    /**
     * @param state
     * @see StarType
     * @param type type of star
     * @param starSize size of star
     */
    public Star(GameState state, StarType type, int starSize) {
        super(state);
        this.type = type;
        this.starSize = starSize;
    }

    /**
     * Set parent star system
     * @param parentStarSystem parent star system id.
     */
    void setParentStarSystem(int parentStarSystem) {
        this.parentStarSystem = parentStarSystem;
    }

    /**
     * Get parent star system id
     * @return parent star system id
     */
    public int getParentStarSystem() {
        return parentStarSystem;
    }
    
    public int getOwnerID(){
        return ownerID;
    }
    
    public void setOwnerID(int id) {
        this.ownerID = id;
    }
}
