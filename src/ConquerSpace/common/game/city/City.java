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
package ConquerSpace.common.game.city;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.StarDate;
import ConquerSpace.common.game.characters.Person;
import ConquerSpace.common.game.characters.PersonEnterable;
import ConquerSpace.common.game.organizations.Administrable;
import ConquerSpace.common.game.population.Population;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.resources.StorageNeeds;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.save.SaveStuff;
import ConquerSpace.common.save.Serialize;
import ConquerSpace.common.save.SerializeClassName;
import ConquerSpace.common.util.DoubleHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("city")
public class City extends ConquerSpaceGameObject implements PersonEnterable, ResourceStockpile, Administrable {
    @Serialize("population")
    public Integer population;
    
    public static final String CITY_DEFAULT = "emp";
    
    private Integer governor;
    
    @Serialize("name")
    private String name;
    
    @Serialize("areas")
    public ArrayList<Integer> areas;
    
    @Serialize("working-for")
    public ArrayList<Integer> workableFor;
    
    @Serialize("people")
    public ArrayList<Person> peopleAtCity;

    @Serialize(value = "resources", special = SaveStuff.Good)
    public HashMap<Integer, Double> resources;
    
    @Serialize(value = "demands", special = SaveStuff.Good)
    public DoubleHashMap<Integer> resourceDemands;

    @Serialize("storage-needs")
    public ArrayList<StorageNeeds> storageNeeds;
    //public ArrayList<PopulationUnit> population;
    
    @Serialize("max-storage")
    private int maxStorage;
    
    public ArrayList<Integer> supplyChains;
    
    private int ledgerClearDelta = 0;
    public HashMap<Integer, DoubleHashMap<String>> resourceLedger;
    
    @Serialize("location")
    private Integer location;

    @Serialize("tags")
    public HashMap<String, Integer> tags;

    //% to completing a unit
    @Serialize("population-completion")
    private float populationUnitPercentage = 0;

    //Growth rates of the species...
    //private HashMap<Race, Float> speciesRates;
    private boolean resetJobs = false;

    @Serialize("city-type")
    private CityType cityType;

    //Size in tiles
    @Serialize("tiles")
    private int size;

    public City(GameState gameState, Integer location) {
        super(gameState);
        workableFor = new ArrayList<>();
        areas = new ArrayList<>();
        storageNeeds = new ArrayList<>();
        resources = new HashMap<>();
        //jobProcessor = new JobProcessor();
        this.location = location;
        peopleAtCity = new ArrayList<>();
        
        Population population = new Population(gameState);
        this.population = population.getId();
        
        resourceLedger = new HashMap<>();
        resourceDemands = new DoubleHashMap<>();
        tags = new HashMap<>();
        cityType = CityType.Generic;
        size = 0;
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
        return gameState.getObject(location, Planet.class).getUniversePath();
    }

    public Person getGovernor() {
        return gameState.getObject(governor, Person.class);
    }

    public void setGovernor(Person governor) {
        governor.setRole("Governing " + name);
        this.governor = governor.getId();
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
        return new ArrayList();
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void incrementSize() {
        size++;
    }

    public void addArea(Integer a) {
        areas.add(a);
    }

    public CityType getCityType() {
        return cityType;
    }

    public void setCityType(CityType cityType) {
        this.cityType = cityType;
    }

    public void clearLedger(int delta) {
        ledgerClearDelta = delta;
        resourceLedger.clear();
    }

    public int getLedgerClearDelta() {
        return ledgerClearDelta;
    }
}
