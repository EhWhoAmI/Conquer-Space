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
import java.util.ArrayList;

/**
 * Economy of a place, a place what wants to buy and sell stuff with the outside market
 *
 * @author EhWhoAmI
 */
public class Trader extends ConquerSpaceGameObject {

    /**
     * Wealth in actual cash
     * <p>
     */
    private int wealth;

    /**
     * What they want
     */
    ArrayList<GoodOrder> buyOrders;

    /**
     * What they want to get rid of
     */
    ArrayList<GoodOrder> sellOrders;

    public Trader(GameState gameState) {
        super(gameState);
        wealth = 0;
        buyOrders = new ArrayList<>();
        sellOrders = new ArrayList<>();
    }

    public int getWealth() {
        return wealth;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    public void setRequests(ArrayList<GoodOrder> requests) {
        this.buyOrders = requests;
    }

    public ArrayList<GoodOrder> getRequests() {
        return buyOrders;
    }
}
