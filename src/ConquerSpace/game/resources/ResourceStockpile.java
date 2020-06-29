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
package ConquerSpace.game.resources;

import ConquerSpace.game.universe.UniversePath;

/**
 *
 * @author EhWhoAmI
 */
public interface ResourceStockpile {
    public void addResourceTypeStore(Integer type);
    public Double getResourceAmount(Integer type);
    public void addResource(Integer type, Double amount);
    //Describe position
    public UniversePath getUniversePath();
    public boolean canStore(Integer type);
    
    public Integer[] storedTypes();
    
    public boolean removeResource(Integer type, Double amount);
}