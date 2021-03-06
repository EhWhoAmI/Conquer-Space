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

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.save.SerializeClassName;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("hull")
public class Hull extends ConquerSpaceGameObject implements Cloneable {

    //KGS
    //Also determines hull hp and strength
    private long mass;
    //Meters cube
    private long space;

    private ObjectReference material;

    private ShipType shipType;

    //Thrust in kilonewtons
    private long thrust;

    private String name;

    public Hull(GameState gameState, long mass, long space, ObjectReference material, ShipType shipType, long thrust, String name) {
        super(gameState);
        this.mass = mass;
        this.space = space;
        this.material = material;
        this.shipType = shipType;
        this.thrust = thrust;
        this.name = name;
    }

    public long getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public long getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public ObjectReference getMaterial() {
        return material;
    }

    public float getMassToSpaceRatio() {
        return ((float) mass / (float) space);
    }

    public float getStrength() {
        return (getMassToSpaceRatio() * gameState.getObject(material, HullMaterial.class).getStrength());
    }

    public boolean isValid() {
        return (getStrength() >= 1);
    }

    public long getThrust() {
        return thrust;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public void setThrust(long thrust) {
        this.thrust = thrust;
    }

    public void setMaterial(ObjectReference material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setMass(long mass) {
        this.mass = mass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpace(long space) {
        this.space = space;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public Object clone() {
        return new Hull(gameState, mass, space, getMaterial(), shipType, thrust, name);
    }

}
