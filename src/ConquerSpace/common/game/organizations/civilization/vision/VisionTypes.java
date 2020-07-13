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
package ConquerSpace.common.game.organizations.civilization.vision;

/**
 *
 * @author EhWhoAmI
 */
public class VisionTypes {
    /**
     * Doesn't know it is even there.
     */
    public static final int UNDISCOVERED = 0;
    /**
     * Knows the star exists, but nothing else.
     */
    public static final int EXISTS = 1;
    
    /**
     * Knows what is inside, planets and all.
     */
    public static final int KNOWS_ALL = 100;
}
