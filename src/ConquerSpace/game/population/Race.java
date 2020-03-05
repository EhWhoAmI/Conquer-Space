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
package ConquerSpace.game.population;

/**
 * Species or race
 * @author EhWhoAmI
 */
public class Race {
    //Usage of food per month
    private int foodPerMonth;
    //Base increase in population per year. Will be incremented per every couple of ticks
    private float breedingRate;

    private String name;
    
    //The amount of support a pop unit needs (as in pop)
    private float upkeep = 0;

    public Race(int foodPerMonth, float breedingRate, String name) {
        this.foodPerMonth = foodPerMonth;
        this.breedingRate = breedingRate;
        this.name = name;
    }

    public float getBreedingRate() {
        return breedingRate;
    }

    public int getFoodPerMonth() {
        return foodPerMonth;
    }

    public String getName() {
        return name;
    }

    public void setBreedingRate(float breedingRate) {
        this.breedingRate = breedingRate;
    }

    public void setFoodPerMonth(int foodPerMonth) {
        this.foodPerMonth = foodPerMonth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getUpkeep() {
        return upkeep;
    }

    public void setUpkeep(float upkeep) {
        this.upkeep = upkeep;
    }
}

