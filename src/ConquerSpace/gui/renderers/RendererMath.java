package ConquerSpace.gui.renderers;

import ConquerSpace.game.universe.GalacticLocation;

/**
 * Does all the math for rendering.
 *
 * @author Zyun
 */
public class RendererMath {

    /**
     * Converts a GalaticLocation(AKA polar coordinate) to a point on a
     * Cartesian coordinate pane(or a swing panel pane), so that you can plot it
     * on a swing jpanel.
     *
     * @see GalacticLocation
     * @param g galatic location/polar coordinate.
     * @param center center of the plot of polar coordinate.
     * @param unitSize the size of the individual unit(as in the distance of
     * <code>GalaticLocation</code>.
     * @return Point of the converted polar coordinate
     */
    public static Point polarCoordToCartesianCoord(long distance, double degrees, Point center, int unitSize) {
        long xpos;
        long ypos;

        double opp = (Math.sin(Math.toRadians(degrees)) * distance);
        double adj = (Math.cos(Math.toRadians(degrees)) * distance);

        //Multipy units. May have to change this for accuracy.
        opp *= unitSize;
        adj *= unitSize;

        xpos = (long) (center.x + adj);
        ypos = (long) (center.y - opp);
        
        return (new RendererMath.Point(xpos, ypos));
    }

    public static class Point {

        public long x;
        public long y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public long getX() {
            return x;
        }

        public long getY() {
            return y;
        }
    }

    /**
     * Hide constructor.
     */
    private RendererMath() {
    }
}
