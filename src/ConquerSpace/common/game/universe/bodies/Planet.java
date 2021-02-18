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
import ConquerSpace.common.game.ships.Orbitable;
import ConquerSpace.common.game.universe.GeographicPoint;
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
    private ArrayList<ObjectReference> strata;

    @Serialize("owner")
    private ObjectReference ownerReference = ObjectReference.INVALID_REFERENCE;

    //Empty as default -- undiscovered
    @Serialize("name")
    private String name = "";
    //public PlanetSector[] planetSectors;

    @Serialize("satellites")
    private ArrayList<ObjectReference> satellites;

    @Serialize("city-positions")
    private HashMap<GeographicPoint, ObjectReference> cityDistributions;

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
    private ArrayList<ObjectReference> cities;

    private ObjectReference governor;

    private ObjectReference planetaryMarket;

    /**
     * If this is empty, the planet does not have life.
     */
    private ArrayList<LocalLife> localLife;

    private long population = 0;
    private float populationIncrease = 0;

    private double infrastructureIndex;

    /**
     * Creates planet
     *
     * @param gameState
     * @param planetType Type of planet. See <code>PlanetTypes</code>
     * @param planetSize size of planet
     */
    public Planet(GameState gameState, int planetType, int planetSize) {
        super(gameState);
        this.planetType = planetType;
        this.planetSize = planetSize;
        //Surface area equals 4 * diameter
        //Surface area is in sectors
        //1 sector = 10 'units'
        satellites = new ArrayList<>();
        strata = new ArrayList<>();
        cityDistributions = new HashMap<>();

        scanned = new ArrayList<>();
        cities = new ArrayList<>();

        //planetJobs = new ArrayList<>();
        localLife = new ArrayList<>();

        infrastructureIndex = 1d;

        setDiameter(planetSize * 2);
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

    public int getSatelliteCount() {
        return satellites.size();
    }

    public Orbitable getSatellite(int i) {
        return gameState.getObject(satellites.get(i), Orbitable.class);
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
        if (getCityDistributions().containsKey(pt)) {
            //City exists in that place already
            getCityDistributions().remove(pt);
        } else {
            b.incrementSize();
        }

        if (!cityDistributions.containsValue(b.getReference())) {
            getCities().add(b.getReference());
            //Add the initial point
            b.setInitialPoint(pt);
        }

        getCityDistributions().put(pt, b.getReference());
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
        if (getCityDistributions().containsKey(pt)) {
            ObjectReference id = getCityDistributions().get(pt);

            for (int i = 0; i < getCities().size(); i++) {
                if (getCities().get(i).equals(id)) {
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

    public ArrayList<ObjectReference> getCities() {
        return cities;
    }

    public ObjectReference getPlanetaryMarket() {
        return planetaryMarket;
    }

    public void setPlanetaryMarket(ObjectReference planetaryMarket) {
        this.planetaryMarket = planetaryMarket;
    }

    /**
     * @return the strata
     */
    public ArrayList<ObjectReference> getStrata() {
        return strata;
    }

    /**
     * @return the cityDistributions
     */
    public HashMap<GeographicPoint, ObjectReference> getCityDistributions() {
        return cityDistributions;
    }

    /**
     * @return the localLife
     */
    public ArrayList<LocalLife> getLocalLife() {
        return localLife;
    }

    /**
     * @param population the population to set
     */
    public void setPopulation(long population) {
        this.population = population;
    }

    /**
     * @return the populationIncrease
     */
    public float getPopulationIncrease() {
        return populationIncrease;
    }

    /**
     * @param populationIncrease the populationIncrease to set
     */
    public void setPopulationIncrease(float populationIncrease) {
        this.populationIncrease = populationIncrease;
    }

    public double getInfrastructureIndex() {
        return infrastructureIndex;
    }

    public void setInfrastructureIndex(double infrastructureIndex) {
        this.infrastructureIndex = infrastructureIndex;
    }
}
