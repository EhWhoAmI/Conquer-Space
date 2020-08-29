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
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.resources.ProductionProcess;

/**
 *
 * @author EhWhoAmI
 */
public class ManufacturerAreaFactory extends AreaFactory {
    protected ProductionProcess process;
    //How much of that production process per round...
    float productivity;
    
    public ManufacturerAreaFactory(Civilization builder) {
        super(builder);
    }

    public void setProcess(ProductionProcess process) {
        this.process = process;
    }

    public void setProductivity(float productivity) {
        this.productivity = productivity;
    }

    @Override
    public Area build(GameState gameState) {
        ManufacturerArea area = new ManufacturerArea(gameState, process, productivity);
        return setDefaultInformation(gameState, area);
    }
}
