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
package ConquerSpace.common.game.science.logistics;

import ConquerSpace.common.util.Utilities;

/**
 *
 * @author EhWhoAmI
 */
public class ResourcePermissions {

    public static final ResourcePermissions OWNER_PERMISSIONS = new ResourcePermissions(true, true, true);
    /**
     * Can get the number of resources in the stockpile.
     */
    boolean read;
    /**
     * Can put resources.
     */
    boolean place;
    /**
     * Can remove resources.
     */
    boolean remove;

    public ResourcePermissions(boolean read, boolean place, boolean remove) {
        this.read = read;
        this.place = place;
        this.remove = remove;
    }

    public ResourcePermissions() {
        this.read = false;
        this.place = false;
        this.remove = false;
    }

    public boolean isPlace() {
        return place;
    }

    public boolean isRead() {
        return read;
    }

    public boolean isRemove() {
        return remove;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResourcePermissions) {
            ResourcePermissions perm = (ResourcePermissions) obj;
            return (this.place == perm.place && this.read == perm.read && this.remove == perm.remove);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return ((Utilities.boolToInt(read)) + (Utilities.boolToInt(place) << 1) + (Utilities.boolToInt(remove) << 2));
    }
}
