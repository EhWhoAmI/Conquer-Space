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
package ConquerSpace.common.game.ships;

import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.Vector;
import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
public class Division extends SpaceShip{
    private int id;
    private ArrayList<Ship> ships;
    private Vector v;
    private UniversePath location;
    private int parentfleetID;

    public Division(int id, long X, long Y, Vector v, UniversePath location, int parentfleetID) {
        this.id = id;
        this.ships = new ArrayList<>();;
        this.X = X;
        this.Y = Y;
        this.v = v;
        this.location = location;
        this.parentfleetID = parentfleetID;
    }

    



    public int getId() {
        return id;
    }


    public int getParentfleetID() {
        return parentfleetID;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }
    
    public void addShip(Ship s) {
        ships.add(s);
    }

    @Override
    public double getX() {
        return X;
    }

    @Override
    public double getY() {
        return Y;
    }

    @Override
    public Vector getVector() {
        return v;
    }

    @Override
    public UniversePath getLocation() {
        return location;
    }

    public void setVector(Vector v) {
        this.v = v;
    }

    public void setX(double X) {
        this.X = X;
    }

    public void setY(double Y) {
        this.Y = Y;
    }

    @Override
    public long getSpeed() {
        return 0;
    }

    @Override
    public UniversePath getOrbiting() {
        return null;
    }
    
    
}
