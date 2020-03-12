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
package ConquerSpace.game.buildings.area.industrial;

import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.universe.goods.ProductionProcess;

/**
 *
 * @author EhWhoAmI
 */
public class Factory extends Area {

    ProductionProcess process;

    public Factory(ProductionProcess what) {
        this.process = what;
    }

    public ProductionProcess getProcess() {
        return process;
    }

    @Override
    public String toString() {
        return "Factory";
    }

    //And other upkeep stuff
}
