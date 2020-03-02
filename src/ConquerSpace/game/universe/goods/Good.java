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
package ConquerSpace.game.universe.goods;

/**
 * Type of good that can be transported
 * @author zyunl
 */
public abstract class Good {
    String name;
    int id;
    double volume; // volume, m^3
    double mass; //mass, kg

    public Good(String name, int id, double volume, double mass) {
        this.name = name;
        this.id = id;
        this.volume = volume;
        this.mass = mass;
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
}
