package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.spaceObjects.StarSystem;
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
 *
 * @author Zyun
 */
public class SystemRenderer {

    public SystemInternalsDrawer drawer;
    private Dimension bounds;

    private Universe universe;

    public SystemRenderer(StarSystem sys, Universe u, Dimension bounds) {
        this.bounds = bounds;
        universe = u;
        drawer = new SystemInternalsDrawer(sys, u, bounds);
    }

    public void drawStarSystem(Graphics g, Point translate) {
        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(Color.BLACK);
        g2d.fill(bg);

        for (StarDrawStats s : drawer.stats.starDrawStats) {
            Ellipse2D.Float thingy = new Ellipse2D.Float(translate.x + s.getPos().x - s.getDiameter() / 2, translate.y + s.getPos().y - s.getDiameter() / 2, s.getDiameter(), s.getDiameter());
            g2d.setColor(s.getColor());
            g2d.fill(thingy);
        }
        for (PlanetDrawStats p : drawer.stats.planetDrawStats) {
            //Draw orbit circle
            Ellipse2D.Float circle = new Ellipse2D.Float(translate.x + bounds.width / 2 - p.getOrbitPath(), translate.y + bounds.height / 2 - p.getOrbitPath(), p.getOrbitPath() * 2, p.getOrbitPath() * 2);
            g2d.setColor(Color.WHITE);
            g2d.draw(circle);

            //Background
            if (p.getOwnerColor() != null) {
                int aurasize = 10;
                Ellipse2D.Float owner = new Ellipse2D.Float(translate.x + p.getPos().x - ((p.getSize() + aurasize) / 2), translate.y + p.getPos().y - ((p.getSize() + aurasize) / 2), p.getSize() + aurasize, p.getSize() + aurasize);
                g2d.setColor(p.getOwnerColor());
                g2d.fill(owner);
            }

            Ellipse2D.Float planet = new Ellipse2D.Float(translate.x + p.getPos().x - (p.getSize() / 2), translate.y + p.getPos().y - (p.getSize() / 2), p.getSize(), p.getSize());
            g2d.setColor(p.getColor());
            g2d.fill(planet);
            //Draw owner

            g2d.setColor(Color.white);
            g2d.drawString(p.getOwner(), p.getPos().x - (p.getSize() / 2) + translate.x, p.getPos().y - (p.getSize() / 2) + translate.y);
        }

        //Draw scale line
        Line2D.Float line = new Line2D.Float(10, 20, 50, 20);
        g2d.draw(line);
        g2d.drawString((20d / (double) drawer.sizeofAU) + " AU", 10, 10);
    }
}
