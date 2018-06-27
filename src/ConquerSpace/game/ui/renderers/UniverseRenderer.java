package ConquerSpace.game.ui.renderers;

import ConquerSpace.Globals;
import ConquerSpace.game.UniversePath;
import ConquerSpace.game.universe.civilizations.VisionTypes;
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
 * Jpanel of the universe. Displays the whole of it.
 *
 * @author Zyun
 */
public class UniverseRenderer extends JPanel {

    //private static final Logger LOGGER = CQSPLogger.getLogger(UniverseRenderer.class.getName());
    private Dimension bounds;
    public UniverseDrawer drawer;

    Universe universe;

    public UniverseRenderer(Dimension bounds, Universe universe) {
        this.bounds = bounds;
        this.universe = universe;
        drawer = new UniverseDrawer(universe, bounds);

        setPreferredSize(bounds);
        //LOGGER.info("Displaying universe " + universe.toReadableString());
    }

    /**
     * Paints the universe.
     *
     * @param g Graphics.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Paint bounds dark blue.
        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(new Color(0, 0, 255));
        g2d.fill(bg);

        Ellipse2D.Float universeCircle = new Ellipse2D.Float(0, 0, drawer.universeDrawnSize, drawer.universeDrawnSize);
        g2d.setColor(Color.BLACK);
        g2d.fill(universeCircle);

        for (SectorDrawStats s : drawer.sectorDrawings) {
            //Check vision
            for (UniversePath pa : universe.getCivilization(0).vision.keySet()) {
                if (pa.getSectorID() == s.getId() && universe.getCivilization(0).vision.get(pa) > VisionTypes.UNDISCOVERED) {
                    //Draw the sectors
                    Point p = s.getPosition();

                    Ellipse2D.Float sector = new Ellipse2D.Float(p.x - s.getRadius(), p.y - s.getRadius(), s.getRadius() * 2, s.getRadius() * 2);
                    Line2D.Float ln = new Line2D.Float(p, new Point(drawer.universeDrawnSize / 2, drawer.universeDrawnSize / 2));

                    // Draw star systems   
                    for (SystemDrawStats sys : s.systems) {
                        if (universe.getCivilization(0).vision.get(sys.getPath()) > 0) {
                            if (universe.control.get(sys.getPath()) > -1) {
                                switch (universe.getCivilization(0).vision.
                                        get(sys.getPath())) {
                                    case VisionTypes.KNOWS_INTERIOR:
                                        g2d.setColor(Color.gray);
                                        break;
                                    default:
                                        g2d.setColor(universe.getCivilization(universe.control.get(sys.getPath())).getColor());
                                }
                                //Control, if any...
                                Ellipse2D.Float control = new Ellipse2D.Float(sys.getPosition().x - 5, sys.getPosition().y - 5, 10, 10);
                                g2d.fill(control);
                            }
                            
                            g2d.setColor(sys.getColor());
                            Ellipse2D.Float system = new Ellipse2D.Float(sys.getPosition().x, sys.getPosition().y, 2, 2);
                            g2d.fill(system);

                            Line2D.Float systemln = new Line2D.Float(sys.getPosition(), p);
                            g2d.setColor(Color.orange);
                            //Unncomment for debugging
                            //g2d.draw(systemln);
                        }
                    }
                    g2d.setColor(Color.RED);
                    g2d.draw(sector);

                    g2d.setColor(Color.orange);
                    //Uncomment this for debugging
                    //g2d.draw(ln);
                    break;
                }
            }
            //Draw scale line
            Line2D.Float line = new Line2D.Float(10, 20, drawer.sizeOfLtyr * 30 + 10, 20);
            g2d.draw(line);
            g2d.drawString("30 light years", 10, 10);
        }
    }
}
