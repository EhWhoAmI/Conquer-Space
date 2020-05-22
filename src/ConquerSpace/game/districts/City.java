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
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonEnterable;
import ConquerSpace.game.population.Population;
import ConquerSpace.game.population.jobs.JobProcessor;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.universe.UniversePath;
import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
public class City implements PersonEnterable {

    public Population population;
    public static final String CITY_DEFAULT = "emp";
    private Person governor;
    private String name;
    public ArrayList<District> buildings;
    public ArrayList<Workable> jobs;
    public ArrayList<Person> peopleAtCity;

    private UniversePath location;

    //% to completing a unit
    private float populationUnitPercentage = 0;

    //Growth rates of the species...
    //private HashMap<Race, Float> speciesRates;
    private boolean resetJobs = false;

    private JobProcessor jobProcessor;

    public City(UniversePath location) {
        buildings = new ArrayList<>();
        jobs = new ArrayList<>();
        //jobProcessor = new JobProcessor();
        this.location = location;
        peopleAtCity = new ArrayList<>();
        population = new Population();
    }

    public void setPopulationUnitPercentage(float populationUnitPercentage) {
        this.populationUnitPercentage = populationUnitPercentage;
    }

    public float getPopulationUnitPercentage() {
        return populationUnitPercentage;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public UniversePath getUniversePath() {
        return location;
    }

    public void addDistrict(District stor) {
        buildings.add(stor);
        stor.setCity(this);        
    }

    public Person getGovernor() {
        return governor;
    }

    public void setGovernor(Person governor) {
        governor.setRole("Governing " + name);
        this.governor = governor;
    }

//    @Override
//    public Job[] jobsNeeded() {
//        ArrayList<Job> jobsNeeded = new ArrayList<>();
//        //Add city needed jobs
//
//        //Add all children jobs
//        for (District b : buildings) {
//            Job[] jobs = b.jobsNeeded();
//            for (Job j : jobs) {
//                jobsNeeded.add(j);
//            }
//        }
//
//        Job[] jobArray = Arrays.copyOf(jobsNeeded.toArray(), jobsNeeded.size(), Job[].class);
//        return jobArray;
//    }
    public boolean toResetJobs() {
        return resetJobs;
    }

    public void resetJobs() {
        resetJobs = true;
    }

    public void doneResettingJobs() {
        resetJobs = false;
    }

    public int getPopulationSize() {
        int i = 0;
        return i;
    }

    @Override
    public ArrayList<Person> getPeopleArrayList() {
        return peopleAtCity;
    }

    public void incrementPopulation(StarDate date, long delta) {
        population.incrementPopulation(date, delta);
    }
}
