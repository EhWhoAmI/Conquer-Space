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

package ConquerSpace.common.game.ships;

/**
 * What a ship is capable of doing.
 *
 * @author EhWhoAmI
 */
public enum ShipCapability {
    /**
     * The ship is able to enter orbit from land. Will probably customize so that it can deal with
     * different planets.
     */
    ToOrbit,
    
    /**
     * The ship is able to travel in space.
     */
    SpaceTravel,
    
    /**
     * The ship is able to travel in the atmosphere.
     */
    AtmosphericTravel,
    
    /**
     * The ship is able to enter the atmosphere safely. Note: it does not mean that it can land or
     * fly.
     */
    Reentry,
    
    /**
     * The ship is able to dock with another ship.
     */
    Docking,
    
    /**
     * The ship is able to land on a surface.
     */
    Landing;
}
