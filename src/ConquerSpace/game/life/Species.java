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
import ConquerSpace.game.resources.FoodGood;
import ConquerSpace.game.resources.LiveGood;
import java.util.ArrayList;

/**
 * Species on a planet, can go to other planets
 *
 * @author EhWhoAmI
 */
public class Species {

    private static int idCounter = 0;
    private int id = 0;
    public ArrayList<LifeTrait> lifeTraits;

    private int speciesGood;
    private int foodGood;
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
        FoodGood foodGoodResource = new FoodGood(this, 1, 420);
        LiveGood speciesGoodResource = new LiveGood(this, 1, 420);
        GameController.goodHashMap.put(foodGoodResource.getId(), foodGoodResource);
        GameController.goodHashMap.put(speciesGoodResource.getId(), speciesGoodResource);
        speciesGood = speciesGoodResource.getId();
        foodGood = foodGoodResource.getId();
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

    public int getFoodGood() {
        return foodGood;
    }

    public int getLiveSpeciesGood() {
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
