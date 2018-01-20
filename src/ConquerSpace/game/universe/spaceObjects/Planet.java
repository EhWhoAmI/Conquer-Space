package ConquerSpace.game.universe.spaceObjects;

/**
 * Planet class
 * @author Zyun
 */
public class Planet {
    private int planetType;
    private int orbitalDistance;
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
        this.orbitalDistance = orbitalDistance;
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
}
