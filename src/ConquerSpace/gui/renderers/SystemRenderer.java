package ConquerSpace.gui.renderers;

import ConquerSpace.game.actions.ShipAction;
import ConquerSpace.game.actions.ShipMoveAction;
import ConquerSpace.game.actions.ToOrbitAction;
import ConquerSpace.game.universe.ships.SpaceShip;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.PlanetTypes;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.StarTypes;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

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

    private BufferedImage[] systemTerrain;

    public SystemRenderer(StarSystem sys, Universe u, Dimension bounds) {
        this.bounds = bounds;
        universe = u;
        this.sys = sys;

        //Terrain
        systemTerrain = new BufferedImage[sys.getPlanetCount()];
        //The terrain will be circles

        //Size of star system
        long size = 0;
        for (int i = 0; i < sys.getPlanetCount(); i++) {
            if (sys.getPlanet(i).getOrbitalDistance() > size) {
                size = sys.getPlanet(i).getOrbitalDistance();
            }
            //render terrain
            if (sys.getPlanet(i).getPlanetType() == PlanetTypes.ROCK) {
                TerrainRenderer tr = new TerrainRenderer(sys.getPlanet(i));
                systemTerrain[i] = toBufferedImage(tr.getImage(0.15));

                //Create copy
                BufferedImage temp = resize(systemTerrain[i], systemTerrain[i].getHeight(), systemTerrain[i].getHeight());
                systemTerrain[i] = new BufferedImage(systemTerrain[i].getHeight(), systemTerrain[i].getWidth(), BufferedImage.TYPE_INT_ARGB);

                for (int x = 0; x < temp.getWidth(); x++) {
                    for (int y = 0; y < temp.getHeight(); y++) {
                        //Draw
                        int centerX = temp.getWidth() / 2;

                        int centerY = temp.getHeight() / 2;
                        if (Math.sqrt((centerY - y) * (centerY - y) + (centerX - x) * (centerX - x)) <= temp.getWidth() / 2) {
                            systemTerrain[i].setRGB(x, y, temp.getRGB(x, y));
                        }
                    }
                }
            }
        }

        sizeofAU = 15;
    }

    public void drawStarSystem(Graphics g, double translateX, double translateY, double scale) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(Color.BLACK);
        //g2d.fill(bg);

        //X Y grid for reference
        /*Line2D.Double xline = new Line2D.Double(
                (translateX + bounds.width / 2) / scale,
                (translateY + bounds.width / 2 + 10000) / scale,
                (translateX + bounds.width / 2) / scale,
                (translateY + bounds.width / 2 - 10000) / scale
        );

        g2d.setColor(Color.WHITE);
        g2d.draw(xline);
        Line2D.Double yline = new Line2D.Double(
                (translateX + bounds.width / 2 + 10000) / scale,
                (translateY + bounds.width / 2) / scale,
                (translateX + bounds.width / 2 - 10000) / scale,
                (translateY + bounds.width / 2) / scale
        );
        g2d.setColor(Color.WHITE);
        g2d.draw(yline);*/
        for (int i = 0; i < sys.getStarCount(); i++) {
            Star star = sys.getStar(i);

            Ellipse2D.Double thingy = new Ellipse2D.Double(
                    (translateX + bounds.width / 2) / scale - star.starSize / 50000 / 2,
                    (translateY + bounds.height / 2) / scale - star.starSize / 50000 / 2,
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
                    (translateX + bounds.width / 2 - p.getOrbitalDistance() * sizeofAU / 10_000_000) / scale,
                    (translateY + bounds.height / 2 - p.getOrbitalDistance() * sizeofAU / 10_000_000) / scale,
                    (p.getOrbitalDistance()) * sizeofAU / 10_000_000 * 2 / scale,
                    (p.getOrbitalDistance()) * sizeofAU / 10_000_000 * 2 / scale);
            g2d.setColor(Color.WHITE);
            g2d.draw(circle);
        }

        for (int i = 0; i < sys.getPlanetCount(); i++) {
            Planet p = sys.getPlanet(i);
            //Draw orbit circle
            Ellipse2D.Double planet = new Ellipse2D.Double((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2),
                    (translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.height / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2),
                    p.getPlanetSize() / PLANET_DIVISOR, p.getPlanetSize() / PLANET_DIVISOR);
            switch (p.getPlanetType()) {
                case PlanetTypes.GAS:
                    g2d.setColor(Color.MAGENTA);
                    g2d.fill(planet);

                    break;
                case PlanetTypes.ROCK:
                    g2d.setColor(Color.ORANGE);
                    break;
            }

            g2d.drawImage(systemTerrain[i], (int) ((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2)),
                    (int) ((translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.height / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2)), null);
            //g2d.setColor(p.g());
            int planetX = (int) ((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2));
            int planetY = (int) ((translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2));

            //Draw shadow
            //Get slope
            double slope = (((translateY + bounds.height / 2) / scale)
                    - ((int) ((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale)))
                    / (((translateX + bounds.width / 2) / scale) - ((int) ((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale)));
            //Do it!
            GradientPaint shadow = new GradientPaint(planetX, planetY, new Color(0, 0, 0, 0),
                    (float) (planetX + (p.getPlanetSize() / PLANET_DIVISOR) / 1.1), (float) (planetY + (p.getPlanetSize() / PLANET_DIVISOR) / 1.1), Color.BLACK);

//            g2d.setColor(Color.RED);
//            g2d.fill(new Ellipse2D.Float((float) planetX, (float) planetY, 10, 10));
//            g2d.setColor(Color.BLUE);
//            g2d.fill(new Ellipse2D.Float((float) (planetX + (p.getPlanetSize() / PLANET_DIVISOR / 2)), (float) (planetY + (p.getPlanetSize() / PLANET_DIVISOR / 2)), 10, 10));
            //g2d.setPaint(shadow);
            //Draw owner
            g2d.setColor(Color.BLACK);
            Arc2D.Float shadowArc = new Arc2D.Float(Arc2D.CHORD);
            shadowArc.height = (p.getPlanetSize() / PLANET_DIVISOR);
            shadowArc.width = (p.getPlanetSize() / PLANET_DIVISOR);
            shadowArc.x = (int) ((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2));
            shadowArc.y = (int) ((translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.height / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2));
            shadowArc.start = (int) (p.getPlanetDegrees() - 100);
            shadowArc.extent = (200);
            g2d.fill(shadowArc);
            /*g.fillArc((int) ((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2)),
                    (int) ((translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.height / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2)), (p.getPlanetSize() / PLANET_DIVISOR), (p.getPlanetSize() / PLANET_DIVISOR), (int) (p.getPlanetDegrees() - 90), (180));*/

            //Draw name and background
            if (!p.getName().equals("")) {
                g2d.setColor(Color.gray);

                if (p.isHabitated()) {
                    g2d.setColor(Color.red);
                    g2d.fill(new Rectangle2D.Double(
                            (translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale - (g2d.getFontMetrics().stringWidth(p.getName()) + 3) / 2,
                            (translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale + (p.getPlanetSize() / PLANET_DIVISOR / 2),
                            (g2d.getFontMetrics().stringWidth(p.getName()) + 3), (g2d.getFontMetrics().getHeight()) + 3)
                    );
                    g2d.setColor(Color.white);
                }

                g2d.drawString(p.getName(),
                        (float) ((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale) - (g2d.getFontMetrics().stringWidth(p.getName()) + 3) / 2,
                        (float) ((translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale) + (p.getPlanetSize() / PLANET_DIVISOR / 2) + g2d.getFontMetrics().getHeight());
            }
        }

        //Interstellar boundary...
        long interstellardistance = sys.getPlanet(sys.getPlanetCount() - 1).getOrbitalDistance();
        interstellardistance += 10 * 10_000_000;
        Ellipse2D.Double boundaryCircle = new Ellipse2D.Double(
                (translateX + bounds.width / 2 - interstellardistance * sizeofAU / 10_000_000) / scale,
                (translateY + bounds.height / 2 - interstellardistance * sizeofAU / 10_000_000) / scale,
                (interstellardistance) * sizeofAU / 10_000_000 * 2 / scale,
                (interstellardistance) * sizeofAU / 10_000_000 * 2 / scale);
        g2d.setColor(new Color(0, 127, 255));
        g2d.draw(boundaryCircle);

        //draw spaceships
        for (SpaceShip ship : sys.spaceShips) {
            double x = (ship.getX());
            double y = (ship.getY());
            //Draw dot
            g2d.setColor(Color.yellow);
            g2d.fill(new Ellipse2D.Double((translateX + x * sizeofAU / 10_000_000 + bounds.width / 2) / scale - 5,
                    (translateY + y * sizeofAU / 10_000_000 + bounds.width / 2) / scale - 5, 10, 10));
            //Show actions
            double previousX = ship.getX();
            double previousY = ship.getY();
            for (ShipAction act : ship.commands) {
                if (act instanceof ShipMoveAction) {
                    //Draw line
                    ShipMoveAction move = (ShipMoveAction) act;
                    Line2D.Double line = new Line2D.Double(
                            (translateX + previousX * sizeofAU / 10_000_000 + bounds.width / 2) / scale,
                            (translateY + previousY * sizeofAU / 10_000_000 + bounds.width / 2) / scale,
                            (translateX + move.getPosition().getX() * sizeofAU / 10_000_000 + bounds.width / 2) / scale,
                            (translateY + move.getPosition().getY() * sizeofAU / 10_000_000 + bounds.width / 2) / scale);
                    g2d.setColor(Color.green);

                    g2d.draw(line);
                    previousX = move.getPosition().getX();
                    previousY = move.getPosition().getY();
                    //Get current location and draw

                } else if (act instanceof ToOrbitAction) {
                    ToOrbitAction move = (ToOrbitAction) act;
                    Line2D.Double line = new Line2D.Double(
                            (translateX + previousX * sizeofAU / 10_000_000 + bounds.width / 2) / scale,
                            (translateY + previousY * sizeofAU / 10_000_000 + bounds.width / 2) / scale,
                            (translateX + move.getPosition().getX() * sizeofAU / 10_000_000 + bounds.width / 2) / scale,
                            (translateY + move.getPosition().getY() * sizeofAU / 10_000_000 + bounds.width / 2) / scale);
                    g2d.setColor(Color.cyan);
                    g2d.draw(line);
                    previousX = move.getPosition().getX();
                    previousY = move.getPosition().getY();
                }
            }
            //ship.getName();
        }
        //Draw scale line
        // TODO: MAKE ACCURATE!
        Line2D.Float line = new Line2D.Float(10, 20, (float) (sizeofAU * 20 / scale + 10), 20);
        g2d.setColor(Color.yellow);
        g2d.draw(line);
        g2d.drawString((20d / (double) sizeofAU) + " AU", 10, 10);
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
