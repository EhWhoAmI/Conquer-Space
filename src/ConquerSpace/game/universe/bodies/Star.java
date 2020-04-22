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
package ConquerSpace.game.universe.bodies;

import ConquerSpace.game.universe.UniversePath;

/**
 * A star.
 *
 * @author Zyun
 */
public class Star extends Body {

    public int type;
    /**
     * Radius of star in kilometers.
     * Largest star can be about 1.7k solar radii, where the sun is about 695,700km.
     * Neutron stars can be tiny, you know.
     */
    public int starSize;
    private int parentStarSystem;
    
    private int ownerID = -1;

    /**
     * @see StarTypes
     * @param type type of star
     * @param starSize size of star
     */
    public Star(int type, int starSize) {
        this.type = type;
        this.starSize = starSize;
    }

    /**
     * Get the readable string of this star
     * @return this star to readable string
     */
    public String toReadableString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Star: " + ID + "(type=");
        switch (type) {
            case StarTypes.RED:
                builder.append("red");
                break;
            case StarTypes.BLUE:
                builder.append("blue");
                break;
            case StarTypes.BROWN:
                builder.append("brown");
                break;
            case StarTypes.YELLOW:
                builder.append("yellow");
        }
        builder.append("Size=" + starSize + ")\n");
        return (builder.toString());
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

    
    public UniversePath getUniversePath() {
        return (new UniversePath(parentStarSystem, ID));
    }
    
    public int getOwnerID(){
        return ownerID;
    }
    
    public void setOwnerID(int id) {
        this.ownerID = id;
    }
}
