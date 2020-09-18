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

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.Vector;
import ConquerSpace.common.save.SerializeClassName;
import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("ship")
public class Ship extends SpaceShip implements Launchable{

    String sclass;

    private ObjectReference hull;
    public ArrayList<ObjectReference> components;

    private ObjectReference shipClass;

    public Ship(GameState gameState, ShipClass sclass, double X, double Y, Vector v, UniversePath location) {
        super(gameState);
        this.X = X;
        this.Y = Y;
        goingToX = X;
        goingToY = Y;
        this.v = v;
        this.location = location;
        shipClass = sclass.getReference();
        //Set ship's id

        components = new ArrayList<>();
        //Get components
        if (!sclass.components.isEmpty()) {
            components.addAll(sclass.components);
        }
        this.hull = sclass.getHull();
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

    public ObjectReference getHull() {
        return hull;
    }

    public String getShipClassName() {
        return sclass;
    }

    public ObjectReference getShipClass() {
        return shipClass;
    }

    @Override
    public long getSpeed() {
        return estimatedThrust / mass;
    }
}
