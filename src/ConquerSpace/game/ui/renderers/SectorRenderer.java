package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.spaceObjects.Sector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * Renders sector and all the containing star systems.
 * @author Zyun
 */
public class SectorRenderer extends JPanel{
    private Dimension bounds;
    private SectorDrawer drawer;
    public SectorRenderer(Dimension bounds, Sector sector) {
        //Draw background
        this.bounds = bounds;
        
        drawer = new SectorDrawer(sector, bounds);
        
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
        for (SystemDrawStats s: drawer.stats) {
            Ellipse2D.Float star = new Ellipse2D.Float(s.getPos().x, s.getPos().y, 20, 20);
            g2d.setColor(s.getColor());
            g2d.fill(star);
        }
    }
}
