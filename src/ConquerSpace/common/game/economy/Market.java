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
import ConquerSpace.common.game.resources.GoodReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author EhWhoAmI
 */
public class Market extends ConquerSpaceGameObject {

    private ArrayList<ObjectReference> traders;

    public HashMap<GoodReference, ArrayList<GoodOrder>> buyOrders;
    public HashMap<GoodReference, ArrayList<GoodOrder>> sellOrders;

    //The price history
    public HashMap<GoodReference, Double> historicPrices;

    public Market(GameState gameState) {
        super(gameState);
        traders = new ArrayList<>();
        buyOrders = new HashMap<>();
        sellOrders = new HashMap<>();
        historicPrices = new HashMap<>();
    }

    public void compileOrders() {
        buyOrders.clear();
        sellOrders.clear();

        //Compile stuff
        for (ObjectReference reference : traders) {
            Trader trader = gameState.getObject(reference, Trader.class);

            //Sort buy orders
            for (GoodOrder order : trader.buyOrders) {
                //Get good
                initializeGoodIfNonExistant(order.getGood(), buyOrders);
                //Get hash map
                ArrayList<GoodOrder> orderList = buyOrders.get(order.getGood());
                //Add order
                orderList.add(order);
            }

            //Sort sell orders
            for (GoodOrder order : trader.sellOrders) {
                //Get good
                initializeGoodIfNonExistant(order.getGood(), sellOrders);
                //Get hash map
                ArrayList<GoodOrder> orderList = sellOrders.get(order.getGood());
                //Add order
                orderList.add(order);
            }
        }

        //Then sort the maps
        for (Map.Entry<GoodReference, ArrayList<GoodOrder>> entry : buyOrders.entrySet()) {
            Object key = entry.getKey();
            ArrayList<GoodOrder> val = entry.getValue();
            Collections.sort(val);
        }

        for (Map.Entry<GoodReference, ArrayList<GoodOrder>> entry : sellOrders.entrySet()) {
            Object key = entry.getKey();
            ArrayList<GoodOrder> val = entry.getValue();
            Collections.sort(val);
            Collections.reverse(val);
        }

        //Done compiling
    }

    public void compileStats() {
        historicPrices.clear();
        
    }

    private void initializeGoodIfNonExistant(GoodReference reference, HashMap<GoodReference, ArrayList<GoodOrder>> map) {
        if (!map.containsKey(reference)) {
            map.put(reference, new ArrayList<>());
        }
    }
}
