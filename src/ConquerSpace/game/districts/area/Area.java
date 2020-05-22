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
package ConquerSpace.game.districts.area;

import ConquerSpace.game.population.jobs.JobType;
import ConquerSpace.game.population.jobs.Workable;

/**
 * Works as a modifier to the district
 */
public class Area implements Workable {

    private int currentlyManningJobs;
    private int operatingJobs;
    private int maxJobs;
    private int powerUsage;

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
        return (JobType.Jobless);
    }

    public int getCurrentlyManningJobs() {
        return currentlyManningJobs;
    }

    public void setCurrentlyManningJobs(int currentlyManningJobs) {
        this.currentlyManningJobs = currentlyManningJobs;
    }
}
