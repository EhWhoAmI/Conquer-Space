package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.GalaticLocation;
import ConquerSpace.game.universe.civilizations.stats.Economy;
import ConquerSpace.game.universe.civilizations.stats.Population;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;
/**
 * Planet class.
 * @author Zyun
 */
public class Planet extends SpaceObject{
    private int planetType;
    private GalaticLocation orbitalDistance;
    private int planetSize;
    private int id;
    
    private int ownerID;
    private int surfaceArea;
    //Empty as default -- undiscovered
    private String name = "";
    public PlanetSector[] planetSectors;
    private int parent;
    public Population population;
    public Economy economy;
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
        population = new Population();
        economy = new Economy();
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
        builder.append(", Orbital Distance=" + orbitalDistance + ", Planet size: " + planetSize + " Planet Sectors " + planetSectors.length + ":\n");
                
        for(PlanetSector s : planetSectors) {
            builder.append(s.toReadableString());
            builder.append(", \n");
        }
        builder.append(")\n");
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

    public long getPopulation() {
        return population.population.get(population.population.size() - 1);
    }

    public int getSurfaceArea() {
        return surfaceArea;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public void setPlanetSector(int index, PlanetSector sector) {
        planetSectors[index] = sector;
    }
    
    public int getPlanetSectorCount() {
        return planetSectors.length;
    }
    
    public void computePopulation(){
        long currentPopulation = 0;
        
        for(PlanetSector sector : planetSectors) {
            if(sector instanceof PopulationStorage) {
                //Parse
                currentPopulation +=((PopulationStorage)sector).pop.population.get(population.population.size() - 1);
            }
        }
        population.population.add(currentPopulation);
    }
    
    public void computeEconomy() {
        
    }
    
    @Override
    public void processTurn(int turn) {
        computePopulation();
        computeEconomy();
    }
}
