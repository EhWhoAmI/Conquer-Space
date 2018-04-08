package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.spaceObjects.StarSystem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public class SystemRenderer extends JPanel {

    public SystemInternalsDrawer drawer;
    private Dimension bounds;

    public SystemRenderer(StarSystem sys, Dimension bounds) {
        this.bounds = bounds;
        drawer = new SystemInternalsDrawer(sys, bounds);
        setPreferredSize(bounds);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(Color.BLACK);
        g2d.fill(bg);

        for (StarDrawStats s : drawer.stats.starDrawStats) {
            Ellipse2D.Float thingy = new Ellipse2D.Float(s.getPos().x - s.getDiameter() / 2, s.getPos().y - s.getDiameter() / 2, s.getDiameter(), s.getDiameter());
            g2d.setColor(s.getColor());
            g2d.fill(thingy);
        }
        for (PlanetDrawStats p : drawer.stats.planetDrawStats) {
            //Draw orbit circle
            Ellipse2D.Float circle = new Ellipse2D.Float(bounds.width/2 - p.getOrbitPath(), bounds.height/2 - p.getOrbitPath(), p.getOrbitPath()*2, p.getOrbitPath()*2);
            g2d.setColor(Color.WHITE);
            g2d.draw(circle);
            
            Ellipse2D.Float planet = new Ellipse2D.Float(p.getPos().x - (p.getSize()/2), p.getPos().y - (p.getSize()/2), p.getSize(), p.getSize());
            g2d.setColor(p.getColor());
            g2d.fill(planet);
         }
    }

}
