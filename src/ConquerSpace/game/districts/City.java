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
import ConquerSpace.game.districts.area.Area;
import ConquerSpace.game.logistics.SupplyChain;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonEnterable;
import ConquerSpace.game.population.Population;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import ConquerSpace.game.universe.resources.StorageNeeds;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author EhWhoAmI
 */
public class City implements PersonEnterable, ResourceStockpile {

    private static int idCounter = 0;
    private int id;
    
    public Population population;
    public static final String CITY_DEFAULT = "emp";
    private Person governor;
    private String name;
    public ArrayList<Area> areas;
    public ArrayList<Workable> jobs;
    public ArrayList<Person> peopleAtCity;
    
    private HashMap<Good, Double> resources;
    public ArrayList<StorageNeeds> storageNeeds;
    //public ArrayList<PopulationUnit> population;
    private int maxStorage;
    public ArrayList<SupplyChain> supplyChains;

    private UniversePath location;

    //% to completing a unit
    private float populationUnitPercentage = 0;

    //Growth rates of the species...
    //private HashMap<Race, Float> speciesRates;
    private boolean resetJobs = false;
    
    //Size in tiles
    private int size;

    public City(UniversePath location) {
        jobs = new ArrayList<>();
        areas = new ArrayList<>();
        storageNeeds = new ArrayList<>();
        resources = new HashMap<>();
        //jobProcessor = new JobProcessor();
        this.location = location;
        peopleAtCity = new ArrayList<>();
        population = new Population();
        size = 0;
        this.id = idCounter++;
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

    public Person getGovernor() {
        return governor;
    }

    public void setGovernor(Person governor) {
        governor.setRole("Governing " + name);
        this.governor = governor;
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
        return i;
    }

    @Override
    public ArrayList<Person> getPeopleArrayList() {
        return peopleAtCity;
    }

    public void incrementPopulation(StarDate date, long delta) {
        population.incrementPopulation(date, delta);
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

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public void incrementSize(){
        size++;
    }
    
    public void addArea(Area a) {
        areas.add(a);
    }
}
