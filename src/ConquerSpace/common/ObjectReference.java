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
package ConquerSpace.common;

import java.io.Serializable;

/**
 *
 * @author EhWhoAmI
 */
public final class ObjectReference implements Serializable{
    public static final ObjectReference INVALID_REFERENCE = new ObjectReference(-1);
    private final int objectId;

    ObjectReference(int objectId) {
        this.objectId = objectId;
    }

    /**
     * Copies the reference
     * @param reference 
     */
    public ObjectReference(ObjectReference reference) {
        this.objectId = reference.objectId;
    }
    
    

    public int getId() {
        return objectId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObjectReference other = (ObjectReference) obj;
        if (this.objectId != other.objectId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return objectId;
    }

    @Override
    public String toString() {
        return Integer.toString(objectId);
    }
}
