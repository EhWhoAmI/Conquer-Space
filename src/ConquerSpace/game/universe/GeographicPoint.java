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
public class GeographicPoint {
    private int X;
    private int Y;

    public GeographicPoint() {
        X = 0;
        Y = 0;
    }

    public GeographicPoint(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GeographicPoint) {
            GeographicPoint ither = (GeographicPoint) obj;
            return ((ither.X == X) && (ither.Y == Y));
        }
        return false;
    }  

    @Override
    public int hashCode() {
        return (X + "" + Y).hashCode();
    }
}
