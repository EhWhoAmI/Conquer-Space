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
package ConquerSpace.common.game.science.city.area;

/**
 *
 * @author EhWhoAmI
 */
public class ConstructingArea extends Area{
    private int ticksLeft;
    private Area toBuild;

    public ConstructingArea(int ticks, Area toBuild) {
        this.ticksLeft = ticks;
        this.toBuild = toBuild;
    }

    public Area getToBuild() {
        return toBuild;
    }

    public int getTicksLeft() {
        return ticksLeft;
    }

    public void setTicksLeft(int ticksLeft) {
        this.ticksLeft = ticksLeft;
    }
    
    public void tickConstruction(int value) {
        ticksLeft -= value;
    }

    @Override
    public String toString() {
        return "Constructing " + toBuild.toString();
    }
}
