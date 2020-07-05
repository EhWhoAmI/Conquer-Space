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
package ConquerSpace.game.city.area;

import ConquerSpace.game.logistics.ResourcePermissions;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public class ResourceStockpileArea extends Area{
    private ResourcePermissions defaultPermissions;
    private HashMap<Integer, ResourcePermissions> allPermissions;

    public ResourceStockpileArea() {
        allPermissions = new HashMap<>();
        defaultPermissions = new ResourcePermissions(false, false, false);
    }
    
    public ResourcePermissions getPermission(Integer person) {
        if(allPermissions.containsKey(person)) {
            return allPermissions.get(person);
        }
        return defaultPermissions;
    }
    
    public void addPermission(Integer person, ResourcePermissions permission) {
        allPermissions.put(person, permission);
    }
}
