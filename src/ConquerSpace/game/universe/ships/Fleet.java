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
package ConquerSpace.game.universe.ships;

import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.Vector;
import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Fleet extends SpaceShip{
    private ArrayList<Division> divisions;
    private String name;
    private int id;
    private Vector v;
    private UniversePath location;
    
    public Fleet(String name, int id) {
        this.id = id;
        this.name = name;
        divisions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Division> getDivisions() {
        return divisions;
    }
    
    public void addDivision(Division d) {
        divisions.add(d);
    }

    @Override
    public UniversePath getLocation() {
        return location;
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

    public void setVector(Vector v) {
        this.v = v;
    }

    public void setX(double X) {
        this.X = X;
    }

    public void setY(double Y) {
        this.Y = Y;
    }

    public void setLocation(UniversePath location) {
        this.location = location;
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
