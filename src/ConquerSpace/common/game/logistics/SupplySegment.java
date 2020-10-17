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
package ConquerSpace.common.game.logistics;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;

/**
 *
 * @author EhWhoAmI
 */
public class SupplySegment extends ConquerSpaceGameObject{
    private ObjectReference point1;
    private ObjectReference point2;
    public SupplySegment(GameState state) {
        super(state);
        point1 = ObjectReference.INVALID_REFERENCE;
        point2 = ObjectReference.INVALID_REFERENCE;
    }

    public ObjectReference getPoint1() {
        return point1;
    }

    public void setPoint1(ObjectReference point1) {
        this.point1 = point1;
    }

    public ObjectReference getPoint2() {
        return point2;
    }

    public void setPoint2(ObjectReference point2) {
        this.point2 = point2;
    }
}
