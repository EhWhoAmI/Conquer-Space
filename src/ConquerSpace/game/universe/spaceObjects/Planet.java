package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.City;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.population.Job;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.stats.Economy;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.ships.Orbitable;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Planet class.
 *
 * @author Zyun
 */
public class Planet extends SpaceObject {

    private int planetType;
    private long orbitalDistance;
    private int degrees;
    private int planetSize;
    
    private long xpos;
    private long ypos;
    
    public ArrayList<ResourceVein> resourceVeins;
    
    int id;

    private int ownerID = ControlTypes.NONE_CONTROLLED;
    //Empty as default -- undiscovered
    private String name = "";
    //public PlanetSector[] planetSectors;

    private int parentStarSystem;

    public Economy economy;
    
    private ArrayList<Orbitable> satellites;
    
    public HashMap<GeographicPoint, Building> buildings;
    
    public ArrayList<Integer> scanned;
    
    private int terrainSeed;
    
    private int terrainColoringIndex;
    
    private boolean habitated = false;
    
    private float degreesPerTurn = 0.0f;
    
    public ArrayList<City> cities;
    
    public ArrayList<Job> planetJobs;
    
    public ArrayList<PopulationUnit> population;
    
    /**
     * If this is empty, the planet does not have life.
    */
    public ArrayList<LocalLife> localLife;
    /**
     * Creates planet
     *
     * @param planetType Type of planet. See <code>PlanetTypes</code>
     * @param orbitalDistance orbit of planet in km
     * @param planetSize size of planet
     * @param id planet id
     * @param parentStarSystem parent star system
     */    
    public Planet(int planetType, long orbitalDistance, int planetSize, int id, int parentStarSystem) {
        this.planetType = planetType;
        this.orbitalDistance = orbitalDistance;
        this.planetSize = planetSize;
        this.id = id;
        this.parentStarSystem = parentStarSystem;
        this.degrees = 0;
        //Surface area equals 4 * diameter
        //Surface area is in sectors
        //1 sector = 10 'units'
        //planetSectors = new PlanetSector[surfaceArea];
        economy = new Economy();
        satellites = new ArrayList<>();
        resourceVeins = new ArrayList<>();
        buildings = new HashMap<>();
        
        scanned = new ArrayList<>();
        cities = new ArrayList<>();
        
        planetJobs = new ArrayList<>();
        population = new ArrayList<>();
        
        localLife = new ArrayList<>();
    }

    /**
     * Returns a human-readable string
     *
     * @return a human-readable string.
     */
    public String toReadableString() {
        StringBuilder builder = new StringBuilder();
        //Parse planet type.
        builder.append("Planet " + id + ": (Type=");
        switch (planetType) {
            case PlanetTypes.ROCK:
                builder.append("rock");
                break;
            case PlanetTypes.GAS:
                builder.append("gas");
        }
        builder.append(", Orbital Distance=" + orbitalDistance + ", Planet size: " + planetSize + 
                "Rectangular Position: " + xpos + ", " + ypos+ ":\n");


        builder.append(")\n");
        return (builder.toString());
    }

    public int getId() {
        return id;
    }

    public long getOrbitalDistance() {
        return orbitalDistance;
    }

    public int getPlanetSize() {
        return planetSize;
    }

    public int getPlanetType() {
        return planetType;
    }

    /**
     * Change the degrees by <code>degs</code> degrees.
     * @param degs degrees
     */
    public void modDegrees(float degs) {
        degrees+=degs;
        degrees %= 360;
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
    
    public float getPlanetDegrees() {
        return (degrees);
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
        satellites.add(s);
    }
    
    public ArrayList<Orbitable> getSatellites() {
        return satellites;
    }
    
    public void putShipInOrbit(Orbitable orb) {
        satellites.add(orb);
    }
    
    public long getX() {
        return xpos;
    }
    
    public long getY() {
        return ypos;
    }
    
    public void setX(long x) {
        xpos = x;
    }
    
    public void setY(long y) {
        ypos = y;
    }

    @Override
    public String toString() {
        if(name.isEmpty()) {
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
}
