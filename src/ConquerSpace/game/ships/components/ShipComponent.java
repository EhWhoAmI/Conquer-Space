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
package ConquerSpace.game.ships.components;

/**
 *
 * @author Zyun
 */
public class ShipComponent {
    //Mass in KG
    protected int mass;
    //Cost in credits
    protected int cost;
    protected String name;
    protected int id;
    protected int rating;
    protected int secondaryRating;

    public ShipComponent(int mass, int cost, String name) {
        this.mass = mass;
        this.cost = cost;
        this.name = name;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public int getMass() {
        return mass;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRatingType() {
        return "";
    }

    public int getSecondaryRating() {
        return secondaryRating;
    }

    public void setSecondaryRating(int secondaryRating) {
        this.secondaryRating = secondaryRating;
    }

    public String getSecondaryRatingType() {
        return "";
    }

    public Object clone() {
        return new ShipComponent(mass, cost, name);
    }
    
    @Override
    public String toString() {
        return getName();
    }
}