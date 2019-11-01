package ConquerSpace.gui.renderers;

import ConquerSpace.game.actions.ShipAction;
import ConquerSpace.game.actions.ShipMoveAction;
import ConquerSpace.game.actions.ToOrbitAction;
import ConquerSpace.game.universe.ships.SpaceShip;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.StarTypes;
import ConquerSpace.game.universe.spaceObjects.Universe;
import static ConquerSpace.gui.game.GameWindow.CQSPDesktop.BOUNDS_SIZE;
import ConquerSpace.util.CQSPLogger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class SystemRenderer {

    private static final Logger LOGGER = CQSPLogger.getLogger(SystemRenderer.class.getName());

    public static final int PLANET_DIVISOR = 7;
    private Dimension bounds;

    private Universe universe;
    private StarSystem sys;
    public int sizeofAU;

    private BufferedImage[] systemTerrain;
    private Thread rendererThread;

    boolean processedRenders = false;
    double scaleSize = 20;

    private Point measureStart;
    private Point measureEnd;
    private boolean measuring = false;

    public SystemRenderer(StarSystem sys, Universe u, Dimension bounds) {
        this.bounds = bounds;
        universe = u;
        this.sys = sys;

        //Terrain
        systemTerrain = new BufferedImage[sys.getPlanetCount()];
        //The terrain will be circles

        //Size of star system
        Runnable r = new Runnable() {
            @Override
            public void run() {
                long beginning = System.currentTimeMillis();

                long size = 0;
                for (int i = 0; i < sys.getPlanetCount(); i++) {
                    if (sys.getPlanet(i).getOrbitalDistance() > size) {
                        size = sys.getPlanet(i).getOrbitalDistance();
                    }
                    //render terrain
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

                long end = System.currentTimeMillis();
                LOGGER.info("Time to render system: " + (end - beginning));
            }
        };
        rendererThread = new Thread(r);
        rendererThread.setName("renderer");
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
        if (!processedRenders) {
            rendererThread.start();
            processedRenders = true;
        }
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

            Ellipse2D.Double starCircle = new Ellipse2D.Double(
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
            g2d.fill(starCircle);
        }

        for (int i = 0; i < sys.getPlanetCount(); i++) {
            Planet p = sys.getPlanet(i);
            //Draw orbit circle
            Ellipse2D.Double orbitCitcle = new Ellipse2D.Double(
                    (translateX + bounds.width / 2 - p.getOrbitalDistance() * sizeofAU / 10_000_000) / scale,
                    (translateY + bounds.height / 2 - p.getOrbitalDistance() * sizeofAU / 10_000_000) / scale,
                    (p.getOrbitalDistance()) * sizeofAU / 10_000_000 * 2 / scale,
                    (p.getOrbitalDistance()) * sizeofAU / 10_000_000 * 2 / scale);
            g2d.setColor(Color.WHITE);
            g2d.draw(orbitCitcle);
        }

        for (int i = 0; i < sys.getPlanetCount(); i++) {
            Planet p = sys.getPlanet(i);

            if (systemTerrain[i] != null) {
                g2d.drawImage(systemTerrain[i],
                        (int) ((translateX + (bounds.width / 2) + (p.getX()) * sizeofAU / 10_000_000) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2)),
                        (int) ((translateY + (bounds.height / 2) + (p.getY()) * sizeofAU / 10_000_000) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2)), null);
            }
            //g2d.setColor(p.g());
            int planetX = (int) ((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2));
            int planetY = (int) ((translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2));

            //Draw shadow
            g2d.setColor(Color.BLACK);
            Arc2D.Float shadowArc = new Arc2D.Float(Arc2D.CHORD);
            shadowArc.height = ((p.getPlanetSize() * 1.1f) / PLANET_DIVISOR);
            shadowArc.width = ((p.getPlanetSize() * 1.1f) / PLANET_DIVISOR);
            shadowArc.x = (int) ((translateX + (p.getX()) * sizeofAU / 10_000_000 + bounds.width / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2));
            shadowArc.y = (int) ((translateY + (p.getY()) * sizeofAU / 10_000_000 + bounds.height / 2) / scale - (p.getPlanetSize() / PLANET_DIVISOR / 2));
            shadowArc.start = (int) (p.getPlanetDegrees() - 100);
            shadowArc.extent = (200);
            g2d.fill(shadowArc);

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
        }

        //Draw measurement line
        if (measuring) {
            //Draw the line

            //Get length of line to get the distance
            double distance = pointDistance(measureStart.x, measureStart.y, measureEnd.x, measureEnd.y);
            double spaceLength = distance/ scale*sizeofAU;//((sizeofAU * number) = pixels*scale

            //Get the halfway point to draw the text
            g2d.setColor(Color.blue);
            g2d.drawString(String.format("%.3f pixels, %.3f AU, %.3f km" + (sizeofAU * spaceLength / scale), distance, spaceLength, spaceLength*149598000), (measureStart.x + measureEnd.x) / 2 + 10, (measureStart.y + measureEnd.y) / 2 + 10);
            g2d.setColor(Color.orange);

            Line2D.Float measureLine = new Line2D.Float(measureStart, measureEnd);

            g2d.draw(measureLine);

        }

        Line2D.Float newLine = new Line2D.Float(100, 100, 100, 200);
        g2d.setColor(Color.orange);
        g2d.draw(newLine);

        //Draw scale line
        //Limit size of scale...
        for (int i = 0; i < 20; i++) {
            if ((sizeofAU * scaleSize / scale) > 100) {
                scaleSize -= 0.5;
            } else if ((sizeofAU * scaleSize / scale) < 50) {
                scaleSize += 0.5;
            }
        }

        Line2D.Float line = new Line2D.Float(10, 20, (float) (sizeofAU * scaleSize / scale + 10), 20);
        g2d.setColor(Color.yellow);
        g2d.draw(line);

        g2d.drawString(String.format("%.3f", (scaleSize / (double) sizeofAU)) + " AU", 10, 10);
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

    public void setMeasureDistance(Point start, Point end) {
        measureStart = start;
        measureEnd = end;
        measuring = true;
    }

    public void endMeasureDistance() {
        measuring = false;
    }

    private static double pointDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}
