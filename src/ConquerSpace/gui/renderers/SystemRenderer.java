package ConquerSpace.gui.renderers;

import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.PlanetTypes;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.StarTypes;
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
public class SystemRenderer {

    public static final int PLANET_DIVISOR = 7;
    private Dimension bounds;

    private Universe universe;
    private StarSystem sys;
    public int sizeofAU;

    public SystemRenderer(StarSystem sys, Universe u, Dimension bounds) {
        this.bounds = bounds;
        universe = u;
        this.sys = sys;
        //Size of star system
        int systemDrawnSize = (((bounds.height < bounds.width) ? bounds.height : bounds.width) / 2);

        long size = 0;
        for (int i = 0; i < sys.getPlanetCount(); i++) {
            if (sys.getPlanet(i).getOrbitalDistance() > size) {
                size = sys.getPlanet(i).getOrbitalDistance();
            }
        }

        sizeofAU = 15;
    }

    public void drawStarSystem(Graphics g, double translateX, double translateY, double scale) {
        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(Color.BLACK);
        g2d.fill(bg);

        for (int i = 0; i < sys.getStarCount(); i++) {
            Star star = sys.getStar(i);

            Ellipse2D.Double thingy = new Ellipse2D.Double(
                    (translateX + bounds.width / 2) * scale - star.starSize / 50000 / 2,
                    (translateY + bounds.height / 2) * scale - star.starSize / 50000 / 2,
                    star.starSize / 50000, star.starSize / 50000);
            Color c;
            switch (star.type) {
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
            g2d.fill(thingy);
        }

        for (int i = 0; i < sys.getPlanetCount(); i++) {
            Planet p = sys.getPlanet(i);
            //Draw orbit circle
            Ellipse2D.Double circle = new Ellipse2D.Double(
                    (translateX + bounds.width / 2 - p.getOrbitalDistance() * sizeofAU / 10_000_000) * scale,
                    (translateY + bounds.height / 2 - p.getOrbitalDistance() * sizeofAU / 10_000_000) * scale,
                    (p.getOrbitalDistance()) * sizeofAU / 10_000_000 * 2 * scale,
                    (p.getOrbitalDistance()) * sizeofAU / 10_000_000 * 2 * scale);
            g2d.setColor(Color.WHITE);
            g2d.draw(circle);
        }

        for (int i = 0; i < sys.getPlanetCount(); i++) {
            Planet p = sys.getPlanet(i);
            //Draw orbit circle
            Ellipse2D.Double planet = new Ellipse2D.Double((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) * scale - (p.getPlanetSize() / PLANET_DIVISOR / 2),
                    (translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.height / 2) * scale - (p.getPlanetSize() / PLANET_DIVISOR / 2),
                    p.getPlanetSize() / PLANET_DIVISOR, p.getPlanetSize() / PLANET_DIVISOR);
            switch (p.getPlanetType()) {
                case PlanetTypes.GAS:
                    g2d.setColor(Color.MAGENTA);
                    break;
                case PlanetTypes.ROCK:
                    g2d.setColor(Color.ORANGE);
                    break;
            }
            //g2d.setColor(p.g());
            g2d.fill(planet);
            //Draw owner

            //Draw name and background
            if (!p.getName().equals("")) {
                g2d.setColor(Color.gray);

                if (p.isHabitated()) {
                    g2d.setColor(Color.red);
                    g2d.fill(new Rectangle2D.Double(
                            (translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) * scale - (g2d.getFontMetrics().stringWidth(p.getName()) + 3) / 2,
                            (translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.width / 2) * scale + (p.getPlanetSize() / PLANET_DIVISOR / 2),
                            (g2d.getFontMetrics().stringWidth(p.getName()) + 3), (g2d.getFontMetrics().getHeight()) + 3)
                    );
                    g2d.setColor(Color.white);
                }

                g2d.drawString(p.getName(),
                        (float) ((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) * scale) - (g2d.getFontMetrics().stringWidth(p.getName()) + 3) / 2,
                        (float) ((translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.width / 2) * scale) + (p.getPlanetSize() / PLANET_DIVISOR / 2) + g2d.getFontMetrics().getHeight());
            }
        }

        //draw spaceships
        for (Ship ship : sys.spaceShips) {
            double x = (ship.getX());
            double y = (ship.getY());
            //Draw dot
            g2d.setColor(Color.yellow);
            g2d.fill(new Ellipse2D.Double((translateX + x * sizeofAU / 10_000_000 + bounds.width / 2) * scale - 5,
                    (translateY + y * sizeofAU / 10_000_000 + bounds.width / 2) * scale - 5, 10, 10));
            ship.getName();
        }
        //Draw scale line
        // TODO: MAKE ACCURATE!
        Line2D.Float line = new Line2D.Float(10, 20, (float) (sizeofAU * 20 * scale + 10), 20);
        g2d.setColor(Color.yellow);
        g2d.draw(line);
        g2d.drawString((20d / (double) sizeofAU) + " AU", 10, 10);
    }
}
