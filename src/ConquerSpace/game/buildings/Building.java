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

import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.jobs.Employer;
import ConquerSpace.game.jobs.Job;
import ConquerSpace.game.jobs.Workable;
import ConquerSpace.util.ResourceLoader;
import ConquerSpace.util.logging.CQSPLogger;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import org.apache.logging.log4j.Logger;

/**
 * A building is defined as a series of points
 *
 * @author EhWhoAmI
 */
public abstract class Building implements Workable {

    private static final Logger LOGGER = CQSPLogger.getLogger(Building.class.getName());

    private Color color;
    public ArrayList<Area> areas;
    private Employer owner;
    private String type;
    private City city;

    //In Megawatts
    private int energyUsage;
    public ArrayList<InfrastructureBuilding> infrastructure;

    public Building() {
        areas = new ArrayList<>();
        infrastructure = new ArrayList<>();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    @Override
    @SuppressWarnings("unchecked")
    public Job[] jobsNeeded() {
        ArrayList<Job> jobsNeeded = new ArrayList();
        for (Area a : areas) {
            if (a instanceof Workable) {
                Job[] jobs = ((Workable) a).jobsNeeded();
                for (Job j : jobs) {
                    jobsNeeded.add(j);
                }
            }
        }
        Job[] jobArray = Arrays.copyOf(jobsNeeded.toArray(), jobsNeeded.size(), Job[].class);
        return jobArray;
    }

    @Override
    public void processJob(Job j) {

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
}
