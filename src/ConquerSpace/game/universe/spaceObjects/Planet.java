package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.universe.GalaticLocation;
import ConquerSpace.game.universe.civilizations.stats.Economy;
import ConquerSpace.game.universe.civilizations.stats.Population;
import ConquerSpace.game.universe.spaceObjects.pSectors.BuildingBuilding;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;

/**
 * Planet class.
 *
 * @author Zyun
 */
public class Planet extends SpaceObject {

    private int planetType;
    private GalaticLocation orbitalDistance;
    private int planetSize;
    private int id;

    private int ownerID;
    private int surfaceArea;
    //Empty as default -- undiscovered
    private String name = "";
    public PlanetSector[] planetSectors;

    private int parentStarSystem;
    private int parentSector;

    public Population population;
    public Economy economy;

    /**
     * Creates planet
     *
     * @param planetType Type of planet. See <code>PlanetTypes</code>
     * @param orbitalDistance orbit of planet
     * @param planetSize size of planet
     * @param id planet id
     * @param parentStarSystem parent star system
     * @param parentSector parent sector
     */
    public Planet(int planetType, int orbitalDistance, int planetSize, int id, int parentStarSystem, int parentSector) {
        this.planetType = planetType;
        this.orbitalDistance = new GalaticLocation(0, orbitalDistance);
        this.planetSize = planetSize;
        this.id = id;
        this.parentSector = parentSector;
        this.parentStarSystem = parentStarSystem;

        //Surface area equals 4 * diameter
        //Surface area is in sectors
        //1 sector = 10 'units'
        surfaceArea = (int) Math.floor((planetSize * planetSize * Math.PI * 4) / 10);
        if (surfaceArea == 0) {
            surfaceArea = 1;
        }
        planetSectors = new PlanetSector[surfaceArea];
        population = new Population();
        economy = new Economy();
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
        builder.append(", Orbital Distance=" + orbitalDistance + ", Planet size: " + planetSize + " Planet Sectors " + planetSectors.length + ":\n");

        for (PlanetSector s : planetSectors) {
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

    public void computePopulation(int turn) {
        //Population
        long currentPopulation = 0;
        
        //Birth rate
        float birthRate = 0;
        //Death rate
        float deathRate = 0;
        
        //Population:
        //Last and current
        long lastPop;
        if(population.population.size() == 0)
            lastPop = 0;
        else
            lastPop = getPopulation();
        int index = 0;
        for (PlanetSector sector : planetSectors) {
            if (sector instanceof PopulationStorage) {
                //Parse
                Population pop = ((PopulationStorage) sector).pop;
                currentPopulation += pop.population.get(turn);
                birthRate += pop.getLastYearsbirthsPer1K(turn);
                deathRate += pop.getLastYearsMortalityRate(turn);
                index ++;
            }
        }
        population.population.add(currentPopulation);
        
        //Calculate averages
        population.birthsPer1k.add(birthRate/index);
        population.mortalityRate.add(deathRate/index);
        if(lastPop != 0)
            population.populationGrowth.add((float)(((currentPopulation - lastPop) / lastPop) * 100));
        else
            population.populationGrowth.add(0f);
        
    }

    public void computeEconomy(int turn) {

    }

    @Override
    public void processTurn(int turn) {
        int index = 0;
        for (PlanetSector planetSector : planetSectors) {
            planetSector.processTurn(turn);
            
            //Parse building buildings
            if(planetSector instanceof BuildingBuilding) {
                BuildingBuilding building = (BuildingBuilding)planetSector;
                if(building.getTurns() == 0) {
                    //Replace
                    planetSectors[index] = building.getSector();
                }
            }
            index ++;
        }
        computePopulation(turn);
        computeEconomy(turn);
    }

    public int getParentSector() {
        return parentSector;
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

}
