package ConquerSpace.game.ui.renderers;

import ConquerSpace.Globals;
import ConquerSpace.game.UniversePath;
import ConquerSpace.game.universe.civilizations.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.Sector;
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
        for (ControlDrawStats c : drawer.controlDrawStats) {
            
            Point pos = c.getPos();
            Ellipse2D.Float control = new Ellipse2D.Float(pos.x - 25, pos.y - 25, 50, 50);
            g2d.setColor(new Color(c.getColor().getRed(), c.getColor().getBlue(), c.getColor().getGreen(), 127));
            g2d.fill(control);
        }
        
        for (SystemDrawStats s : drawer.stats) {
            for (UniversePath p : Globals.universe.getCivilization(0).vision.keySet()) {
                if (p.getSystemID()== s.getPath().getSystemID()&& Globals.universe.getCivilization(0).vision.get(p) > VisionTypes.UNDISCOVERED) {
                    Ellipse2D.Float star = new Ellipse2D.Float(s.getPosition().x, s.getPosition().y, 20, 20);
                    g2d.setColor(s.getColor());
                    g2d.fill(star);
                }
            }
        }

        //Draw scale line
        Line2D.Float line = new Line2D.Float(10, 20, drawer.sizeOfLtyr * 30 + 10, 20);
        g2d.setColor(Color.ORANGE);
        g2d.draw(line);
        g2d.drawString("30 light years", 10, 10);
    }
}
