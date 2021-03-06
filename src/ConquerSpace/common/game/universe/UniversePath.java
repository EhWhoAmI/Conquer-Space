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
package ConquerSpace.common.game.universe;

import java.io.Serializable;

/**
 * Path of an object in a universe. To write path: "sectorid:star
 * systemid:planet id" for a planet "sectorid" for a sector "sectorid:star
 * systemid" for a star system "whateverpath:[r, d]" for an object in space, eg
 * a spaceship or satellite.
 *
 * @author EhWhoAmI
 */
public class UniversePath implements Serializable{
    /**
     * ID of system
     */
    private int systemIndex = -1;

    /**
     * The ID of the body.
     */
    private int bodyIndex = -1;
    
    /**
     * Empty constructor.
     */
    public UniversePath() {
    }

    /**
     * Creates a new UniversePath object
     *
     * @param systemID ID of system
     * @param bodyId id of the body
     */
    public UniversePath(int systemID, int bodyId) {
        this.systemIndex = systemID;
        this.bodyIndex = bodyId;
    }

    /**
     * Creates a new UniversePath object
     *
     * @param systemID ID of system
     */
    public UniversePath(int systemID) {
        this.systemIndex = systemID;
    }


    /**
     * Get the id of the Star system
     *
     * @return Star system ID
     */
    public int getSystemIndex() {
        return systemIndex;
    }

    public int getBodyIndex() {
        return bodyIndex;
    }

    /**
     * To string
     *
     * @return this object in a string form, so that it can be parsed.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
            b.append(systemIndex);
        if (bodyIndex >= 0) {
            b.append(":");
            b.append(bodyIndex);
        }
        return b.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UniversePath) {
            UniversePath other = (UniversePath) obj;
            return ((this.bodyIndex == other.bodyIndex) && (this.systemIndex == other.systemIndex));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return(("" + systemIndex + bodyIndex).hashCode());
    }
}
