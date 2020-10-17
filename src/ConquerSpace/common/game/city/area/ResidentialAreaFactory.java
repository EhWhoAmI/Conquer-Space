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
import ConquerSpace.common.game.organizations.Civilization;

/**
 *
 * @author EhWhoAmI
 */
public class ResidentialAreaFactory extends AreaFactory {

    int maxPopulation;

    public ResidentialAreaFactory(Civilization creator) {
        super(creator);
    }

    public void setMaxPopulation(int maxPopulation) {
        this.maxPopulation = maxPopulation;
    }

    public int getMaxPopulation() {
        return maxPopulation;
    }

    @Override
    public Area build(GameState gameState) {
        ResidentialArea area = new ResidentialArea(gameState);
        setDefaultInformation(gameState, area);
        area.maxPopulation = maxPopulation;
        return area;
    }
}
