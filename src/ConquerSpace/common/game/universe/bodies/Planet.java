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
package ConquerSpace.common.game.universe.bodies;

import ConquerSpace.common.game.organizations.civilization.stats.Economy;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.life.LocalLife;
import ConquerSpace.common.game.organizations.Administrable;
import ConquerSpace.common.game.people.Person;
import ConquerSpace.common.game.population.jobs.Workable;
import ConquerSpace.common.game.ships.Orbitable;
import ConquerSpace.common.game.ships.satellites.Satellite;
import ConquerSpace.common.game.universe.GeographicPoint;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.resources.Stratum;
import ConquerSpace.common.game.Serialize;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Planet class.
 *
 * @author EhWhoAmI
 */
public class Planet extends Body implements Administrable {

    @Serialize(key = "type")
    private int planetType;

    //Radius in hundreds of kilometers, which means that one tile is 100x100 km
    @Serialize(key = "size")
    private int planetSize;

    @Serialize(key = "strata")
    public ArrayList<Stratum> strata;

    @Serialize(key = "owner")
    private int ownerID = ControlTypes.NONE_CONTROLLED;

    //Empty as default -- undiscovered
    @Serialize(key = "name")
    private String name = "";
    //public PlanetSector[] planetSectors;

    @Serialize(key = "parent")
    private int parentStarSystem;

    public Economy economy;

    @Serialize(key = "satellites")
    private ArrayList<Orbitable> satellites;

    @Serialize(key = "city-positions")
    public HashMap<GeographicPoint, Integer> cityDistributions;

    //Civs that have scanned this
    @Serialize(key = "scanned")
    private ArrayList<Integer> scanned;

    @Serialize(key = "terrain-seed")
    private int terrainSeed;

    @Serialize(key = "coloringIndex")
    private int terrainColoringIndex;

    @Serialize(key = "lived")
    private boolean habitated = false;

    @Serialize(key = "delta")
    private float degreesPerTurn = 0.0f;

    @Serialize(key = "cities")
    public ArrayList<City> cities;

    private Person governor;

    @Serialize(key = "providers")
    public ArrayList<Workable> jobProviders;

    /**
     * If this is empty, the planet does not have life.
     */
    public ArrayList<LocalLife> localLife;

    public long population = 0;
    public float populationIncrease = 0;

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
        this.parentStarSystem = parentStarSystem;
        //Surface area equals 4 * diameter
        //Surface area is in sectors
        //1 sector = 10 'units'
        //planetSectors = new PlanetSector[surfaceArea];
        economy = new Economy();
        satellites = new ArrayList<>();
        strata = new ArrayList<>();
        cityDistributions = new HashMap<>();

        scanned = new ArrayList<>();
        cities = new ArrayList<>();

        jobProviders = new ArrayList<>();

        //planetJobs = new ArrayList<>();
        localLife = new ArrayList<>();
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
        return (new UniversePath(parentStarSystem, id));
    }

    public int getSatelliteCount() {
        return satellites.size();
    }

    public Orbitable getSatellite(int i) {
        return satellites.get(i);
    }

    public void addSatellite(Satellite s) {
        s.setOrbiting(getUniversePath());
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
            return (id + "");
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

    public void addCityDefinition(GeographicPoint pt, City b) {
        //Check if exists already
        if (cityDistributions.containsKey(pt)) {
            //City exists in that place already
            cityDistributions.remove(pt);
        } else {
            b.incrementSize();
        }

        if (!cityDistributions.containsValue(b)) {
            cities.add(b);
        }

        cityDistributions.put(pt, b.getId());
    }

    /*
    How the height of planet works
     */
    //Height of planet on map
    public int getPlanetHeight() {
        return (2 * planetSize);
    }

    //Width of map
    public int getPlanetWidth() {
        int value = (int) (2 * planetSize * Math.PI);
        return value;
    }

    public City getCity(GeographicPoint pt) {
        if (cityDistributions.containsKey(pt)) {
            int id = cityDistributions.get(pt);

            for (int i = 0; i < cities.size(); i++) {
                if(cities.get(i).getId() == id) {
                    return cities.get(i);
                }
            }
        }
        return null;
    }
    
    public boolean hasScanned(int id) {
        return scanned.contains(id);
    }
    
    public void scan(int id) {
        scanned.add(id);
    }
}
