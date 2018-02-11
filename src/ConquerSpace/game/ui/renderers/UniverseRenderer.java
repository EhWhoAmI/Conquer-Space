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
import java.awt.geom.Point2D;
import javax.swing.JPanel;

/**
 * Jpanel of the universe. Displays the whole of it.
 * @author Zyun
 */
public class UniverseRenderer extends JPanel{
    private static final Logger LOGGER = CQSPLogger.getLogger(UniverseRenderer.class.getName());
    private Dimension bounds;
    private Universe universe;
    private UniverseDrawer drawer;
    
    public UniverseRenderer(Dimension bounds, Universe universe) {
        this.bounds = bounds;
        this.universe = universe;
        
        drawer = new UniverseDrawer(universe, bounds);
        
        setPreferredSize(bounds);
        //LOGGER.info("Displaying universe " + universe.toReadableString());
    }
    
    /**
     * Paints the universe.
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
        
        for (UniverseDrawer.SectorDrawStats s : drawer.sectorDrawings) {
            //Draw the sectors
            Point p = s.getPosition();
            
            Ellipse2D.Float sector = new Ellipse2D.Float(p.x - s.getCircumference()/2, p.y - s.getCircumference()/2, s.getCircumference(), s.getCircumference());
            Line2D.Float ln = new Line2D.Float(p, new Point(drawer.universeDrawnSize/2, drawer.universeDrawnSize/2));
            
            g2d.setColor(Color.RED);
            g2d.draw(sector);
            
            g2d.setColor(Color.orange);
            g2d.draw(ln);
        }
    }
}
