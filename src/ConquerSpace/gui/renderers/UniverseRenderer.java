package ConquerSpace.gui.renderers;

import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.StarTypes;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.gui.game.GameWindow;
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
public class UniverseRenderer {

    public int sizeOfLTYR = 1;
    private Dimension bounds;

    Universe universe;
    public int universeDrawnSize;

    public UniverseRenderer(Dimension bounds, Universe universe) {
        this.bounds = bounds;
        this.universe = universe;
        long universeRadius = 0;
        //LOGGER.info("Displaying universe " + universe.toReadableString());
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            StarSystem s = universe.getStarSystem(i);
            if (s.getGalaticLocation().getDistance() > universeRadius) {
                universeRadius = s.getGalaticLocation().getDistance();
            }
        }

        universeRadius++;
        //universeDimensionsLTYR = universeRadius;
        universeDrawnSize = (bounds.height > bounds.width) ? bounds.height : bounds.width;

        //Do fancy math to calculate the size of 1 light year. Divide the universe drawn size with universe details' diameter
        //Multiply by 2 because it is a radius
        sizeOfLTYR = (int) Math.floor(universeDrawnSize / (universeRadius * 2));

        //Calculate size
    }

    public void drawUniverse(Graphics g, double translateX, double translateY, double scale) {
        //Paint bounds dark blue.
        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(new Color(0, 0, 255));
        g2d.fill(bg);

        Ellipse2D.Double universeCircle = new Ellipse2D.Double(translateX * scale, translateY * scale, universeDrawnSize * scale, universeDrawnSize * scale);
        g2d.setColor(Color.BLACK);
        g2d.fill(universeCircle);

        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            //Check vision
            StarSystem sys = universe.getStarSystem(i);
            // Draw star systems   
            //Check vision...
            if (universe.getCivilization(0).vision.get(sys.getUniversePath()) > VisionTypes.UNDISCOVERED) {
                //Control
                if (universe.control.get(sys.getUniversePath()) > -1) {
                    switch (universe.getCivilization(0).vision.
                            get(sys.getUniversePath())) {
                        case VisionTypes.EXISTS:
                            g2d.setColor(Color.gray);
                            break;
                        default:
                            g2d.setColor(universe.getCivilization(universe.control.get(sys.getUniversePath())).getColor());
                    }
                    //Control, if any...
                    Ellipse2D.Double control = new Ellipse2D.Double(
                            scale * (sys.getX() * sizeOfLTYR + translateX + bounds.height / 2) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10) / 2,
                            scale * (sys.getY() * sizeOfLTYR + translateY + bounds.height / 2) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10) / 2,
                            (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10),
                            (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10));
                    g2d.fill(control);
                }
                //End of control
                Color c;
                switch (sys.getStar(0).type) {
                    case StarTypes.TYPE_A:
                        c = Color.decode("#D5E0FF");
                        break;
                    case StarTypes.TYPE_B:
                        c = Color.decode("#A2C0FF");
                        break;
                    case StarTypes.TYPE_O:
                        c = Color.decode("#92B5FF");
                        break;
                    case StarTypes.TYPE_F:
                        c = Color.decode("#F9F5FF");
                        break;
                    case StarTypes.TYPE_G:
                        c = Color.decode("#fff4ea");
                        break;
                    case StarTypes.TYPE_K:
                        c = Color.decode("#FFDAB5");
                        break;
                    case StarTypes.TYPE_M:
                        c = Color.decode("#FFB56C");
                        break;
                    default:
                        c = Color.BLACK;
                }
                g2d.setColor(c);
                
                Ellipse2D.Double system = new Ellipse2D.Double(
                        scale * (sys.getX() * sizeOfLTYR + translateX + bounds.height / 2) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2),
                        scale * (sys.getY() * sizeOfLTYR + translateY + bounds.height / 2) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2),
                        GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR, GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR);
                g2d.fill(system);
                
            } else {
                //Draw stars and stuff...
                g2d.setColor(Color.GRAY);
                Ellipse2D.Double system = new Ellipse2D.Double(
                        scale * (sys.getX() * sizeOfLTYR + translateX + bounds.height / 2) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2),
                        scale * (sys.getY() * sizeOfLTYR + translateY + bounds.height / 2) - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2),
                        GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR, GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR);
                g2d.fill(system);
            }
        }
        //Draw scale line
        // TODO: MAKE ACCURATE
        Line2D.Double line = new Line2D.Double(10, 20, (sizeOfLTYR * 30 * scale + 10), 20);
        g2d.draw(line);
        g2d.drawString("30 light years", 10, 10);
    }
}
