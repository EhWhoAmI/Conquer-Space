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
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.jobs.Workable;
import ConquerSpace.game.universe.goods.Good;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import java.awt.Color;
import java.util.ArrayList;

/**
 * More like a miner. Desc: A district dedicated to the mining of a resource.
 *
 * @author EhWhoAmI
 */
public class ResourceMinerDistrict extends Building implements PopulationStorage, Workable {

    /**
     * Maximum jobs in this district.
     */
    private int scale;
    private ResourceVein veinMining;
    private double amount;
    private Good resourceMining;
    private int maxStorage;
    private City city;
    /*
    The population of the area...
     */
    public ArrayList<PopulationUnit> population;

    public ResourceMinerDistrict(ResourceVein vein, double amount) {
        this.veinMining = vein;
        if (vein != null) {
            resourceMining = vein.getResourceType();
        }
        this.amount = amount;
        population = new ArrayList<>();
    }

    public ResourceVein getVeinMining() {
        return veinMining;
    }

    public void setVeinMining(ResourceVein veinMining) {
        if (veinMining != null) {
            resourceMining = veinMining.getResourceType();
        }
        this.veinMining = veinMining;
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
        veinMining.removeResources((int)miningPerMonth());
        j.resources.put(resourceMining, (int)miningPerMonth());
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
}
