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

import ConquerSpace.game.StarDate;
import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.logistics.SupplyChain;
import ConquerSpace.game.logistics.SupplyNode;
import ConquerSpace.game.population.jobs.Employer;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.StorageNeeds;
import ConquerSpace.util.ResourceLoader;
import ConquerSpace.util.logging.CQSPLogger;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import org.apache.logging.log4j.Logger;

/**
 * A building is defined as a series of points
 *
 * @author EhWhoAmI
 */
public class District implements SupplyNode, PopulationStorage {

    private static int idCounter = 0;
    private static final Logger LOGGER = CQSPLogger.getLogger(District.class.getName());

    private int id;

    public ArrayList<Area> areas;
    private Employer owner;
    private String type;
    private City city;
    private HashMap<Good, Double> resources;
    public ArrayList<StorageNeeds> needs;
    //public ArrayList<PopulationUnit> population;
    private int maxStorage;
    public ArrayList<SupplyChain> supplyChains;

    //In Megawatts
    private int energyUsage;
    public ArrayList<InfrastructureBuilding> infrastructure;
    private DistrictType districtType = DistrictType.Generic;

    public District() {
        areas = new ArrayList<>();
        infrastructure = new ArrayList<>();
        supplyChains = new ArrayList<>();
        resources = new HashMap<>();
        id = idCounter++;
    }

    public DistrictType getDistrictType() {
        return districtType;
    }

    public void setDistrictType(DistrictType districtType) {
        this.districtType = districtType;
    }

    public Color getColor() {
        return new Color(0, 0, 255);
    }

    public void setOwner(Employer owner) {
        this.owner = owner;
    }

    public Employer getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    public int getEnergyUsage() {
        return energyUsage;
    }

    public void setEnergyUsage(int energyUsage) {
        this.energyUsage = energyUsage;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public String getTooltipText() {
        return "";
    }

    private static Properties tooltipStrings;

    static {
        tooltipStrings = new Properties();
        try {
            FileInputStream stream = new FileInputStream(ResourceLoader.getResourceByFile("config.buildings.tooltip"));
            tooltipStrings.load(stream);
        } catch (FileNotFoundException ex) {
            LOGGER.info("error", ex);
        } catch (IOException ex) {
            LOGGER.info("error", ex);
        }
    }

    protected final static String getBuildingTooltipString(String code) {
        String text = tooltipStrings.getProperty(code);
        if (text != null) {
            return text;
        }
        return "";
    }

    public void tick(StarDate date, long delta) {

    }

    @Override
    public ArrayList<SupplyChain> getSupplyChains() {
        return supplyChains;
    }

    //Describe position
    public UniversePath getUniversePath() {
        return city.getUniversePath();
    }

    @Override
    public void addResourceTypeStore(Good type) {
        resources.put(type, 0d);
    }

    @Override
    public Double getResourceAmount(Good type) {
        return resources.get(type);
    }

    @Override
    public void addResource(Good type, Double amount) {
        if (!resources.containsKey(type)) {
            resources.put(type, 0d);
        }
        resources.put(type, resources.get(type) + amount);
    }

    @Override
    public boolean canStore(Good type) {
        return true;//(resources.containsKey(type));
    }

    @Override
    public Good[] storedTypes() {
        Iterator<Good> res = resources.keySet().iterator();
        Good[] arr = new Good[resources.size()];
        int i = 0;
        while (res.hasNext()) {
            Good next = res.next();
            arr[i] = next;
            i++;
        }
        return arr;
    }

    @Override
    public boolean removeResource(Good type, Double amount) {
        //Get the amount in the place
        Double currentlyStored = resources.get(type);
        if (amount > currentlyStored) {
            return false;
        }

        resources.put(type, currentlyStored - amount);
        return true;
    }

    public void addArea(Planet p, Area a) {
        areas.add(a);
        if (a instanceof Workable) {
            p.jobProviders.add(a);
        }
    }

    /**
     * Literally doesn't even matter rn
     *
     * @return
     */
    @Override
    public int getMaxStorage() {
        return maxStorage;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof District) {
            return (((District) obj).id == this.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }
}