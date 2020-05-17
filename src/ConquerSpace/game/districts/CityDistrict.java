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

import java.awt.Color;

/**
 *
 * @author EhWhoAmI
 */
public class CityDistrict extends District {

    private int maxStorage;

    public CityDistrict() {
    }

    public Color getColor() {
        return Color.BLUE;
    }

    public int getMaxStorage() {
        return maxStorage;
    }

    public void setMaxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
    }

    @Override
    public String getType() {
        return "City District";
    }

    @Override
    public String getTooltipText() {
        String txt = String.format(getBuildingTooltipString("citydistrict"), getCity().getName());
        return txt;
    }

//    @Override
//    public Job[] jobsNeeded() {
//        ArrayList<Job> jobsNeeded = new ArrayList<>();
//        //Infrastructure jobs
//        Job job = new Job(JobType.Infrastructure);
//        job.setJobRank(JobRank.Low);
//        job.setWorkingFor(this);
//        job.setEmployer(getOwner());
//        jobsNeeded.add(job);
//        
//        Job[] jobArray = Arrays.copyOf(jobsNeeded.toArray(), jobsNeeded.size(), Job[].class);
//        return jobArray;
//    }
    /**
     * CityDistrict city = (CityDistrict) building; if (building instanceof
     * AdministrativeCenter) { //An admin center deals with the planet, maybe
     * the galaxy if necessary //Add jobs that deal with admin
     * AdministrativeCenter center = (AdministrativeCenter) building; Job job =
     * new Job(JobType.Administrator); job.setJobRank(JobRank.High);
     * job.setWorkingFor(center); job.setEmployer(center.getOwner());
     * p.jobs.add(job); } Job job = new Job(JobType.Infrastructure);
     * job.setJobRank(JobRank.Low); job.setWorkingFor(city);
     * job.setEmployer(building.getOwner()); p.jobs.add(job);
     */

}
