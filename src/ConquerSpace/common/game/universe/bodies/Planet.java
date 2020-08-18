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

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.characters.Person;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.life.LocalLife;
import ConquerSpace.common.game.organizations.Administrable;
import ConquerSpace.common.game.organizations.civilization.stats.Economy;
import ConquerSpace.common.game.ships.Orbitable;
import ConquerSpace.common.game.ships.satellites.Satellite;
import ConquerSpace.common.game.universe.GeographicPoint;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.save.Serialize;
import ConquerSpace.common.save.SerializeClassName;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Planet class.
 *
 * @author EhWhoAmI
 */
@SerializeClassName("planet")
public class Planet extends StarSystemBody implements Administrable {

    @Serialize("type")
    private int planetType;

    //Radius in hundreds of kilometers, which means that one tile is 100x100 km
    @Serialize("size")
    private int planetSize;

    @Serialize("strata")
    public ArrayList<ObjectReference> strata;

    @Serialize("owner")
    private ObjectReference ownerReference = ObjectReference.INVALID_REFERENCE;

    //Empty as default -- undiscovered
    @Serialize("name")
    private String name = "";
    //public PlanetSector[] planetSectors;

    public Economy economy;

    @Serialize("satellites")
    private ArrayList<ObjectReference> satellites;

    @Serialize("city-positions")
    public HashMap<GeographicPoint, ObjectReference> cityDistributions;

    //Civs that have scanned this
    @Serialize("scanned")
    private ArrayList<ObjectReference> scanned;

    @Serialize("terrain-seed")
    private int terrainSeed;

    @Serialize("coloringIndex")
    private int terrainColoringIndex;

    @Serialize("lived")
    private boolean habitated = false;

    @Serialize("delta")
    private float degreesPerTurn = 0.0f;

    @Serialize("cities")
    public ArrayList<ObjectReference> cities;

    private ObjectReference governor;

    /**
     * If this is empty, the planet does not have life.
     */
    public ArrayList<LocalLife> localLife;

    public long population = 0;
    public float populationIncrease = 0;

    /**
     * Creates planet
     *
     * @param gameState
     * @param planetType Type of planet. See <code>PlanetTypes</code>
     * @param planetSize size of planet
     * @param id planet id
     */
    public Planet(GameState gameState, int planetType, int planetSize, int id) {
        super(gameState);
        this.planetType = planetType;
        this.planetSize = planetSize;
        //Surface area equals 4 * diameter
        //Surface area is in sectors
        //1 sector = 10 'units'
        economy = new Economy();
        satellites = new ArrayList<>();
        strata = new ArrayList<>();
        cityDistributions = new HashMap<>();

        scanned = new ArrayList<>();
        cities = new ArrayList<>();

        //planetJobs = new ArrayList<>();
        localLife = new ArrayList<>();
    }

    public int getPlanetSize() {
        return planetSize;
    }

    public int getPlanetType() {
        return planetType;
    }

    public ObjectReference getOwnerReference() {
        return ownerReference;
    }

    public long getPopulation() {
        return 0;//population.population.get(population.population.size() - 1);
    }

    public void setOwnerReference(ObjectReference ownerReference) {
        this.ownerReference = ownerReference;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UniversePath getUniversePath() {
        return (new UniversePath(getParentIndex(), getIndex()));
    }

    public int getSatelliteCount() {
        return satellites.size();
    }

    public Orbitable getSatellite(int i) {
        return gameState.getObject(satellites.get(i), Orbitable.class);
    }

    public void addSatellite(Satellite s) {
        s.setOrbiting(getUniversePath());
        satellites.add(s.getReference());
    }

    public ArrayList<ObjectReference> getSatellites() {
        return satellites;
    }

    public void putShipInOrbit(Orbitable orb) {
        if (orb instanceof ConquerSpaceGameObject) {
            satellites.add(((ConquerSpaceGameObject) orb).getReference());
        }
    }

    @Override
    public String toString() {
        if (name.isEmpty()) {
            return getReference().toString();
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
        return gameState.getObject(governor, Person.class);
    }

    public void setGovernor(Person governor) {
        this.governor = governor.getReference();
    }

    public void addCityDefinition(GeographicPoint pt, City b) {
        //Check if exists already
        if (cityDistributions.containsKey(pt)) {
            //City exists in that place already
            cityDistributions.remove(pt);
        } else {
            b.incrementSize();
        }

        if (!cityDistributions.containsValue(b.getReference())) {
            cities.add(b.getReference());
            //Add the initial point
            b.setInitialPoint(pt);
        }

        cityDistributions.put(pt, b.getReference());
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
            ObjectReference id = cityDistributions.get(pt);

            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).equals(id)) {
                    return gameState.getObject(cities.get(i), City.class);
                }
            }
        }
        return null;
    }

    public boolean hasScanned(ObjectReference id) {
        return scanned.contains(id);
    }

    public void scan(ObjectReference id) {
        scanned.add(id);
    }

    public ArrayList<City> getCities() {
        ArrayList<City> cityList = new ArrayList<>();
        for (ObjectReference cityId : cities) {
            City c = gameState.getObject(cityId, City.class);
            cityList.add(c);
        }
        return cityList;
    }
}
