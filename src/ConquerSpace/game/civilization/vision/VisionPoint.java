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
package ConquerSpace.game.civilization.vision;

import ConquerSpace.game.universe.Point;

/**
 * A point where vision comes from, where you can start vision.
 *
 * @author EhWhoAmI
 */
public interface VisionPoint {

    /**
     * Range in light years.
     *
     * @return range.
     */
    public int getRange();
    public int getCivilization();
    public Point getPosition();
    
    public static final int GAMMA = 0;
    public static final int XRAY = 1;
    public static final int ULTRAVIOLET = 2;
    public static final int VISIBLE = 3;
    public static final int INFRARED = 4;
    public static final int TERAHERTZ = 5;
    public static final int MICROWAVE = 6;
    public static final int RADIO_WAVE = 7;
}
