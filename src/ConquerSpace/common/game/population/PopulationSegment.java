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
package ConquerSpace.common.game.population;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.save.SerializeClassName;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("population-segment")
public class PopulationSegment extends ConquerSpaceGameObject{
    public long size = 0;
    
    public int species;
    
    public int culture;
    
    //Placeholder value for now...
    public int tier;
    
    /**
     * Population increase every tick
     */
    public float populationIncrease;

    
    public PopulationSegment(GameState gameState, int species, int culture) {
        super(gameState);
        this.species = species;
        this.culture = culture;
    }

    public int getCulture() {
        return culture;
    }

    public long getSize() {
        return size;
    }

    public int getSpecies() {
        return species;
    }
}
