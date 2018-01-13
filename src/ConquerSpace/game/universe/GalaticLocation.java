package ConquerSpace.game.universe;

/**
 * Galatic location.
 * @author Zyun
 */
public class GalaticLocation {
    private float degrees;
    private int distance;

    /**
     * Constructor.
     * @param degrees
     * @param distance
     */
    public GalaticLocation(float degrees, int distance) {
        this.degrees = degrees;
        this.distance = distance;
    }

    //To string
    @Override
    public String toString() {
        return("(Degrees: " + degrees + " Distance: " + distance + ")");
    }
}
