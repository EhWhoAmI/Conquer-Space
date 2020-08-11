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
package ConquerSpace.common.game.universe.bodies;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.save.SerializeClassName;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("system-body")
public class StarSystemBody extends Body{
    private int index;
    
    private int parent;
    
    private ObjectReference parentId;

    public StarSystemBody(GameState gameState) {
        super(gameState);
    }
    
    void setIndex(int index){
        this.index = index;
    }

    /**
     * Get the index of this body in the star system.
     * @return 
     */
    public int getIndex() {
        return index;
    }

    public int getParentIndex() {
        return parent;
    }

    void setParent(int parent) {
        this.parent = parent;
    }

    public ObjectReference getParentReference() {
        return parentId;
    }

    void setParentId(ObjectReference parentId) {
        this.parentId = parentId;
    }
}
