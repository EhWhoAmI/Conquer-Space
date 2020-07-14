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
package ConquerSpace.common.game.life;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import java.util.ArrayList;

/**
 * Species on a planet, can go to other planets
 *
 * @author EhWhoAmI
 */
public class Species extends ConquerSpaceGameObject{
    public ArrayList<LifeTrait> lifeTraits;

    private int speciesGood;
    private int foodGood;
    //Breeding rate
    private float baseBreedingRate = 0;
    //Breeding method
    //Etc...
    private String name;

    public Species(GameState gameState, String name) {
        super(gameState);
        //Add all the food goods...
        lifeTraits = new ArrayList<>();
        this.name = name;
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

    public void setFoodGood(int foodGood) {
        this.foodGood = foodGood;
    }

    public void setSpeciesGood(int speciesGood) {
        this.speciesGood = speciesGood;
    }

    @Override
    public String toString() {
        return name;
    }
}
