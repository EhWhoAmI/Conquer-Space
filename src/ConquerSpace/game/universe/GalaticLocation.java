package ConquerSpace.game.universe;

/**
 * Galatic location. In a nutshell, this is a polar coordinate.
 * @author Zyun
 */
public class GalaticLocation {
    private float degrees;
    private int distance;

    /**
     * Constructor.
     * @param degrees degrees from north in a clockwise direction.
     * @param distance distance from the center.
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
    
    /**
     * Get the degrees.
     * @return Degrees.
     */
    public float getDegrees() {
        return (degrees);
    }
    
    /**
     * Get the distance.
     * @return Distance.
     */
    public float getDistance() {
        return (distance);
    }
}
