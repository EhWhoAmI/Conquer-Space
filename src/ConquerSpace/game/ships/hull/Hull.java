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
package ConquerSpace.game.ships.hull;

/**
 *
 * @author Zyun
 */
public class Hull {
    //KGS
    //Also determines hull hp and strength
    private long mass;
    //Meters cube
    private long space;
    
    private HullMaterial material;
    
    private int shipType;
    
    //Thrust in kilonewtons
    private long thrust;
    
    private String name;

    public Hull(long mass, long space, HullMaterial material, int shipType, long thrust, String name) {
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

    public HullMaterial getMaterial() {
        return material;
    }
    
    public float getMassToSpaceRatio() {
        return ((float)mass/(float)space);
    }
    
    public float getStrength() {
        return (getMassToSpaceRatio() * material.getStrength());
    }
    
    public boolean isValid() {
        return (getStrength()>=1);
    }

    public long getThrust() {
        return thrust;
    }

    public long getShipType() {
        return shipType;
    }

    public void setShipType(int shipType) {
        this.shipType = shipType;
    }

    public void setThrust(long thrust) {
        this.thrust = thrust;
    }

    public void setMaterial(HullMaterial material) {
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
        return new Hull(mass, space, material, shipType, thrust, name);
    }
    
    
}
