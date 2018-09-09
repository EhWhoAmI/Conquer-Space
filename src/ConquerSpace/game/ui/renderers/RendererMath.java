package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.GalacticLocation;
import java.awt.Point;

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
     * <br><br>
     * What polar coordinates are:
     * <a href="https://en.wikipedia.org/wiki/Polar_coordinate_system">Polar coordinate
     * <br><br>
     * Cartesian coordinates: Just your usual x and y graph.
     * system on wikipedia</a>
     * <br><br>
     * How it does it:
     * <br>
     * Split circle into 4 equal length quadrants or sectors, each with 90
     * degrees arc length. Something like this
     * <br>
     * <img src="./doc-files/polar_to_cartesian_quardants.png" alt="Polar to cartesian coordinate image">
     * <br>
     *
     * Then, after getting a sector, we have the angle, modulus the value by 90,
     * get the quadrant it is in, then we have the a right triangle, like below.
     *
     * <br>
     * <img src="./doc-files/polar_to_cartesian_quardants_triangles.png" alt="Other stuff">
     * <br>
     *
     * So, as we can see, the red line is the hypotenuse, the blue line is the
     * adjacent, and the orange line is the opposite. the green line is the
     * angle or θ. We want to get the position the blue and red line meet, or at the
     * yellow spot.
     * <br><br>
     * Polar coordinates are based on distance and angle (θ) from a point. So, the
     * hypotenuse is the distance from a point, and θ is, well, the
     * angle, so we just modulus(%) θ by 90, so that we can get a right triangle.
     * <br><br>
     * Then, with some basic trig, we can get the length of the opposite and the
     * adjacent with sine and cosine.
     * <br><br>
     * The length of the opposite is: sine (θ) * distance
     * <br>
     * The length of the adjacent is similar: cosine (θ) * distance
     * <br><br>
     * But then, there is a slight problem. We need to change it depending on
     * the quadrant, because the opposites and adjacents represent different
     * axis. So, we follow this diagram.
     * <br>
     * <img src="./doc-files/polar_to_cartesian_addition.png" alt="">
     * <br>
     * We do this because it co-relates to the x and y coordinates. For the
     * quadrants we divide the angle (θ), by 90, to get the quardant number. In 
     * the code, subtract it by 1 to see the quardant.
     *
     * @see GalacticLocation
     * @param g galatic location/polar coordinate.
     * @param center center of the plot of polar coordinate.
     * @param unitSize the size of the individual unit(as in the distance of
     * <code>GalaticLocation</code>.
     * @return Point of the converted polar coordinate
     */
    public static Point polarCoordToCartesianCoord(GalacticLocation g, Point center, int unitSize) {
        //Do math to calculate the position of the sector. 
        //Distance is to the center of the sector to center of universe.
        //So, distance is hypotenuse, we have the angle, and we need the opposite and adjectent.
        double ang = (double) g.getDegrees();
        int rot = (int) Math.floor(ang / 90);
        ang = ang % 90;

        //Then do a sine to calculate the length of the opposite. 
        int xpos;
        int ypos;

        //Math.sin and Math.cos is in radians.
        double opp = Math.sin(Math.toRadians(ang)) * g.getDistance();
        double adj = Math.cos(Math.toRadians(ang)) * g.getDistance();

        //Multipy units. May have to change this for accuracy.
        opp *= unitSize;
        adj *= unitSize;

        //This basically splits an imaginary circle into 4 quardants, and draws right angled triangles
        //Then it calculates all that based on the theory above.
        switch (rot) {
            case 0:
                //Quardant 1 on javadoc
                xpos = (int) Math.floor(center.getX() + adj);
                ypos = (int) Math.floor(center.getY() - opp);
                break;
            case 1:
                //Quardant 2 on javadoc
                xpos = (int) Math.floor(center.getX() - opp);
                ypos = (int) Math.floor(center.getY() - adj);
                break;
            case 2:
                //Quardant 3 on javadoc
                xpos = (int) Math.floor(center.getX() - adj);
                ypos = (int) Math.floor(center.getY() + opp);
                break;
            case 3:
                //Quardant 4 on javadoc
                xpos = (int) Math.floor(center.getX() + opp);
                ypos = (int) Math.floor(center.getY() + adj);
                break;
            default:
                //IDK something went wrong...
                xpos = 0;
                ypos = 0;
        }

        return (new Point(xpos, ypos));
    }

    /**
     * Hide constructor.
     */
    private RendererMath() {
    }
}
