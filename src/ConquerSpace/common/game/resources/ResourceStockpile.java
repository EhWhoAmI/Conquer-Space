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
package ConquerSpace.common.game.resources;

/**
 *
 * @author EhWhoAmI
 */
public interface ResourceStockpile {
    public void addResourceTypeStore(StoreableReference type);
    public Double getResourceAmount(StoreableReference type);
    public void addResource(StoreableReference type, Double amount);
    public boolean canStore(StoreableReference type);
    public boolean hasResource(StoreableReference type);
    
    public StoreableReference[] storedTypes();
    
    public boolean removeResource(StoreableReference type, Double amount);
    
    public void preResourceTransfer(StoreableReference type, Double amount, ResourceStockpile toWhere);
    public void postResourceTransfer(StoreableReference type, Double amount, ResourceStockpile toWhere);
}
