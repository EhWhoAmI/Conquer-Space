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

import ConquerSpace.game.GameController;
import ConquerSpace.game.StarDate;
import ConquerSpace.game.jobs.Job;
import ConquerSpace.game.jobs.JobRank;
import ConquerSpace.game.jobs.JobType;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author EhWhoAmI
 */
public class SpacePort extends Building {

    public ArrayList<SpacePortLaunchPad> launchPads = new ArrayList<>();
    private LaunchSystem system;

    public SpacePort(LaunchSystem system, int amount) {
        this.system = system;
        launchPads = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            launchPads.add(new SpacePortLaunchPad(system));
        }
    }

    @Override
    public Color getColor() {
        return Color.MAGENTA;
    }

    @Override
    public String getType() {
        return "Space Port";
    }

    @Override
    public void tick(StarDate date, long delta) {
        //Iterate through launchpads and process
        for (SpacePortLaunchPad splp : launchPads) {
            splp.ticks += GameController.GameRefreshRate;
        }
    }

    @Override
    public Job[] jobsNeeded() {
        ArrayList<Job> jobsNeeded = new ArrayList<>();

        Job job = new Job(JobType.SpacePortEngineer);
        job.setEmployer(getOwner());
        job.setJobRank(JobRank.Medium);
        jobsNeeded.add(job);

        Job[] jobArray = Arrays.copyOf(jobsNeeded.toArray(), jobsNeeded.size(), Job[].class);
        return jobArray;
    }

}
