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

package ConquerSpace.common.game.economy;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.resources.StoreableReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public class Market extends ConquerSpaceGameObject implements ResourceStockpile {

    private ArrayList<ObjectReference> traders;

    private HashMap<StoreableReference, MarketEntry> marketPrices;

    private EconomyType economyType = EconomyType.LassiezFaire;

    public Market(GameState gameState) {
        super(gameState);
        traders = new ArrayList<>();
        marketPrices = new HashMap<>();
    }

    public void addTrader(Trader trader) {
        traders.add(trader.getReference());
    }

    public HashMap<StoreableReference, MarketEntry> getMarketPrices() {
        return marketPrices;
    }
    
    public void buyResource(StoreableReference ref, double amount) {
        
    }
    
    public void sellResource(StoreableReference ref, double amount) {
        //Get a function of the price
    }
    
    public double getResourceCost(StoreableReference ref) {
        return 1;//marketPrices.get(ref).getPrice();
    }

    @Override
    public void addResourceTypeStore(StoreableReference type) {
    }

    @Override
    public Double getResourceAmount(StoreableReference type) {
       return 0d;
    }

    @Override
    public void addResource(StoreableReference type, Double amount) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canStore(StoreableReference type) {
        return true;
    }

    @Override
    public boolean hasResource(StoreableReference type) {
        return true;
    }

    @Override
    public StoreableReference[] storedTypes() {
        return new StoreableReference[0];
    }

    @Override
    public boolean removeResource(StoreableReference type, Double amount) {
        return true;
    }

    @Override
    public String toString() {
        return "Market";
    }
}
