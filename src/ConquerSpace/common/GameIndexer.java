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

import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.universe.bodies.Galaxy;

/**
 *
 * @author EhWhoAmI
 */
public class GameIndexer {
    Galaxy u;

    public GameIndexer(Galaxy u) {
        this.u = u;
    }
    
    public void index(Civilization c) {
        compileResourceStorages(c);
    }
    
    public void compileResourceStorages(Civilization c) {
        c.resourceStorages.clear();
        //for(Planet p : c.habitatedPlanets) {
            //p.cities.stream().forEach(cty -> {c.resourceStorages.add(cty);});
        //}
    }
    
    /**
     * Get people in civ.
     * @param c 
     */
    public void takeCensus(Civilization c) {
        
    }
}
