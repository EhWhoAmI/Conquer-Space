package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.GameObject;
import ConquerSpace.game.universe.GalaticLocation;
import ConquerSpace.game.universe.spaceObjects.planet.PlanetSector;
/**
 * Planet class.
 * @author Zyun
 */
public class Planet extends GameObject{
    private int planetType;
    private GalaticLocation orbitalDistance;
    private int planetSize;
    private int id;
    
    private int ownerID;
    private int population;
    private int surfaceArea;
    public PlanetSector[] planetSectors;
    
    /**
     * Creates planet
     * @param planetType
     * @param orbitalDistance
     * @param planetSize
     * @param id
     */
    public Planet(int planetType, int orbitalDistance, int planetSize, int id) {
        this.planetType = planetType;
        this.orbitalDistance = new GalaticLocation(0, orbitalDistance);
        this.planetSize = planetSize;
        this.id = id;
        //Surface area equals 4 * diameter
        //Surface area is in sectors
        //1 sector = 100 'units'
        surfaceArea = (int) Math.floor((planetSize * planetSize * Math.PI * 4) / 100);
        planetSectors = new PlanetSector[surfaceArea];
        
    }

    /**
     * Returns a human-readable string
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
        builder.append(", Orbital Distance=" + orbitalDistance + ", Planet size: " + planetSize + ")\n");
        return (builder.toString());
    }

    public int getId() {
        return id;
    }

    public int getOrbitalDistance() {
        return (int) orbitalDistance.getDistance();
    }

    public int getPlanetSize() {
        return planetSize;
    }

    public int getPlanetType() {
        return planetType;
    }
    
    public void modDegrees(float degs) {
        orbitalDistance.setDegrees(orbitalDistance.getDegrees() + degs);
    }

    public int getOwnerID() {
        return ownerID;
    }

    public int getPopulation() {
        return population;
    }

    public int getSurfaceArea() {
        return surfaceArea;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
