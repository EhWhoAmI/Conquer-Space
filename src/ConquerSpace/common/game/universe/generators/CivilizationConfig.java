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
package ConquerSpace.common.game.universe.generators;

import ConquerSpace.common.game.population.RacePreferredClimateTpe;
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
    public RacePreferredClimateTpe civilizationPreferredClimate;
    
    public String civCurrencyName;
    public String civCurrencySymbol;
}
