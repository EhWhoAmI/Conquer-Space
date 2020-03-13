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
package ConquerSpace;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.universe.bodies.Universe;
import java.util.Properties;

/**
 * Global variables of the game.
 *
 * @author Zyun
 */
public class Globals {

    /**
     * This is the whole universe.
     */
    public static Universe universe;

    /**
     * This is the settings of the game.
     */
    public static Properties settings;

    /**
     * Date in the stars. Game timer.
     */
    public static StarDate date = new StarDate();
}
