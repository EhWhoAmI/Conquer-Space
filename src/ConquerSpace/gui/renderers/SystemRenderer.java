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

        sizeofAU = 1;
        if (size != 0) {
            sizeofAU = (int) (Math.floor(systemDrawnSize / (int) (size / 10000000) + 1));
        }
    }

    public void drawStarSystem(Graphics g, Point translate, float scale) {
        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(Color.BLACK);
        g2d.fill(bg);

        for (int i = 0; i < sys.getStarCount(); i++) {
            Star star = sys.getStar(i);
            Ellipse2D.Float thingy = new Ellipse2D.Float(
                    (translate.x - star.starSize / 100000 + bounds.width / 2) * scale,
                    (translate.y - star.starSize / 100000 + bounds.height / 2) * scale,
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
            Ellipse2D.Float circle = new Ellipse2D.Float(
                    (translate.x + bounds.width / 2 - p.getOrbitalDistance() / 10_000_000 * sizeofAU) * scale,
                    (translate.y + bounds.height / 2 - p.getOrbitalDistance() / 10_000_000 * sizeofAU) * scale,
                    (p.getOrbitalDistance() / 10_000_000) * sizeofAU * 2 * scale,
                    (p.getOrbitalDistance() / 10_000_000) * sizeofAU * 2 * scale);
            g2d.setColor(Color.WHITE);
            g2d.draw(circle);

            Ellipse2D.Float planet = new Ellipse2D.Float((translate.x + (p.getX() / 10_000_000) * sizeofAU + bounds.width / 2) * scale - (p.getPlanetSize() / 2),
                    (translate.y + (p.getY() / 10_000_000) * sizeofAU + bounds.height / 2) * scale - (p.getPlanetSize() / 2),
                    p.getPlanetSize(), p.getPlanetSize());
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
                g2d.setColor(Color.red);
                g2d.fill(new Rectangle2D.Float(
                        (translate.x + (p.getX() / 10_000_000) * sizeofAU + bounds.width / 2) * scale,
                        (translate.y + (p.getY() / 10_000_000) * sizeofAU + bounds.width / 2) * scale - g2d.getFontMetrics().getHeight(),
                        (g2d.getFontMetrics().stringWidth(p.getName()) + 3), (g2d.getFontMetrics().getHeight()) + 3)
                );

                g2d.setColor(Color.white);
                
                g2d.drawString(p.getName(),
                        (translate.x + (p.getX() / 10_000_000) * sizeofAU + bounds.width / 2)* scale,
                        (translate.y + (p.getY() / 10_000_000) * sizeofAU + bounds.width / 2) * scale);
            }
        }

        //draw spaceships
        for (Ship ship : sys.spaceShips) {
            int x = (int) (ship.getX() / 10000000);
            int y = (int) (ship.getY() / 10000000);
            //Draw dot
            g2d.setColor(Color.yellow);
            g2d.fill(new Ellipse2D.Float((translate.x + x * sizeofAU + bounds.width / 2)* scale, (translate.y + y * sizeofAU + bounds.width / 2)* scale, 10, 10));
            ship.getName();
        }
        //Draw scale line
        // TODO: MAKE ACCURATE!
        Line2D.Float line = new Line2D.Float(10, 20, 50, 20);
        g2d.draw(line);
        g2d.drawString((20d / (double) sizeofAU) + " AU", 10, 10);
    }
}
