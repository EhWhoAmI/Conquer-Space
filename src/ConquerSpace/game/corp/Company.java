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
package ConquerSpace.game.corp;

import ConquerSpace.game.city.City;
import ConquerSpace.game.economy.Currency;
import ConquerSpace.game.population.jobs.Employer;
import ConquerSpace.game.universe.bodies.Planet;

/**
 *
 * @author EhWhoAmI
 */
public class Company implements Employer{
    private String name;
    private int affiliation;
    private City cityBasedIn;
    private Planet planetBasedIn;

    public Company() {
    }

    public String getName() {
        return name;
    }

    public int getAffiliation() {
        return affiliation;
    }

    public City getCityBasedIn() {
        return cityBasedIn;
    }

    public Planet getPlanetBasedIn() {
        return planetBasedIn;
    }

    public void setAffiliation(int affiliation) {
        this.affiliation = affiliation;
    }

    public void setCityBasedIn(City cityBasedIn) {
        this.cityBasedIn = cityBasedIn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlanetBasedIn(Planet planetBasedIn) {
        this.planetBasedIn = planetBasedIn;
    }

    @Override
    public Currency getCurrency() {
        return null;
    }

    @Override
    public long getMoney() {
        return 0;
    }

    @Override
    public void changeMoney(long amount) {
        //Do nothing!
    }
}
