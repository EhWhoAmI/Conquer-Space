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
package ConquerSpace.util;

import java.io.File;

/**
 *
 * @author EhWhoAmI
 */
public class Utilities {

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
    }

    public static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return (Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
    }

    private static final String NUMBER_NAMES[] = new String[]{
        "thousand",
        "million",
        "billion",
        "trillion",
        "quadrillion",
        "quintillion"};

    public static String longToHumanString(long number) {
        if(number < 10000) {
            return "" + number;
        }
        for (int i = NUMBER_NAMES.length; i > 0; i--) {
            if (Math.pow(1000, i) < number) {
                return (Math.round((number/Math.pow(1000, i))*100d)/100d) + " " + NUMBER_NAMES[i - 1];
            }
        }
        return "";
    }

}
