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
package ConquerSpace.game.organizations.civilization;

import java.awt.Color;

/**
 * Configuration of the civilization.
 * @author EhWhoAmI
 */
public class CivilizationConfig {
    public Color civColor;
    public String civSymbol;
    public String homePlanetName;
    public String speciesName;
    public String civilizationName;
    public String civilizationPreferredClimate;
    public String civCurrencyName;
    public String civCurrencySymbol;

    public String getCivCurrencyName() {
        return civCurrencyName;
    }

    public String getCivCurrencySymbol() {
        return civCurrencySymbol;
    }

    public void setCivCurrencyName(String civCurrencyName) {
        this.civCurrencyName = civCurrencyName;
    }

    public void setCivCurrencySymbol(String civCurrencySymbol) {
        this.civCurrencySymbol = civCurrencySymbol;
    }

    public void setCivColor(Color civColor) {
        this.civColor = civColor;
    }

    public void setCivSymbol(String civSymbol) {
        this.civSymbol = civSymbol;
    }

    public void setCivilizationName(String civilizationName) {
        this.civilizationName = civilizationName;
    }

    public void setCivilizationPreferredClimate(String civilizationPreferredClimate) {
        this.civilizationPreferredClimate = civilizationPreferredClimate;
    }

    public void setHomePlanetName(String homePlanetName) {
        this.homePlanetName = homePlanetName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public Color getCivColor() {
        return civColor;
    }

    public String getCivSymbol() {
        return civSymbol;
    }

    public String getCivilizationName() {
        return civilizationName;
    }

    public String getCivilizationPreferredClimate() {
        return civilizationPreferredClimate;
    }

    public String getHomePlanetName() {
        return homePlanetName;
    }

    public String getSpeciesName() {
        return speciesName;
    }
}
