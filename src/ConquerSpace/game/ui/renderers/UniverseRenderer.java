package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import org.apache.logging.log4j.Logger;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JPanel;

/**
 * Jpanel of the universe. Displays the whole of it.
 *
 * @author Zyun
 */
public class UniverseRenderer extends JPanel {

    private static final Logger LOGGER = CQSPLogger.getLogger(UniverseRenderer.class.getName());
    private Dimension bounds;
    private UniverseDrawer drawer;

    public UniverseRenderer(Dimension bounds, Universe universe) {
        this.bounds = bounds;

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
            //Draw the sectors
            Point p = s.getPosition();

            Ellipse2D.Float sector = new Ellipse2D.Float(p.x - s.getRadius(), p.y - s.getRadius(), s.getRadius() * 2, s.getRadius() * 2);
            Line2D.Float ln = new Line2D.Float(p, new Point(drawer.universeDrawnSize / 2, drawer.universeDrawnSize / 2));
            //Draw control
            for (ControlDrawStats c : drawer.controlDrawStats) {
                Point pos = c.getPos();
                Ellipse2D.Float control = new Ellipse2D.Float(pos.x - 5, pos.y - 5, 10, 10);
                g2d.setColor(new Color(c.getColor().getRed(), c.getColor().getBlue(), c.getColor().getGreen(), 10));
                g2d.fill(control);
            }
            // Draw star systems
            for (SystemDrawStats sys : s.systems) {
                g2d.setColor(sys.getColor());
                Ellipse2D.Float system = new Ellipse2D.Float(sys.getPos().x, sys.getPos().y, 2, 2);
                g2d.fill(system);

                Line2D.Float systemln = new Line2D.Float(sys.getPos(), p);
                g2d.setColor(Color.orange);
                //g2d.draw(systemln);
            }
            g2d.setColor(Color.RED);
            g2d.draw(sector);

            g2d.setColor(Color.orange);
            //g2d.draw(ln);
        }
    }
}
