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

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;

/**
 *
 * @author EhWhoAmI
 */
public class SupplyManager {
    public static void connectSegments(GameState gameState, ObjectReference ref1, ObjectReference ref2) {
        SupplyNode part1 = gameState.getObject(ref1, SupplyNode.class);
        SupplyNode part2 = gameState.getObject(ref2, SupplyNode.class);
        SupplySegment segment = new SupplySegment(gameState);
        part1.addSupplyConnection(segment.getReference());
        part2.addSupplyConnection(segment.getReference());
        segment.setPoint1(ref1);
        segment.setPoint2(ref2);
    }
}
