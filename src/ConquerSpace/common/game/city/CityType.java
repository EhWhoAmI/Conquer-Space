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
package ConquerSpace.common.game.city;

import ConquerSpace.common.util.ResourceLoader;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author EhWhoAmI
 */
public enum CityType {

    Generic,
    City,
    Farm,
    Infrastructure,
    Manufacturing,
    Research,
    Mine;

    private static final HashMap<CityType, Color> districtColors;

    static {
        districtColors = new HashMap<>();
        //Read file...
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(ResourceLoader.getResourceByFile("district.colors")));
            properties.forEach((key, set) -> {
                if (key instanceof String && set instanceof String) {
                    String propertyName = (String) key;
                    String value = (String) set;
                    CityType type = CityType.valueOf(propertyName);
                    if (type != null) {
                        String[] colors = value.split(",");
                        if (colors.length == 3) {
                            //Parse the numbers
                            try {
                                int r = Integer.parseInt(colors[0]);
                                int g = Integer.parseInt(colors[1]);
                                int b = Integer.parseInt(colors[2]);
                                Color color = new Color(r, g, b);
                                districtColors.put(type, color);
                            } catch (NumberFormatException nfe) {
                            }
                        }
                    }
                }
            });

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    public static Color getDistrictColor(CityType type) {
        return (districtColors.get(type));
    }
}
