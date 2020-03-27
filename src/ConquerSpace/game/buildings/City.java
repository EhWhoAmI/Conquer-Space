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
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonEnterable;
import ConquerSpace.game.jobs.Job;
import ConquerSpace.game.jobs.JobProcessor;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.jobs.Workable;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.util.DistributedRandomNumberGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author EhWhoAmI
 */
public class City implements PersonEnterable, Workable {

    private Person governor;
    private String name;
    public ArrayList<Building> buildings;
    public ArrayList<Job> jobs;
    public ArrayList<Person> peopleAtCity;

    private UniversePath location;

    //% to completing a unit
    private float populationUnitPercentage = 0;

    //Growth rates of the species...
    private HashMap<Race, Float> speciesRates;

    private boolean resetJobs = false;

    private JobProcessor jobProcessor;

    public City(UniversePath location) {
        buildings = new ArrayList<>();
        jobs = new ArrayList<>();
        //jobProcessor = new JobProcessor();
        this.location = location;
        peopleAtCity = new ArrayList<>();
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

    public void addDistrict(Building stor) {
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

    @Override
    public Job[] jobsNeeded() {
        ArrayList<Job> jobsNeeded = new ArrayList<>();
        //Add city needed jobs

        //Add all children jobs
        for (Building b : buildings) {
            Job[] jobs = b.jobsNeeded();
            for (Job j : jobs) {
                jobsNeeded.add(j);
            }
        }

        Job[] jobArray = Arrays.copyOf(jobsNeeded.toArray(), jobsNeeded.size(), Job[].class);
        return jobArray;
    }

    @Override
    public void processJob(Job j) {
        //Working for the city itself...
    }

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
        for (Building b : buildings) {
            if (b instanceof PopulationStorage) {
                i += ((PopulationStorage) b).getPopulationArrayList().size();
            }
        }
        return i;
    }

    @Override
    public ArrayList<Person> getPeopleArrayList() {
        return peopleAtCity;
    }

    public void incrementPopulation(StarDate date, long delta) {
        float increment = 0;
        increment += getPopulationUnitPercentage();

        HashMap<Race, Integer> species = new HashMap<>();
        ArrayList<PopulationStorage> storages = new ArrayList<>();
        int population = 0;
        for (Building building : buildings) {
            if (building instanceof PopulationStorage) {
                PopulationStorage storage = (PopulationStorage) building;
                storages.add(storage);
                for (PopulationUnit unit : storage.getPopulationArrayList()) {
                    //Population increment
                    //Fraction it so it does not accelerate at a crazy rate
                    //Do subtractions here in the future, like happiness, and etc.
                    increment += (unit.getSpecies().getBreedingRate() / 50);
                    //Add to hashmap
                    if (species.containsKey(unit.getSpecies())) {
                        //Add to it...
                        Integer count = species.get(unit.getSpecies());
                        count++;
                        species.put(unit.getSpecies(), count);
                    } else {
                        species.put(unit.getSpecies(), 1);
                    }
                    population++;
                }
            }
        }

        //Increment the value...
        setPopulationUnitPercentage(increment);
        if (increment > 100) {
            //Add population to city and stuff.
            //Get the species in the city...

            //Add to storage
            //Sum everything together for random numbers
            DistributedRandomNumberGenerator generator = new DistributedRandomNumberGenerator();
            int i = 0;
            HashMap<Integer, Race> races = new HashMap<>();
            for (Map.Entry<Race, Integer> entry : species.entrySet()) {
                Race key = entry.getKey();
                Integer value = entry.getValue();
                generator.addNumber(i, ((double) value / (double) population));
                races.put(i, key);
                i++;
            }

            //Get the race
            int speciesID = generator.getDistributedRandomNumber();
            Race r = races.get(speciesID);
            PopulationUnit unit = new PopulationUnit(r);

            //Increment population
            int storageID = (int) (Math.random() * storages.size());
            storages.get(storageID).getPopulationArrayList().add(unit);

            setPopulationUnitPercentage(0);
        }
    }
}
