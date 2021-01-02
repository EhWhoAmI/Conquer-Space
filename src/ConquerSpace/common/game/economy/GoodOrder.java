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

import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.resources.StoreableReference;

/**
 *
 * @author EhWhoAmI
 */
public class GoodOrder implements Comparable<GoodOrder> {

    private StoreableReference good;
    private int amount;
    private double cost;
    private int age;
    private ObjectReference owner;

    public GoodOrder() {
        age = 0;
    }

    public StoreableReference getGood() {   
        return good;
    }

    public int getAmount() {
        return amount;
    }

    public double getCost() {
        return cost;
    }

    public void setGood(StoreableReference good) {
        this.good = good;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getAge() {
        return age;
    }

    public void incrementAge(int amout) {
        age += amount;
    }

    public void resetAge() {
        age = 0;
    }

    @Override
    public int compareTo(GoodOrder o) {
        return Double.compare(cost, o.getCost());
    }

    public void setOwner(ObjectReference owner) {
        this.owner = owner;
    }

    public ObjectReference getOwner() {
        return owner;
    }
}
