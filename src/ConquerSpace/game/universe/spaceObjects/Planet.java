package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.GameObject;
import ConquerSpace.game.universe.GalaticLocation;
/**
 * Planet class.
 * @author Zyun
 */
public class Planet extends GameObject{
    private int planetType;
    private GalaticLocation orbitalDistance;
    private int planetSize;
    private int id;
    
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
}
