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

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.game.population.jobs.Workable;
import ConquerSpace.common.save.Serialize;
import ConquerSpace.common.save.SerializeClassName;

/**
 * Works as a modifier to the district
 */
@SerializeClassName("area")
public abstract class Area extends ConquerSpaceGameObject implements Workable, Comparable<Area> {

    /**
     * The number of people currently manning the place.
     */
    @Serialize("manning")
    private int currentlyManningJobs;

    @Serialize("operating")
    private int operatingJobs;

    @Serialize("max")
    private int maxJobs;

    @Serialize("power")
    private int powerUsage;

    //The org owning the thing
    @Serialize("owner")
    private ObjectReference owner = ObjectReference.INVALID_REFERENCE;

    //Resource request
    @Serialize("resource-priority")
    protected int priority = Integer.MAX_VALUE;

    /**
     * This is how productive the area is when it is at only <code> currentlyManningJobs</code>.
     */
    @Serialize("productivity")
    private float workingmultiplier = 1;

    Area(GameState gameState) {
        super(gameState);
    }

    public AreaClassification getAreaType() {
        return AreaClassification.Generic;
    }

    public int getPowerUsage() {
        return powerUsage;
    }

    public void setPowerUsage(int powerUsage) {
        this.powerUsage = powerUsage;
    }

    @Override
    public int operatingJobsNeeded() {
        return operatingJobs;
    }

    @Override
    public int getMaxJobsProvided() {
        return maxJobs;
    }

    public void setMaxJobs(int maxJobs) {
        this.maxJobs = maxJobs;
    }

    public void setOperatingJobs(int operatingJobs) {
        this.operatingJobs = operatingJobs;
    }

    @Override
    public JobType getJobClassification() {
        return (JobType.Imaginary);
    }

    public int getCurrentlyManningJobs() {
        return currentlyManningJobs;
    }

    public void setCurrentlyManningJobs(int currentlyManningJobs) {
        this.currentlyManningJobs = currentlyManningJobs;
    }

    public void setWorkingmultiplier(float workingmultiplier) {
        this.workingmultiplier = workingmultiplier;
    }

    public int getPriority() {
        return priority;
    }

    void setPriority(int priority) {
        this.priority = priority;
    }

    public float getWorkingmultiplier() {
        return workingmultiplier;
    }

    @Override
    public int compareTo(Area o) {
        return Integer.compare(priority, o.priority);
    }

    public void setOwner(ObjectReference owner) {
        this.owner = owner;
    }

    public ObjectReference getOwner() {
        return owner;
    }

    public abstract void accept(AreaDispatcher dispatcher);
}
