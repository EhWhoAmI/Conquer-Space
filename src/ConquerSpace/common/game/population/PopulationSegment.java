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

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.economy.GoodOrder;
import ConquerSpace.common.game.economy.Trader;
import ConquerSpace.common.save.SerializeClassName;
import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("population-segment")
public class PopulationSegment extends ConquerSpaceGameObject implements Trader {

    public long size = 0;
    public long workablePopulation = 0;

    public ObjectReference species;

    public ObjectReference culture;

    //Placeholder value for now...
    public int tier;

    /**
     * Population increase every tick
     */
    public float populationIncrease;

    private int wealth = 0;

    private ArrayList<GoodOrder> buyOrders;
    private ArrayList<GoodOrder> sellOrders;

    private ArrayList<ObjectReference> resourceStorages;
    private ArrayList<ObjectReference> ownedMeansOfProduction;

    public PopulationSegment(GameState gameState, ObjectReference species, ObjectReference culture) {
        super(gameState);
        this.species = species;
        this.culture = culture;
        buyOrders = new ArrayList<>();
        sellOrders = new ArrayList<>();
        resourceStorages = new ArrayList<>();
    }

    public ObjectReference getCulture() {
        return culture;
    }

    public long getSize() {
        return size;
    }

    public ObjectReference getSpecies() {
        return species;
    }

    @Override
    public int getWealth() {
        return wealth;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    @Override
    public ArrayList<GoodOrder> getRequests() {
        return buyOrders;
    }

    @Override
    public ArrayList<GoodOrder> getSellOrders() {
        return sellOrders;
    }

    public ArrayList<ObjectReference> getResourceStorages() {
        return resourceStorages;
    }

    public ArrayList<ObjectReference> getOwnedMeansOfProduction() {
        return ownedMeansOfProduction;
    }
}
