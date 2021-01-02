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
package ConquerSpace.common.game.city.area;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.resources.StoreableReference;

/**
 *
 * @author EhWhoAmI
 */
public class MineAreaFactory extends AreaFactory{

    private StoreableReference resourceMined;
    private ObjectReference miningStratum;
    private float productivity;
    
    public MineAreaFactory(Civilization creator) {
        super(creator);
    }

    public void setMiningStratum(ObjectReference miningStratum) {
        this.miningStratum = miningStratum;
    }

    public void setResourceMined(StoreableReference resourceMined) {
        this.resourceMined = resourceMined;
    }

    public void setProductivity(float productivity) {
        this.productivity = productivity;
    }

    @Override
    public Area build(GameState gameState) {
        MineArea area = new MineArea(gameState, miningStratum, resourceMined, productivity);
        setDefaultInformation(gameState, area);
        return area;
    }
}
