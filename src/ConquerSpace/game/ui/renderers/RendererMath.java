package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.GalaticLocation;
import java.awt.Point;

/**
 * Does all the math for decoupling.
 *
 * @author Zyun
 */
public class RendererMath {

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
        
        switch (rot) {
            case 0:
                //Xpos is opposite.
                xpos = (int) Math.floor(center.getX() + opp);
                ypos = (int) Math.floor(center.getY() - adj);
                break;
            case 1:
                //YPos is adjecant
                xpos = (int) Math.floor(center.getX() + adj);
                ypos = (int) Math.floor(center.getY() + opp);
                break;
            case 2:
                xpos = (int) Math.floor(center.getX() - opp);
                ypos = (int) Math.floor(center.getY() + adj);
                break;
            case 3:
                xpos = (int) Math.floor(center.getX() - adj);
                ypos = (int) Math.floor(center.getY() - opp);
                break;
            default:
                xpos = 0;
                ypos = 0;
        }
        return (new Point(xpos, ypos));
    }
}
