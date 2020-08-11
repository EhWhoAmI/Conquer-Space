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

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author EhWhoAmI
 */
public class EntityManager {

    private HashMap<ObjectReference, ConquerSpaceGameObject> objects;

    private AtomicInteger objectCount;

    public EntityManager() {
        objects = new HashMap<>();
        objectCount = new AtomicInteger();
    }

    public ObjectReference addGameObject(ConquerSpaceGameObject object) {
        ObjectReference nextReference = new ObjectReference(objectCount.incrementAndGet());

        objects.put(nextReference, object);
        return nextReference;
    }

    public ConquerSpaceGameObject getObject(ObjectReference id) {
        return objects.get(id);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(ObjectReference id, Class<T> expectedClass) {
        ConquerSpaceGameObject o = objects.get(id);
        if (expectedClass.isInstance(o)) {
            return (T) o;
        }
        return null;
    }
    
    public int getEntitiyCount() {
        return objects.size();
    }
}
