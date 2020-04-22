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
package ConquerSpace.game;

import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.game.civilization.Civilization;

/**
 *
 * @author EhWhoAmI
 */
public class GameIndexer {
    Universe u;

    public GameIndexer(Universe u) {
        this.u = u;
    }
    
    public void index(Civilization c) {
        compileResourceStorages(c);
    }
    
    public void compileResourceStorages(Civilization c) {
        c.resourceStorages.clear();
        for(Planet p : c.habitatedPlanets) {
            p.buildings.forEach((a, b) -> c.resourceStorages.add(b));
        }
    }
    
    /**
     * Get people in civ.
     * @param c 
     */
    public void takeCensus(Civilization c) {
        
    }
}
