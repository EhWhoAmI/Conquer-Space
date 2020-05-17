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
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Admin center. Is a center for admin stuff, and also stores peeps. because it
 * extends pop storage, and it also is used for controlling politics and things
 * like that. Peeps live in them. Can be different between governments.
 *
 * @author
 */
public class AdministrativeCenter extends CityDistrict {

    @Override
    public Color getColor() {
        return Color.red;
    }

//    @Override
//    public void processJob(Job j) {
//    }

    @Override
    public String getType() {
        return "Administrative Center";
    }

//    @Override
//    public Job[] jobsNeeded() {
//        ArrayList<Job> jobsNeeded = new ArrayList<>();
//
//        Job job = new Job(JobType.Administrator);
//        job.setJobRank(JobRank.High);
//        job.setWorkingFor(this);
//        job.setEmployer(getOwner());
//        jobsNeeded.add(job);
//
//        Job[] jobArray = Arrays.copyOf(jobsNeeded.toArray(), jobsNeeded.size(), Job[].class);
//        return jobArray;
//    }
}
