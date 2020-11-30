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
import ConquerSpace.common.game.resources.StorableReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author EhWhoAmI
 */
public class Market extends ConquerSpaceGameObject {

    private ArrayList<ObjectReference> traders;

    public HashMap<StorableReference, ArrayList<GoodOrder>> buyOrders;
    public HashMap<StorableReference, ArrayList<GoodOrder>> sellOrders;

    //The price history, now it's the current average price
    public HashMap<StorableReference, Double> price;

    public HashMap<StorableReference, Integer> demandMap;
    public HashMap<StorableReference, Integer> supplyMap;

    public HashMap<StorableReference, ArrayList<Double>> historicSDRatio;
    /**
     * Person who taxes go to lol
     */
    ObjectReference controller;
    EconomyType economyType = EconomyType.LassiezFaire;

    public Market(GameState gameState) {
        super(gameState);
        traders = new ArrayList<>();
        buyOrders = new HashMap<>();
        sellOrders = new HashMap<>();
        price = new HashMap<>();
        demandMap = new HashMap<>();
        supplyMap = new HashMap<>();
        historicSDRatio = new HashMap<>();
    }

    public void compileOrders() {
        buyOrders.clear();
        sellOrders.clear();

        //Compile stuff
        for (ObjectReference reference : traders) {
            Trader trader = gameState.getObject(reference, Trader.class);

            //Sort buy orders
            for (GoodOrder order : trader.getRequests()) {
                //Get good
                initializeGoodIfNonExistent(order.getGood(), buyOrders);

                ArrayList<GoodOrder> orderList = buyOrders.get(order.getGood());
                //Add order
                orderList.add(order);
            }

            //Sort sell orders
            for (GoodOrder order : trader.getSellOrders()) {
                //Get good
                initializeGoodIfNonExistent(order.getGood(), sellOrders);
                //Get hash map
                ArrayList<GoodOrder> orderList = sellOrders.get(order.getGood());
                //Add order
                orderList.add(order);
            }
        }

        //Then sort the maps
        for (Map.Entry<StorableReference, ArrayList<GoodOrder>> entry : buyOrders.entrySet()) {
            StorableReference key = entry.getKey();
            ArrayList<GoodOrder> val = entry.getValue();
            Collections.sort(val);
        }

        for (Map.Entry<StorableReference, ArrayList<GoodOrder>> entry : sellOrders.entrySet()) {
            StorableReference key = entry.getKey();
            ArrayList<GoodOrder> val = entry.getValue();
            Collections.sort(val);
            Collections.reverse(val);
        }

        //Done compiling
    }

    public void compileSupplyDemand() {
        HashMap<StorableReference, Integer> demand = new HashMap<>();
        HashMap<StorableReference, Integer> supply = new HashMap<>();
        HashSet<StorableReference> goods = new HashSet<>();
        //Calculate demand
        for (Map.Entry<StorableReference, ArrayList<GoodOrder>> entry : buyOrders.entrySet()) {
            StorableReference key = entry.getKey();
            ArrayList<GoodOrder> val = entry.getValue();
            int demandCount = 0;
            for (GoodOrder r : val) {
                demandCount += r.getAmount();
            }
            demand.put(key, demandCount);
            goods.add(key);
            demandMap.put(key, demandCount);
        }

        //Calculate supply
        for (Map.Entry<StorableReference, ArrayList<GoodOrder>> entry : sellOrders.entrySet()) {
            StorableReference key = entry.getKey();
            ArrayList<GoodOrder> val = entry.getValue();
            int supplyCount = 0;
            for (GoodOrder r : val) {
                supplyCount += r.getAmount();
            }
            supply.put(key, supplyCount);
            goods.add(key);
            supplyMap.put(key, supplyCount);
        }
        //Set cost if its not in the map
        
        for(StorableReference key : goods) {
            if(!price.containsKey(key)) {
                price.put(key, 100d);
            }
        }

        //Ratio between supply and demand
        //supply/demand
        //values less than 1 means a high supply, which means prices go down
        //Values above 1 means a high demand, which means prices go up
        for (StorableReference ref : goods) {
            double supplyDemand = 1;
            if (!supply.containsKey(ref)) {
                //Then 0 supply, so infinite demand
                supplyDemand = Double.MIN_VALUE;
                continue;
            }
            if (!demand.containsKey(ref)) {
                //no demand so infinite supply
                supplyDemand = Double.MAX_VALUE;
                continue;
            }
            supplyDemand = supply.get(ref) / demand.get(ref);
            //So recommended price is that
            //historicSDRatio.put(ref, supplyDemand);
            initializeGoodRatioIfNonExistent(ref, historicSDRatio);
            historicSDRatio.get(ref).add(supplyDemand);
        }
    }

    public void compileStats() {
        price.clear();

        //Get current prices
    }

    public void clearOrders() {
        buyOrders.clear();
        sellOrders.clear();
    }

    private void initializeGoodIfNonExistent(StorableReference reference, HashMap<StorableReference, ArrayList<GoodOrder>> map) {
        if (!map.containsKey(reference)) {
            map.put(reference, new ArrayList<>());
        }
    }
    
    private void initializeGoodRatioIfNonExistent(StorableReference reference, HashMap<StorableReference, ArrayList<Double>> map) {
        if (!map.containsKey(reference)) {
            map.put(reference, new ArrayList<>());
        }
    }
    

    public void addSellOrder(StorableReference reference, GoodOrder order) {
        initializeGoodIfNonExistent(reference, sellOrders);
        sellOrders.get(reference).add(order);
    }

    public void addBuyOrder(StorableReference reference, GoodOrder order) {
        initializeGoodIfNonExistent(reference, buyOrders);
        buyOrders.get(reference).add(order);
    }

    public void addTrader(Trader trader) {
        if (trader instanceof ConquerSpaceGameObject) {
            traders.add(((ConquerSpaceGameObject) trader).getReference());
        }
    }
}
