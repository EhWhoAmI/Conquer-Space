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
package ConquerSpace.game.resources;

/**
 * Type of good that can be transported
 * @author EhWhoAmI
 */
public abstract class Good implements Comparable<Good>{
    private static int idCounter = 0;
    
    //On Screen name
    String name;
    //Unique identifier for human readable things
    String identifier;
    int id;
    double volume; // volume, m^3
    double mass; //mass, kg
    public String[] tags = new String[0];
    private boolean fractionable;

    public Good(String name, String identifier, double volume, double mass) {
        this.id = idCounter++;
        this.name = name;
        this.volume = volume;
        this.mass = mass;
        this.identifier = identifier;
        fractionable = false;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getMass() {
        return mass;
    }

    public double getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Good && ((Good) obj).name.equals(this.name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Good o) {
        return (o.name.compareTo(name));
    }

    public boolean isFractionable() {
        return fractionable;
    }

    public void setFractionable(boolean fractionable) {
        this.fractionable = fractionable;
    }

    public String getIdentifier() {
        return identifier;
    }
}
