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
package ConquerSpace.game.buildings.farm;

import ConquerSpace.game.life.Species;

/**
 * A crop that grows stuff.
 * @author EhWhoAmI
 */
public class Crop {
    private int timeLeft;
    private Species species;
    private int yield;
    
    public Crop(Species species) {
        this.species = species;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public Species getSpecies() {
        return species;
    }

    public int getYield() {
        return yield;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void setYield(int yield) {
        this.yield = yield;
    }
    
    public void subtractTime() {
        timeLeft--;
    }

    @Override
    public String toString() {
        return species.getName();
    }
}
