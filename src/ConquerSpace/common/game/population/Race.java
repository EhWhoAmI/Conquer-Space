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
package ConquerSpace.common.game.population;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.life.Species;
import ConquerSpace.common.save.SaveStuff;
import ConquerSpace.common.save.Serialize;

/**
 * Sentient Species that can become a population.
 *
 * @author EhWhoAmI
 */
public class Race extends Species{
    @Serialize(key = "food-id", special = SaveStuff.Good)
    public Integer food;
    
    //Usage of food per month
    @Serialize(key = "food")
    private int foodPerMonth;
    //Base increase in population per year. Will be incremented per every couple of ticks
    
    @Serialize(key = "breeding-rate")
    private float breedingRate;

    //The amount of support a pop unit needs (as in pop)
    @Serialize(key = "upkeep")
    private float upkeep = 0;

    public Race(GameState state, int foodPerMonth, float breedingRate, String name) {
        super(state, name);
        
        //Set id
        this.foodPerMonth = foodPerMonth;
        this.breedingRate = breedingRate;
    }

    public float getBreedingRate() {
        return breedingRate;
    }

    public int getFoodPerMonth() {
        return foodPerMonth;
    }

    public void setBreedingRate(float breedingRate) {
        this.breedingRate = breedingRate;
    }

    public void setFoodPerMonth(int foodPerMonth) {
        this.foodPerMonth = foodPerMonth;
    }

    public float getUpkeep() {
        return upkeep;
    }

    public void setUpkeep(float upkeep) {
        this.upkeep = upkeep;
    }
}
