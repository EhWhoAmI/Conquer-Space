package ConquerSpace.game.universe;

/**
 * Galactic location. In a nutshell, this is a polar coordinate.
 * @author Zyun
 */
public class GalacticLocation {
    private float degrees;
    private int distance;

    /**
     * Constructor of the polar coordinate.
     * @param degrees degrees from north in a anticlockwise direction.
     * @param distance distance from the center.
     */
    public GalacticLocation(float degrees, int distance) {
        this.degrees = (degrees%360);
        this.distance = distance;
    }

    /**
     * To string. Example output: (Degrees: &lt;degrees&gt; Distance &lt;distance&gt;)
     * @return String value of this class
     */
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
    public int getDistance() {
        return (distance);
    }
    
    /**
     * Set the degrees
     * @param degrees degrees
     */
    public void setDegrees(float degrees) {
        this.degrees = (degrees%360);
    }
    
    /**
     * Set the distance
     * @param distance Distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }
}
