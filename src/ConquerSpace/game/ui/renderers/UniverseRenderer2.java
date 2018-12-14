package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.UniversePath;
import ConquerSpace.game.universe.civilization.controllers.LimitedUniverse;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.GameWindow;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Zyun
 */
public class UniverseRenderer2 {

    private Dimension bounds;
    public UniverseDrawer2 drawer;

    LimitedUniverse universe;

    public UniverseRenderer2(Dimension bounds, LimitedUniverse universe) {
        this.bounds = bounds;
        this.universe = universe;
        drawer = new UniverseDrawer2(universe, bounds);
        //LOGGER.info("Displaying universe " + universe.toReadableString());
    }

    public synchronized void drawUniverse(Graphics g, Point translate) {
        //Paint bounds dark blue.
        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(new Color(0, 0, 255));
        g2d.fill(bg);

        Ellipse2D.Float universeCircle = new Ellipse2D.Float(translate.x, translate.y, drawer.universeDrawnSize , drawer.universeDrawnSize);
        g2d.setColor(Color.BLACK);
        g2d.fill(universeCircle);

        for (SystemDrawStats s : drawer.systemDrawings) {
            //Check vision

            //Draw the sectors
            Point p = s.getPosition();

            Line2D.Float ln = new Line2D.Float(p, new Point((int) (((float) (drawer.universeDrawnSize / 2) + translate.x)), (int) ((((float) (drawer.universeDrawnSize / 2) + translate.y)))));

            // Draw star systems   
            
            //Check vision...
            if (/*universe.getCivilization(0).vision.get(s.getPath()) > VisionTypes.UNDISCOVERED*/true) {
                //Control
                if (universe.getControl().get(s.getPath()) > -1) {
                    switch (universe.getCivilization(0).vision.
                            get(s.getPath())) {
                        case VisionTypes.KNOWS_INTERIOR:
                            g2d.setColor(Color.gray);
                            break;
                        default:
                            g2d.setColor(universe.getCivilization(universe.getControl().get(s.getPath())).getColor());
                    }
                    //Control, if any...
                    Ellipse2D.Float control = new Ellipse2D.Float(
                            ((s.getPosition().x + translate.x)) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10)/2, 
                            ((s.getPosition().y + translate.y)) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10)/2, 
                            (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10), 
                            (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10));
                    g2d.fill(control);
                }
                //End of control
                
                g2d.setColor(s.getColor());
                Ellipse2D.Float system = new Ellipse2D.Float(
                        (s.getPosition().x + translate.x) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2),
                        (s.getPosition().y + translate.y) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2), 
                        GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR, GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR);
                g2d.fill(system);

                Line2D.Float systemln = new Line2D.Float(new Point(s.getPosition().x + translate.x, s.getPosition().y + translate.y), p);
                g2d.setColor(Color.orange);
                //Unncomment for debugging
                //g2d.draw(systemln);
            }

            g2d.setColor(Color.orange);
            //Uncomment this for debugging
            //g2d.draw(ln);
        }
        //Draw scale line
        // TODO: MAKE ACCURATE
        Line2D.Float line = new Line2D.Float(10, 20, drawer.sizeOfLtyr * 30 + 10, 20);
        g2d.draw(line);
        g2d.drawString("30 light years", 10, 10);
    }

    public synchronized void drawUniverse(Graphics g, Point translate, float scale) {
        //Paint bounds dark blue.
        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(new Color(0, 0, 255));
        g2d.fill(bg);

        Ellipse2D.Float universeCircle = new Ellipse2D.Float(translate.x * scale, translate.y * scale, drawer.universeDrawnSize * scale, drawer.universeDrawnSize * scale);
        g2d.setColor(Color.BLACK);
        g2d.fill(universeCircle);

        for (SystemDrawStats s : drawer.systemDrawings) {
            //Check vision

            //Draw the sectors
            Point p = s.getPosition();

            Line2D.Float ln = new Line2D.Float(p, new Point((int) (((float) (drawer.universeDrawnSize / 2) + translate.x) * scale), (int) ((((float) (drawer.universeDrawnSize / 2) + translate.y)) * scale)));

            // Draw star systems   
            
            //Check vision...
            if (universe.getCivilization(0).vision.get(s.getPath()) > VisionTypes.UNDISCOVERED) {
                //Control
                if (universe.getControl().get(s.getPath()) > -1) {
                    switch (universe.getCivilization(0).vision.
                            get(s.getPath())) {
                        case VisionTypes.KNOWS_INTERIOR:
                            g2d.setColor(Color.gray);
                            break;
                        default:
                            g2d.setColor(universe.getCivilization(universe.getControl().get(s.getPath())).getColor());
                    }
                    //Control, if any...
                    Ellipse2D.Float control = new Ellipse2D.Float(
                            scale * ((s.getPosition().x + translate.x)) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10)/2, 
                            scale * ((s.getPosition().y + translate.y)) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10)/2, 
                            (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10), 
                            (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10));
                    g2d.fill(control);
                }
                //End of control
                
                g2d.setColor(s.getColor());
                Ellipse2D.Float system = new Ellipse2D.Float(
                        scale * (s.getPosition().x + translate.x) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2),
                        scale * (s.getPosition().y + translate.y) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2), 
                        GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR, GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR);
                g2d.fill(system);

                Line2D.Float systemln = new Line2D.Float(new Point(s.getPosition().x + translate.x, s.getPosition().y + translate.y), p);
                g2d.setColor(Color.orange);
                //Unncomment for debugging
                //g2d.draw(systemln);
            }

            g2d.setColor(Color.orange);
            //Uncomment this for debugging
            //g2d.draw(ln);
        }
        //Draw scale line
        // TODO: MAKE ACCURATE
        Line2D.Float line = new Line2D.Float(10, 20, drawer.sizeOfLtyr * 30 + 10, 20);
        g2d.draw(line);
        g2d.drawString("30 light years", 10, 10);
    }
    
    public void refresh() {
        drawer.refresh();
    }
}
