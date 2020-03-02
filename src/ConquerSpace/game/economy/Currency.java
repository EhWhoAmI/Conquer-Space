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
package ConquerSpace.game.economy;

import ConquerSpace.game.universe.civilization.Civilization;

/**
 * A nations currency
 *
 * @author zyunl
 */
public class Currency {

    //Amount of isk you print
    private float inflation;

    //Total amount in circulation of that currency, all over the galaxy
    private long inCirculation;
    
    private String name;
    
    private String symbol;
    
    /**
     * The people who print the money...
     */
    private Civilization controller;
   
    public long getInCirculation() {
        return inCirculation;
    }

    public float getInflation() {
        return inflation;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setInCirculation(long inCirculation) {
        this.inCirculation = inCirculation;
    }

    public void setInflation(float inflation) {
        this.inflation = inflation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setController(Civilization controller) {
        this.controller = controller;
    }

    public Civilization getController() {
        return controller;
    }
}
