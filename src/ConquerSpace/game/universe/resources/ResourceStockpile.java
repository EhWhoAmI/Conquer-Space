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
package ConquerSpace.game.universe.resources;

import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.resources.Good;

/**
 *
 * @author Zyun
 */
public interface ResourceStockpile {
    public void addResourceTypeStore(Good type);
    public int getResourceAmount(Good type);
    public void addResource(Good type, int amount);
    //Describe position
    public UniversePath getUniversePath();
    public boolean canStore(Good type);
    
    public Good[] storedTypes();
    
    public boolean removeResource(Good type, int amount);
}
