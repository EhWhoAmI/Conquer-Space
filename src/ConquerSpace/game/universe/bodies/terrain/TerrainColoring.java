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
package ConquerSpace.game.universe.bodies.terrain;

import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public class TerrainColoring {

    public static final int NUMBER_OF_ROCKY_COLORS = 5;

    public static final int NUMBER_OF_GASSY_COLORS = 2;

    public static HashMap<Float, Color> getRockyTerrainColoring(int i) {
        HashMap<Float, Color> colors = new HashMap<>();

        switch (i) {
            case 0:
                colors.put(-1f, new Color(69, 24, 4));
                colors.put(-0.25f, new Color(193, 68, 14));
                colors.put(0.25f, new Color(231, 125, 17));
                colors.put(0.75f, new Color(253, 166, 0));
                colors.put(0.9f, new Color(240, 231, 231));
                break;
            case 1:
                colors.put(-1f, new Color(80, 78, 81));
                colors.put(0f, new Color(151, 151, 159));
                colors.put(0.5f, new Color(206, 204, 209));
                colors.put(1f, new Color(181, 167, 167));
                break;
            case 2:
                colors.put(-1f, new Color(49, 48, 46));
                colors.put(0.25f, new Color(148, 144, 141));
                colors.put(0.5f, new Color(218, 217, 215));
                colors.put(0.7f, new Color(240, 240, 240));
                colors.put(1f, new Color(195, 194, 190));
                break;
            case 3:
                colors.put(-1f, new Color(225, 255, 255));
                colors.put(0.7f, new Color(182, 196, 219));
                break;
            case 4:
                colors.put(-1f, new Color(0, 0, 255));
                colors.put(0.25f, new Color(182, 196, 219));
                colors.put(0.5f, new Color(10, 196, 0));
        }
        return colors;
    }

    public static HashMap<Float, Color> getGassyTerrainColoring(int i) {
        HashMap<Float, Color> colors = new HashMap<>();

        switch (i) {
            case 0:
                colors.put(-1f, new Color(200, 139, 58));
                colors.put(-0.5f, new Color(167, 156, 134));
                colors.put(-0.25f, new Color(210, 207, 218));
                colors.put(0f, new Color(211, 156, 126));
                colors.put(0.25f, new Color(144, 97, 77));
                colors.put(0.75f, new Color(64, 68, 54));
                break;
            case 1:
                colors.put(-1f, new Color(52, 62, 71));
                colors.put(-0.75f, new Color(123, 120, 105));
                colors.put(-.5f, new Color(164, 155, 114));
                colors.put(0f, new Color(197, 171, 110));
                colors.put(0.5f, new Color(195, 161, 113));
        }
        return colors;
    }
}
