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
package ConquerSpace.game.universe;

/**
 *
 * @author zyunl
 */
public class SpacePoint {

    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public SpacePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return (("" + x + y).hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpacePoint) {
            SpacePoint ither = (SpacePoint) obj;
            return ((ither.x == x) && (ither.y == y));
        }
        return false;
    }

    @Override
    public String toString() {
        return (x + ", " + y);
    }
}
