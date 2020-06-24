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
package ConquerSpace.game.city.area;

import ConquerSpace.game.organizations.civilization.vision.VisionPoint;
import ConquerSpace.game.universe.UniversePath;

/**
 *
 * @author EhWhoAmI
 */
public class ObservatoryArea extends Area implements VisionPoint {

    private int civilization;
    private int range;
    private UniversePath position;

    public ObservatoryArea(int civilization, int range, UniversePath position) {
        this.civilization = civilization;
        this.range = range;
        this.position = position;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public int getCivilization() {
        return civilization;
    }

    @Override
    public UniversePath getPosition() {
        return position;
    }

    public void setCivilization(int civilization) {
        this.civilization = civilization;
    }

    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "Observatory";
    }
}
