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
package ConquerSpace.game.districts;

import ConquerSpace.game.population.jobs.Job;
import ConquerSpace.game.population.jobs.JobRank;
import ConquerSpace.game.population.jobs.JobType;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.universe.resources.Good;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author EhWhoAmI
 */
public class ConstructingBuilding extends District {

    private District toBuild;
    private GeographicPoint pt;
    private int length;
    private int scale = 1;
    //Set the resources needed to build over time
    public HashMap<Good, Double> resourcesNeeded;
    public Civilization builder;
    private int cost;

    public ConstructingBuilding(District toBuild, GeographicPoint pt, int length, Civilization builder) {
        this.toBuild = toBuild;
        this.pt = pt;
        this.length = length;
        this.builder = builder;
        resourcesNeeded = new HashMap<>();
    }

    public void setToBuild(District toBuild) {
        this.toBuild = toBuild;
    }

    public GeographicPoint getPt() {
        return pt;
    }

    public District getToBuild() {
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Civilization getBuilder() {
        return builder;
    }

//    @Override
//    public Job[] jobsNeeded() {
//        ArrayList<Job> jobsNeeded = new ArrayList<>();
//
//        //Add construction jobs
//        int scale = getScale();
//        for (int i = 0; i < scale; i++) {
//            Job constructionJob = new Job(JobType.Construction);
//            constructionJob.setJobRank(JobRank.Low);
//            constructionJob.setEmployer(getOwner());
//            //Set them to use resources for the construction
//            for (Map.Entry<Good, Double> set : resourcesNeeded.entrySet()) {
//                Good resource = set.getKey();
//                Double amount = set.getValue();
//                //Add to the job
//                constructionJob.resources.put(resource, -amount);
//            }
//            //Add job to building
//            jobsNeeded.add(constructionJob);
//        }
//
//        Job[] jobArray = Arrays.copyOf(jobsNeeded.toArray(), jobsNeeded.size(), Job[].class);
//        return jobArray;
//    }
}
