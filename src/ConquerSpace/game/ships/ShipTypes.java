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
package ConquerSpace.game.ships;

/**
 * Anything that starts with a M is military.
 *
 * @author Zyun
 */
public class ShipTypes {

    public static final int TRANSPORT_SHIP = 0;
    public static final int TRADE_SHIP = 1;
    public static final int SCIENCE_SHIP = 2;
    public static final int FREIGHTER = 3;
    public static final int PROBE = 4;

    public static final int M_TRANSPORT_SHIP = 1000;
    public static final int M_FIGHTER = 1010;
    public static final int M_DESTROYER = 1020;
    public static final int M_CRUISER = 1030;
    public static final int M_BATTLESHIP = 1040;
    public static final int M_DREADNOUGHT = 1050;
    public static final int M_CARRIER = 1060;
    public static final int M_SCOUT = 1070;

    //satellite stats.
    public static final int SATELLITE_SCIENTIFIC = 0;
    public static final int SATELLITE_MILITARY = 1;
    public static final int SATELLITE_ORBSERVER = 2;
    public static final int SATELLITE_ASTRONOMY = 3;
    public static final int SATELLITE_COMMUNICATIONS = 4;
}
