package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.civilization.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.Sector;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * Renders sector and all the containing star systems.
 *
 * @author Zyun
 */
public class SectorRenderer extends JPanel {

    private Dimension bounds;
    public SectorDrawer drawer;

    private Universe universe;

    public SectorRenderer(Dimension bounds, Sector sector, Universe u) {
        this.universe = u;
        //Draw background
        this.bounds = bounds;

        drawer = new SectorDrawer(sector, universe, bounds);

        setPreferredSize(bounds);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(Color.BLACK);
        g2d.fill(bg);

        Ellipse2D.Float sectorCircle = new Ellipse2D.Float(0, 0, drawer.sectorDrawnSize, drawer.sectorDrawnSize);
        g2d.setColor(Color.red);
        g2d.draw(sectorCircle);

        for (SystemDrawStats s : drawer.stats) {
            //Check vision
            if (universe.getCivilization(0).vision.get(s.getPath()) > VisionTypes.UNDISCOVERED) {
                if (universe.getCivilization(0).vision.get(s.getPath()) > VisionTypes.KNOWS_INTERIOR) {
                    //Control
                    if (universe.control.get(s.getPath()) > -1) {
                        switch (universe.getCivilization(0).vision.
                                get(s.getPath())) {
                            case VisionTypes.KNOWS_INTERIOR:
                                g2d.setColor(Color.gray);
                                break;
                            default:
                                g2d.setColor(universe.getCivilization(universe.control.get(s.getPath())).getColor().darker());
                        }
                        //Control, if any...
                        Ellipse2D.Float control = new Ellipse2D.Float(s.getPosition().x - 12, s.getPosition().y - 12, 50, 50);
                        g2d.fill(control);
                    }
                }
                Ellipse2D.Float star = new Ellipse2D.Float(s.getPosition().x, s.getPosition().y, 20, 20);
                g2d.setColor(s.getColor());
                g2d.fill(star);
            }
        }

        //Draw scale line
        Line2D.Float line = new Line2D.Float(10, 20, drawer.sizeOfLtyr * 30 + 10, 20);
        g2d.setColor(Color.ORANGE);
        g2d.draw(line);
        g2d.drawString("30 light years", 10, 10);
    }

    public void drawSector(Graphics g, Point translate) {
        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(Color.BLACK);
        g2d.fill(bg);

        Ellipse2D.Float sectorCircle = new Ellipse2D.Float(translate.x, translate.y, drawer.sectorDrawnSize, drawer.sectorDrawnSize);
        g2d.setColor(Color.red);
        g2d.draw(sectorCircle);

        for (SystemDrawStats s : drawer.stats) {
            //Check vision
            if (universe.getCivilization(0).vision.get(s.getPath()) > VisionTypes.UNDISCOVERED) {
                if (universe.getCivilization(0).vision.get(s.getPath()) > VisionTypes.KNOWS_INTERIOR) {
                    //Control
                    if (universe.control.get(s.getPath()) > -1) {
                        switch (universe.getCivilization(0).vision.
                                get(s.getPath())) {
                            case VisionTypes.KNOWS_INTERIOR:
                                g2d.setColor(Color.gray);
                                break;
                            default:
                                g2d.setColor(universe.getCivilization(universe.control.get(s.getPath())).getColor().darker());
                        }
                        //Control, if any...
                        Ellipse2D.Float control = new Ellipse2D.Float(s.getPosition().x - 12 + translate.x, s.getPosition().y - 12 + translate.y, 50, 50);
                        g2d.fill(control);
                    }
                }
                Ellipse2D.Float star = new Ellipse2D.Float(s.getPosition().x + translate.x, s.getPosition().y + translate.y, 20, 20);
                g2d.setColor(s.getColor());
                g2d.fill(star);
            }
        }

        //Draw scale line
        Line2D.Float line = new Line2D.Float(10, 20, drawer.sizeOfLtyr * 30 + 10, 20);
        g2d.setColor(Color.ORANGE);
        g2d.draw(line);
        g2d.drawString("30 light years", 10, 10);
    }
}
