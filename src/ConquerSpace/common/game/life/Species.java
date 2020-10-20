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
import ConquerSpace.common.game.resources.FoodGood;
import ConquerSpace.common.game.resources.LiveGood;
import ConquerSpace.common.game.resources.StorableReference;
import ConquerSpace.common.save.SaveStuff;
import ConquerSpace.common.save.Serialize;
import ConquerSpace.common.save.SerializeClassName;
import java.util.ArrayList;

/**
 * Species on a planet, can go to other planets
 *
 * @author EhWhoAmI
 */
@SerializeClassName("species")
public class Species extends ConquerSpaceGameObject {

    @Serialize("traits")
    public ArrayList<LifeTrait> lifeTraits;

    @Serialize(value = "live-good", special = SaveStuff.Good)
    private StorableReference speciesGood;

    /**
     * Id of the food when it's dead
     */
    @Serialize(value = "food-good", special = SaveStuff.Good)
    private StorableReference foodGood;
    //Breeding rate

    @Serialize("breeding-rate")
    private float baseBreedingRate = 0;
    //Breeding method
    //Etc...

    @Serialize("name")
    private String name;

    public Species(GameState gameState, String name) {
        super(gameState);
        this.name = name;
        //Add all the food goods...
        //Nice
        FoodGood foodGoodResource = new FoodGood(this, 1, 420);
        LiveGood speciesGoodResource = new LiveGood(this, 1, 420);
        gameState.addGood(foodGoodResource);
        gameState.addGood(speciesGoodResource);

        setFoodGood(foodGoodResource.getId());
        setSpeciesGood(speciesGoodResource.getId());

        lifeTraits = new ArrayList<>();
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

    public StorableReference getFoodGood() {
        return foodGood;
    }

    public StorableReference getLiveSpeciesGood() {
        return speciesGood;
    }

    public void setFoodGood(StorableReference foodGood) {
        this.foodGood = foodGood;
    }

    public void setSpeciesGood(StorableReference speciesGood) {
        this.speciesGood = speciesGood;
    }

    @Override
    public String toString() {
        return name;
    }
}
