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
package ConquerSpace.game.life;

import ConquerSpace.game.GameController;
import ConquerSpace.game.universe.resources.FoodGood;
import ConquerSpace.game.universe.resources.LiveGood;
import java.util.ArrayList;

/**
 * Species on a planet, can go to other planets
 * @author EhWhoAmI
 */
public class Species {
    
    private static int idCounter = 0;
    private int id = 0;
    public ArrayList<LifeTrait> lifeTraits;

    private LiveGood speciesGood;
    private FoodGood foodGood;
    //Breeding rate
    private float baseBreedingRate = 0;
    //Breeding method
    //Etc...
    private String name;
    
    public Species(String name) {
        id = idCounter++;
        lifeTraits = new ArrayList<>();
        this.name = name;
        //Nice
        foodGood = new FoodGood(this, 1, 420);
        speciesGood = new LiveGood(this, 1, 420);
        GameController.goodHashMap.put(foodGood.getId(), foodGood);
        GameController.goodHashMap.put(speciesGood.getId(), speciesGood);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public float getBaseBreedingRate() {
        return baseBreedingRate;
    }

    public void setBaseBreedingRate(float baseBreedingRate) {
        this.baseBreedingRate = baseBreedingRate;
    }

    public FoodGood getFoodGood() {
        return foodGood;
    }

    public LiveGood getLiveSpeciesGood() {
        return speciesGood;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }
}
