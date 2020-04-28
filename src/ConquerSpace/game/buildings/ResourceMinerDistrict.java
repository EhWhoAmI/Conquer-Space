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
import ConquerSpace.game.population.jobs.JobRank;
import ConquerSpace.game.population.jobs.JobType;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.Stratum;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * More like a miner. Desc: A district dedicated to the mining of a resource.
 *
 * @author EhWhoAmI
 */
public class ResourceMinerDistrict extends District implements PopulationStorage, Workable {

    /**
     * Maximum jobs in this district.
     */
    private int scale;
    private Stratum veinMining;
    private double amount;
    private Good resourceMining;
    private int maxStorage;
    private City city;
    /*
    The population of the area...
     */
    public ArrayList<PopulationUnit> population;

    public ResourceMinerDistrict(Stratum vein, double amount) {
        this.veinMining = vein;
        if (vein != null) {
            //resourceMining = vein.getResourceType();
        }
        this.amount = amount;
        population = new ArrayList<>();
    }

    public Stratum getVeinMining() {
        return veinMining;
    }

    public void setVeinMining(Stratum veinMining) {
        this.veinMining = veinMining;
    }

    public void setResourceMining(Good resourceMining) {
        this.resourceMining = resourceMining;
    }

    public double getAmountMined() {
        return amount;
    }

    public Good getResourceMining() {
        return resourceMining;
    }

    @Override
    public Color getColor() {
        return Color.YELLOW;
    }

    @Override
    public ArrayList<PopulationUnit> getPopulationArrayList() {
        return population;
    }

    @Override
    public int getMaxStorage() {
        return maxStorage;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

    public double miningPerMonth() {
        return (getAmountMined() / getScale());
    }

    @Override
    public void processJob(Job j) {
        //Is mining job, now subtract the stuff..
        //subtract from resource vein
        //veinMining.removeResources((int)miningPerMonth());
        j.resources.put(resourceMining, miningPerMonth());
    }

    @Override
    public String getType() {
        return "Mining District";
    }

    @Override
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String getTooltipText() {
        return String.format(getBuildingTooltipString("mine"), resourceMining.getName());
    }

    @Override
    public Job[] jobsNeeded() {
        ArrayList<Job> jobsNeeded = new ArrayList<>();
        //Add jobs
        for (int i = 0; i < getScale(); i++) {
            Job job = new Job(JobType.Miner);
            job.setJobRank(JobRank.Low);
            job.setWorkingFor(this);
            job.setEmployer(getOwner());
            jobsNeeded.add(job);
        }

        Job[] jobArray = Arrays.copyOf(jobsNeeded.toArray(), jobsNeeded.size(), Job[].class);
        return jobArray;
    }
}
