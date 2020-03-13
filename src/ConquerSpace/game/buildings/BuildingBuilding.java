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

import ConquerSpace.game.jobs.Job;
import ConquerSpace.game.jobs.Workable;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.Good;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public class BuildingBuilding extends Building implements Workable{

    private Building toBuild;
    private GeographicPoint pt;
    private int length;
    private int scale = 1;
    //Set the resources needed to build over time
    public HashMap<Good, Integer> resourcesNeeded;
    public Civilization builder;
    private int cost;

    public BuildingBuilding(Building toBuild, GeographicPoint pt, int length, Civilization builder) {
        this.toBuild = toBuild;
        this.pt = pt;
        this.length = length;
        this.builder = builder;
        resourcesNeeded = new HashMap<>();
    }

    public void setToBuild(Building toBuild) {
        this.toBuild = toBuild;
    }

    public GeographicPoint getPt() {
        return pt;
    }

    public Building getToBuild() {
        return toBuild;
    }

    @Override
    public Color getColor() {
        return Color.PINK;
    }

    public void setPt(GeographicPoint pt) {
        this.pt = pt;
    }

    public int getLength() {
        return length;
    }

    public void decrementLength(int amount) {
        length -= amount;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

    @Override
    public void processJob(Job j) {
        decrementLength(1);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Civilization getBuilder() {
        return builder;
    }

    @Override
    public Job[] jobsNeeded() {
        return new Job[0];
    }
}
