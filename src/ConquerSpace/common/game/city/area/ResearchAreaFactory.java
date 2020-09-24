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
 * Note: science is not included because the science can change, and can be
 * customize a lot, so there is no need for the factory to build the area.
 *
 * @author EhWhoAmI
 */
public class ResearchAreaFactory extends AreaFactory {

    private String name;

    public ResearchAreaFactory(Civilization builder) {
        super(builder);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Area build(GameState gameState) {
        ResearchArea area = new ResearchArea(gameState);
        area.setName(name);
        setDefaultInformation(gameState, area);
        return area;
    }

}
