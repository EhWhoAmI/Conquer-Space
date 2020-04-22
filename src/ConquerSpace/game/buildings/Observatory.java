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
package ConquerSpace.game.buildings;

import ConquerSpace.game.population.jobs.Job;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.civilization.vision.VisionPoint;
import java.awt.Color;

/**
 *
 * @author EhWhoAmI
 */
public class Observatory extends Building implements VisionPoint {

    private int range;
    private int lensSize;
    private int civ;
    private Point point;

    public Observatory(int range, int lensSize, int civ, Point point) {
        this.range = range;
        this.lensSize = lensSize;
        this.civ = civ;
        this.point = point;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public Color getColor() {
        return Color.white;
    }

    public int getLensSize() {
        return lensSize;
    }

    @Override
    public int getCivilization() {
        return civ;
    }

    public void setPosition(Point point) {
        this.point = point;
    }

    @Override
    public Point getPosition() {
        return point;
    }

    @Override
    public String getType() {
        return "Observatory";
    }

    @Override
    public String getTooltipText() {
        return String.format(getBuildingTooltipString("observatory"), range);
    }
    
    @Override
    public Job[] jobsNeeded() {
        return new Job[0];
    }
}
