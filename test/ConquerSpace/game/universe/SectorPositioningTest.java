package ConquerSpace.game.universe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * In a nutshell, this tests the math for the universe renderer. BTW, we will have to mod this for the actual code.
 * @author Zyun
 */
public class SectorPositioningTest {
    public static void main(String[] args) {
        //2 integers for degrees, and distance.
        double degs = 2;
        double dist = 13;
        //Random point
        int ptX = 3;
        int pty = 7;
        System.out.println("X " + ptX + " Y " + pty + " Degrees " + degs + " Distance " + degs);
        
        //Do the math!!!
        
        //Step by step analysis.
        //Dist is the hypotonuse.
        //First, calculate the opposite of the triangle we are to create.
        double opp = (Math.sin(Math.toRadians(degs)) * dist);
        System.out.println("Opp: " + opp);
        //Then the adjacent
        double adj = (Math.cos(Math.toRadians(degs)) * dist);
        System.out.println("Adj: " + adj);
        //Then we have the angles of the triangle.
        //Test if it is a right triangle
        
        System.out.println("Actual math...");
        System.out.println(Math.pow(adj, 2) +  " + " + Math.pow(opp, 2) + " = " + (Math.pow(adj, 2) + Math.pow(opp, 2)));
        System.out.println("Supposed to be...");
        System.out.println(Math.pow(adj, 2) + " + " + Math.pow(opp, 2) + " = " + Math.pow(dist, 2));
        
        //Computer isn't exactly accurate, so the round is to check.
        if (Math.round(Math.pow(adj, 2) + Math.pow(opp, 2)) == Math.pow(dist, 2)) {
            //Then it is a rt triangle
            System.out.println("Is a rt triangle");
        }
        
        //Then add the things together.
        System.out.println("X and Y positions!!!");
        System.out.println("X = " + (ptX + opp) + " Y = " + (pty + adj));
        
        //Tests for other angles. (Angles above 90 degrees.
        {
            dist = 15;
            degs = 103;
            
            //Calculate...
            int rotation = 0;
            while (degs > 89) {
                degs -= 90;
                System.out.println("Subtracting 90");
                rotation ++;
            }
            System.out.println("Calculating degrees "  + degs);
            //Then do the usual math...
            
            //Step by step analysis.
        //Dist is the hypotonuse.
        //First, calculate the opposite of the triangle we are to create.
        opp = (Math.sin(Math.toRadians(degs)) * dist);
        System.out.println("Opp: " + opp);
        //Then the adjacent
        adj = (Math.cos(Math.toRadians(degs)) * dist);
        System.out.println("Adj: " + adj);
        //Then we have the angles of the triangle.
        //Test if it is a right triangle
        
        System.out.println("Actual math...");
        System.out.println(Math.pow(adj, 2) +  " + " + Math.pow(opp, 2) + " = " + (Math.pow(adj, 2) + Math.pow(opp, 2)));
        System.out.println("Supposed to be...");
        System.out.println(Math.pow(adj, 2) + " + " + Math.pow(opp, 2) + " = " + Math.pow(dist, 2));
        
        //Computer isn't exactly accurate, so the round is to check.
        if (Math.round(Math.pow(adj, 2) + Math.pow(opp, 2)) == Math.pow(dist, 2)) {
            //Then it is a rt triangle
            System.out.println("Is a rt triangle");
        }
        
        //This is where it is different, we need to swap things around.
            System.out.println("Rotation: " + rotation);
        double finX = 0;
        double finY = 0;
        switch(rotation) {
            case 0:
                finX = (ptX + opp);
                finY = (pty + adj);
                break;
            case 1:
                finX = (ptX + adj);
                finY = (pty - opp);
                break;
            case 2:
                finX = (ptX - opp);
                finY = (pty - adj);
                break;
            case 3:
                finX = (ptX - adj);
                finY = (pty + opp);
                break;
        }
            System.out.println("X = " + finX + " Y = " + finY);
            displayTest();
        }
    }
    
    public static void displayTest() {
        //int de
        JFrame frame = new JFrame("Display");
        frame.setSize(350, 350);
        System.out.println("------------");
        JPanel pan = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                //Values go here!!
                double dist = 100;
                double degs = 223;
                
                
                int smallSide = (this.getWidth() < this.getHeight())?this.getWidth():this.getHeight();
                //Draw details
                Ellipse2D.Float circle = new Ellipse2D.Float(0, 0, smallSide, smallSide);
                g2d.fill(circle);
                //Draw center
                g2d.setColor(Color.red);
                Ellipse2D.Float center = new Ellipse2D.Float(smallSide/2, smallSide/2, 2, 2);
                g2d.fill(center);
                
                int rot = 0;
                while (degs > 89) {
                    degs -= 90;
                    rot ++;
                }
                
                //Now do the math of the positioning values.
                //Adj
                int opp = (int) Math.round(Math.sin(Math.toRadians(degs)) * dist);
                //Opp
                int adj = (int) Math.round(Math.cos(Math.toRadians(degs)) * dist);
                int xpos = 0;
                int ypos = 0;
                
                switch (rot) {
                    case 0:
                        xpos = (smallSide/2 + opp);
                        ypos = (smallSide/2 - adj);
                        break;
                    case 1:
                        xpos = (smallSide/2 + adj);
                        ypos = (smallSide/2 + opp);
                        break;
                    case 2:
                        xpos = (smallSide/2 - opp);
                        ypos = (smallSide/2 + adj);
                        break;
                    case 3:
                        xpos = (smallSide/2 - adj);
                        ypos = (smallSide/2 - opp);
                        break;
                }
                System.out.println("Adj = " + adj + " Opp = " + opp);
                System.out.println("Final = X: " + xpos + " Y: " + ypos);
                System.out.println("Circle size: " + (smallSide/2));
                //Now draw the line.
                
                
                Line2D.Double ln = new Line2D.Double(smallSide/2, smallSide/2, xpos, ypos);
                g2d.draw(ln);
                
                g2d.setColor(Color.WHITE);
                Line2D.Float d = new Line2D.Float(smallSide/2, 0, smallSide/2, smallSide);
                Line2D.Float f = new Line2D.Float(0, smallSide/2, smallSide, smallSide/2);
                g2d.draw(d);
                g2d.draw(f);
            }
           
        };
        pan.setBounds(0, 0, 300, 300);
        frame.add(pan);
        frame.setVisible(true);
        
    }
}
