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
package ConquerSpace.game.city;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.city.area.Area;
import ConquerSpace.game.logistics.SupplyChain;
import ConquerSpace.game.organizations.Administrable;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonEnterable;
import ConquerSpace.game.population.Population;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.resources.ResourceStockpile;
import ConquerSpace.game.resources.StorageNeeds;
import ConquerSpace.game.save.Serialize;
import ConquerSpace.util.DoubleHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author EhWhoAmI
 */
public class City implements PersonEnterable, ResourceStockpile, Administrable {

    private static int idCounter = 0;
    
    @Serialize(key = "id")
    private final int id;

    @Serialize(key = "population")
    public Population population;
    public static final String CITY_DEFAULT = "emp";
    private Person governor;
    
    @Serialize(key = "name")
    private String name;
    
    @Serialize(key = "areas")
    public ArrayList<Area> areas;
    
    public ArrayList<Workable> workableFor;
    
    @Serialize(key = "people")
    public ArrayList<Person> peopleAtCity;

    @Serialize(key = "resources")
    public HashMap<Integer, Double> resources;
    
    @Serialize(key = "demands")
    public DoubleHashMap<Integer> resourceDemands;

    @Serialize(key = "storage-needs")
    public ArrayList<StorageNeeds> storageNeeds;
    //public ArrayList<PopulationUnit> population;
    private int maxStorage;
    public ArrayList<SupplyChain> supplyChains;
    
    private int ledgerClearDelta = 0;
    public HashMap<Integer, DoubleHashMap<String>> resourceLedger;
    private UniversePath location;

    @Serialize(key = "tags")
    public HashMap<String, Integer> tags;

    //% to completing a unit
    @Serialize(key = "population-completion")
    private float populationUnitPercentage = 0;

    //Growth rates of the species...
    //private HashMap<Race, Float> speciesRates;
    private boolean resetJobs = false;

    private CityType cityType;

    //Size in tiles
    @Serialize(key = "tiles")
    private int size;

    public City(UniversePath location) {
        workableFor = new ArrayList<>();
        areas = new ArrayList<>();
        storageNeeds = new ArrayList<>();
        resources = new HashMap<>();
        //jobProcessor = new JobProcessor();
        this.location = location;
        peopleAtCity = new ArrayList<>();
        population = new Population();
        resourceLedger = new HashMap<>();
        resourceDemands = new DoubleHashMap<>();
        tags = new HashMap<>();
        cityType = CityType.Generic;
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
    }

    @Override
    public void addResourceTypeStore(Integer type) {
        resources.put(type, 0d);
    }

    @Override
    public Double getResourceAmount(Integer type) {
        return resources.get(type);
    }

    @Override
    public void addResource(Integer type, Double amount) {
        if (!resources.containsKey(type)) {
            resources.put(type, 0d);
        }
        resources.put(type, resources.get(type) + amount);
        //Add to ledger
        if (resourceLedger.containsKey(type)) {
            DoubleHashMap<String> resource = resourceLedger.get(type);
            resource.addValue("added", (amount));
        } else {
            DoubleHashMap<String> resource = new DoubleHashMap<>();
            resource.put("added", amount);
            resourceLedger.put(type, resource);
        }
    }

    @Override
    public boolean canStore(Integer type) {
        return true;//(resources.containsKey(type));
    }

    @Override
    public Integer[] storedTypes() {
        Iterator<Integer> res = resources.keySet().iterator();
        Integer[] arr = new Integer[resources.size()];
        int i = 0;
        while (res.hasNext()) {
            Integer next = res.next();
            arr[i] = next;
            i++;
        }
        return arr;
    }

    @Override
    public boolean removeResource(Integer type, Double amount) {
        //Get the amount in the place
        if (!resources.containsKey(type)) {
            //Remove stuff for now
            //resources.put(type, amount);
            return false;
        }
        Double currentlyStored = resources.get(type);

        if (amount > currentlyStored) {
            return false;
        }

        resources.put(type, (currentlyStored - amount));
        //Add to ledger
        if (resourceLedger.containsKey(type)) {
            DoubleHashMap<String> resource = resourceLedger.get(type);
            resource.addValue("removed", -amount);
            resourceLedger.put(type, resource);
        } else {
            DoubleHashMap<String> resource = new DoubleHashMap<>();
            resource.addValue("removed", -amount);
            resourceLedger.put(type, resource);
        }
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

    public void incrementSize() {
        size++;
    }

    public void addArea(Area a) {
        areas.add(a);
    }

    public CityType getCityType() {
        return cityType;
    }

    public void setCityType(CityType cityType) {
        this.cityType = cityType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof City) {
            return (((City) obj).id == this.id);
        }
        return false;
    }

    public void clearLedger(int delta) {
        ledgerClearDelta = delta;
        resourceLedger.clear();
    }

    public int getLedgerClearDelta() {
        return ledgerClearDelta;
    }
}
