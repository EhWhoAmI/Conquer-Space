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
package ConquerSpace.game.ships;

import ConquerSpace.game.ships.components.ShipComponent;
import ConquerSpace.game.ships.components.templates.ShipComponentTemplate;
import ConquerSpace.game.ships.hull.Hull;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.Vector;
import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
public class Ship extends SpaceShip {
    private static int ticker = 0;
    String sclass;

    private Hull hull;
    public ArrayList<ShipComponent> components;
    
    private ShipClass shipClass;

    public Ship(ShipClass sclass, double X, double Y, Vector v, UniversePath location) {
        this.X = X;
        this.Y = Y;
        goingToX = X;
        goingToY = Y;
        this.v = v;
        this.location = location;
        shipClass = sclass;
        //Set ship's id
        id = ticker++;

        components = new ArrayList<>();
        //Get components
        if (!sclass.components.isEmpty()) {
            for (ShipComponentTemplate s : sclass.components) {
                //components.add((ShipComponent) s.clone());
            }
        }
        this.hull = (Hull) sclass.getHull().clone();
        this.mass = sclass.getMass();
        this.sclass = sclass.getName();
    }

    @Override
    public UniversePath getLocation() {
        return location;
    }

    @Override
    public Vector getVector() {
        return v;
    }

    public void setVector(Vector v) {
        this.v = v;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (getName().isEmpty()) {
            return "ship";
        }
        return (getName());
    }

    @Override
    public UniversePath getOrbiting() {
        return location;
    }

    public Hull getHull() {
        return hull;
    }

    public String getShipClassName() {
        return sclass;
    }

    public int getId() {
        return id;
    }

    public ShipClass getShipClass() {
        return shipClass;
    }

    @Override
    public long getSpeed() {
        return estimatedThrust / mass;
    }
}
