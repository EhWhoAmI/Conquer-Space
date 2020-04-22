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

import ConquerSpace.game.StarDate;
import ConquerSpace.game.population.jobs.Job;
import ConquerSpace.game.population.jobs.JobRank;
import ConquerSpace.game.population.jobs.JobType;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.buildings.farm.Crop;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Farms have 2 types, a crop one and a livestock one, maybe a combined one.
 * We'll see.
 *
 * @author EhWhoAmI
 */
public class FarmBuilding extends Building implements PopulationStorage, Workable {

    private FarmType farmType;
    public ArrayList<Crop> crops;
    private int productivity = 0;
    public ArrayList<PopulationUnit> population;
    private int capacity = 0;
    private int maxCapacity = 0;
    private int manPower = 0;
    private int amountFarmed = 0;
    private int harvestersNeeded = 0;
    public ArrayList<Crop> harvestable;
    private City city;

    public FarmBuilding(FarmType ft) {
        farmType = ft;
        population = new ArrayList<>();
        crops = new ArrayList<>();
        harvestable = new ArrayList<>();
    }

    @Override
    public ArrayList<PopulationUnit> getPopulationArrayList() {
        return population;
    }

    @Override
    public int getMaxStorage() {
        return 1;
    }

    @Override
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public Job[] jobsNeeded() {
        ArrayList<Job> jobsNeeded = new ArrayList<>();

        for (int i = 0; i < getManpower(); i++) {
            Job job = new Job(JobType.Farmer);
            job.setJobRank(JobRank.Low);
            job.setWorkingFor(this);
            //Set pay
            job.setPay(1);
            job.setEmployer(getOwner());
            jobsNeeded.add(job);
        }

        Job[] jobArray = Arrays.copyOf(jobsNeeded.toArray(), jobsNeeded.size(), Job[].class);
        return jobArray;
    }

    public static enum FarmType {
        Livestock,
        Crop;
    }

    public int getProductivity() {
        return productivity;
    }

    public void setProductivity(int productivity) {
        this.productivity = productivity;
    }

    public FarmType getFarmType() {
        return farmType;
    }

    public void setFarmType(FarmType farmType) {
        this.farmType = farmType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int maxCapacity) {
        this.capacity = maxCapacity;
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void processJob(Job j) {
        //Harvest a little
        //Check if harvest season...
        if (!harvestable.isEmpty()) {
            Crop c = harvestable.remove(0);
            j.resources.put(c.getSpecies().foodGood, Double.valueOf(c.getYield()));
            //Regrow
            c.setTimeLeft(25);
        }
    }

    public int getManpower() {
        return manPower;
    }

    public void setManpower(int manPower) {
        this.manPower = manPower;
    }

    @Override
    public String getType() {
        return "Farm";
    }

    public void setManPower(int manPower) {
        this.manPower = manPower;
    }

    public void setAmountFarmed(int amountFarmed) {
        this.amountFarmed = amountFarmed;
    }

    public int getAmountFarmed() {
        return amountFarmed;
    }

    public void setHarvestersNeeded(int harvestersNeeded) {
        this.harvestersNeeded = harvestersNeeded;
    }

    public int getHarvestersNeeded() {
        return harvestersNeeded;
    }

    @Override
    public String getTooltipText() {
        return getBuildingTooltipString("farmbuilding");
    }

    @Override
    public void tick(StarDate date, long delta) {
        //Get the resources
        //Calculate productivity
        setHarvestersNeeded(0);

        int yield = 0;
        for (Crop c : crops) {
            c.subtractTime();
            if (c.getTimeLeft() <= 0) {
                //Prepare crop for harvesting
                harvestable.add(c);

                //Check for harvesters
                setHarvestersNeeded(getHarvestersNeeded() + 1);
            }
        }
        setAmountFarmed(yield);
    }
}
