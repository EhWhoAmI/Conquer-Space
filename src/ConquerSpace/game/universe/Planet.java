package ConquerSpace.game.universe;

/**
 *
 * @author Zyun
 */
public class Planet {
    private PlanetTypes planetType;
    private int orbitalDistance;
    private int planetSize;
    private int id;
    
    public Planet(PlanetTypes planetType, int orbitalDistance, int planetSize, int id) {
        this.planetType = planetType;
        this.orbitalDistance = orbitalDistance;
        this.planetSize = planetSize;
        this.id = id;
    }
}
