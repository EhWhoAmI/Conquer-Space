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
package ConquerSpace.common;

import ConquerSpace.common.game.ships.EngineTechnology;

/**
 *
 * @author EhWhoAmI
 */
public class Calculators {
    public static class Optics {

        public static int getRange(int quality, int size) {
            return (int) (Math.log(Math.PI * (size) * (size) + 1) * 2);
        }

        public static int getLensMass(int quality, int size) {
            return (int) (((double) quality / 100d) * size * size * Math.PI);
        }

        public static int getLensSize(int quality, int range) {
            return (int) (Math.sqrt((Math.pow(Math.E, range / 2) - 1) / Math.PI));
        }
    }

    public static class Engine {

        public static int getEngineMass(int thrust, EngineTechnology tech) {
            return (int) (tech.getThrustMultiplier() * thrust);
        }
    }
}
