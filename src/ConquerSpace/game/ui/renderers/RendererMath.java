package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.GalaticLocation;
import java.awt.Point;

/**
 * Does all the math for rendering.
 *
 * @author Zyun
 */
public class RendererMath {
    
    /**
     * Converts a GalaticLocation(AKA polar coordinate) to a point on a Cartesian coordinate pane(or a swing panel pane), so that
     * you can plot it there.
     * @see GalaticLocation
     * @param g galatic location/polar coordinate
     * @param center  center of the plot of polar coordinate
     * @param unitSize the size of the individual unit(as in the distance of <code>GalaticLocation</code>.
     * @return
     */
    public static Point polarCoordToCartesianCoord(GalaticLocation g, Point center, int unitSize) {
        //Do math to calculate the position of the sector. 
        //Distance is to the center of the sector to center of universe.
        //So, distance is hypotenuse, we have the angle, and we need the opposite and adjectent.
        double ang = (double) g.getDegrees();
        int rot = (int) Math.floor(ang/90);
        ang = ang % 90;
        //Then do a sine to calculate the length of the opposite. 
        int xpos;
        int ypos;
        //Math.sin and Math.cos is in radians.
        double opp = Math.sin(Math.toRadians(ang)) * g.getDistance();
        double adj = Math.cos(Math.toRadians(ang)) * g.getDistance();
        opp *= unitSize;
        adj *= unitSize;
        
        //This basically splits an imaginary circle into 4 quardants, and draws right angled triangles
        //Then it calculates all that based on the theory above.
        switch (rot) {
            case 0:
                xpos = (int) Math.floor(center.getX() - opp);
                ypos = (int) Math.floor(center.getY() + adj);
                break;
            case 1:
                xpos = (int) Math.floor(center.getX() - adj);
                ypos = (int) Math.floor(center.getY() - opp);
                break;
            case 2:
                xpos = (int) Math.floor(center.getX() + opp);
                ypos = (int) Math.floor(center.getY() - adj);
                break;
            case 3:
                xpos = (int) Math.floor(center.getX() + adj);
                ypos = (int) Math.floor(center.getY() + opp);
                break;
            default:
                xpos = 0;
                ypos = 0;
        }
        return (new Point(xpos, ypos));
    }
}
