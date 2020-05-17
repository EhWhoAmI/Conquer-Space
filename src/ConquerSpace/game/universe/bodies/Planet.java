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
package ConquerSpace.game.universe.bodies;

import ConquerSpace.game.districts.District;
import ConquerSpace.game.districts.City;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.civilization.stats.Economy;
import ConquerSpace.game.population.Population;
import ConquerSpace.game.universe.resources.Stratum;
import ConquerSpace.game.ships.Orbitable;
import ConquerSpace.game.ships.satellites.Satellite;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Planet class.
 *
 * @author EhWhoAmI
 */
public class Planet extends Body {

    private int planetType;

    //Radius in hundreds of kilometers
    private int planetSize;

    public ArrayList<Stratum> strata;

    private int ownerID = ControlTypes.NONE_CONTROLLED;
    //Empty as default -- undiscovered
    private String name = "";
    //public PlanetSector[] planetSectors;

    private int parentStarSystem;

    public Economy economy;

    private ArrayList<Orbitable> satellites;

    public HashMap<GeographicPoint, District> buildings;

    public ArrayList<Integer> scanned;

    private int terrainSeed;

    private int terrainColoringIndex;

    private boolean habitated = false;

    private float degreesPerTurn = 0.0f;

    public ArrayList<City> cities;

    private Person governor;

    public ArrayList<Workable> jobProviders;

    /**
     * If this is empty, the planet does not have life.
     */
    public ArrayList<LocalLife> localLife;
    
    public Population population;

    /**
     * Creates planet
     *
     * @param planetType Type of planet. See <code>PlanetTypes</code>
     * @param planetSize size of planet
     * @param id planet id
     * @param parentStarSystem parent star system
     */
    public Planet(int planetType, int planetSize, int id, int parentStarSystem) {
        this.planetType = planetType;
        this.planetSize = planetSize;
        super.ID = id;
        this.parentStarSystem = parentStarSystem;
        //Surface area equals 4 * diameter
        //Surface area is in sectors
        //1 sector = 10 'units'
        //planetSectors = new PlanetSector[surfaceArea];
        economy = new Economy();
        satellites = new ArrayList<>();
        strata = new ArrayList<>();
        buildings = new HashMap<>();

        scanned = new ArrayList<>();
        cities = new ArrayList<>();

        jobProviders = new ArrayList<>();

        //planetJobs = new ArrayList<>();
        localLife = new ArrayList<>();
        
        population = new Population();
    }

    /**
     * Returns a human-readable string
     *
     * @return a human-readable string.
     */
    public String toReadableString() {
        StringBuilder builder = new StringBuilder();
        //Parse planet type.
        builder.append("Planet " + ID + ": (Type=");
        switch (planetType) {
            case PlanetTypes.ROCK:
                builder.append("rock");
                break;
            case PlanetTypes.GAS:
                builder.append("gas");
        }
        builder.append(", Orbital Distance=");
        builder.append(orbit.toPolarCoordinate());
        builder.append(", Planet size: ");
        builder.append(planetSize);
        builder.append("Rectangular Position: ");
        builder.append(point);
        builder.append(":\n");

        builder.append(")\n");
        return (builder.toString());
    }

    public int getPlanetSize() {
        return planetSize;
    }

    public int getPlanetType() {
        return planetType;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public long getPopulation() {
        return 0;//population.population.get(population.population.size() - 1);
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public void computeEconomy() {

    }

    public int getParentStarSystem() {
        return parentStarSystem;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    void setParentStarSystem(int parentStarSystem) {
        this.parentStarSystem = parentStarSystem;
    }

    public UniversePath getUniversePath() {
        return (new UniversePath(parentStarSystem, ID));
    }

    public int getSatelliteCount() {
        return satellites.size();
    }

    public Orbitable getSatellite(int i) {
        return satellites.get(i);
    }

    public void addSatellite(Satellite s) {
        satellites.add(s);
    }

    public ArrayList<Orbitable> getSatellites() {
        return satellites;
    }

    public void putShipInOrbit(Orbitable orb) {
        satellites.add(orb);
    }

    @Override
    public String toString() {
        if (name.isEmpty()) {
            return (ID + "");
        }
        return name;
    }

    public int getTerrainSeed() {
        return terrainSeed;
    }

    public void setTerrainSeed(int terrainSeed) {
        this.terrainSeed = terrainSeed;
    }

    public boolean isHabitated() {
        return habitated;
    }

    public void setHabitated(boolean habitated) {
        this.habitated = habitated;
    }

    public void setTerrainColoringIndex(int terrainColoringIndex) {
        this.terrainColoringIndex = terrainColoringIndex;
    }

    public int getTerrainColoringIndex() {
        return terrainColoringIndex;
    }

    public void setDegreesPerTurn(float degreesPerTurn) {
        this.degreesPerTurn = degreesPerTurn;
    }

    public float getDegreesPerTurn() {
        return degreesPerTurn;
    }

    public Person getGovernor() {
        return governor;
    }

    public void setGovernor(Person governor) {
        this.governor = governor;
    }

    /**
     * Checks if it's near a city, and adds it to the city. If not, creates one.
     *
     * @param pt position
     * @param b building to add
     * @return The city it is added to.
     */
    public City addBuildingToPlanet(GeographicPoint pt, District b) {
        City city = null;

        if (buildings.containsKey(pt.getNorth())) {
            city = buildings.get(pt.getNorth()).getCity();
            city.addDistrict(b);
        } else if (buildings.containsKey(pt.getSouth())) {
            city = buildings.get(pt.getSouth()).getCity();
            city.addDistrict(b);
        } else if (buildings.containsKey(pt.getEast())) {
            city = buildings.get(pt.getEast()).getCity();
            city.addDistrict(b);
        } else if (buildings.containsKey(pt.getWest())) {
            city = buildings.get(pt.getWest()).getCity();
            city.addDistrict(b);
        }

        //Create city
        if (city == null) {
            City createdCity = new City(this.getUniversePath());

            createdCity.setName(City.CITY_DEFAULT);
            city = createdCity;
            city.addDistrict(b);
            b.setCity(city);
            cities.add(city);
        }

        placeBuilding(pt, b);
        return city;
    }

    public void placeBuilding(GeographicPoint pt, District b) {
        //Check if exists already
        if (buildings.containsKey(pt)) {
            District existed = buildings.get(pt);
            if (existed != null) {
                existed.getCity().buildings.remove(existed);
            }
        }

        buildings.put(pt, b);
        if (b instanceof Workable) {
            //jobProviders.add(b);
        }
    }

    public int getPlanetHeight() {
        return (2 * planetSize);
    }

    public int getPlanetWidth() {
        int value = (int) (2 * planetSize * Math.PI);
        return value;
    }
}
