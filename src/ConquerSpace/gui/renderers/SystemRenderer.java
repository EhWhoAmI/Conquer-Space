/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.gui.renderers;

import ConquerSpace.game.actions.ShipAction;
import ConquerSpace.game.actions.ShipMoveAction;
import ConquerSpace.game.actions.ToOrbitAction;
import ConquerSpace.game.universe.ships.SpaceShip;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Star;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.StarTypes;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.util.logging.CQSPLogger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Zyun
 */
public class SystemRenderer {

    private static final Logger LOGGER = CQSPLogger.getLogger(SystemRenderer.class.getName());

    public static final int PLANET_DIVISOR = 27;
    private Dimension bounds;

    private Universe universe;
    private StarSystem sys;
    public int sizeofAU;

    private BufferedImage[] systemTerrain;
//Background skybox
    private BufferedImage skybox;
    private Thread rendererThread;
    boolean processedRenders = false;
    double scaleSize = 20;

    private Point measureStart;
    private Point measureEnd;
    private boolean measuring = false;

    private Point mousePosition;

    private Dimension windowSize = new Dimension(1000, 1000);

    private double distanceRatio = 1;

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
                    systemTerrain[i] = toBufferedImage(tr.getSquareImage(1));

                    //Create copy
                    BufferedImage temp = resize(systemTerrain[i], systemTerrain[i].getHeight()/PLANET_DIVISOR, systemTerrain[i].getHeight()/PLANET_DIVISOR);
                    systemTerrain[i] = new BufferedImage(systemTerrain[i].getHeight()/PLANET_DIVISOR, systemTerrain[i].getWidth()/PLANET_DIVISOR, BufferedImage.TYPE_INT_ARGB);

                    for (int x = 0; x < temp.getWidth(); x++) {
                        for (int y = 0; y < temp.getHeight(); y++) {
                            //Draw
                            int centerX = temp.getWidth() / 2;

                            int centerY = temp.getHeight() / 2;

                            //Limit within circle
                            if (Math.sqrt((centerY - y) * (centerY - y) + (centerX - x) * (centerX - x)) <= temp.getWidth() / 2) {
                                systemTerrain[i].setRGB(x, y, temp.getRGB(x, y));
                            }
                        }
                    }
                }

                long end = System.currentTimeMillis();
                LOGGER.info("Time to render system: " + (end - beginning) + " ms");
            }
        };

        try {
            skybox = ImageIO.read(new File("assets/img/bg.png"));
        } catch (IOException ex) {
        }
        rendererThread = new Thread(r);
        rendererThread.setName("system " + sys.getId() + " renderer");
        sizeofAU = 15;
        distanceRatio = sizeofAU / 10_000_000d;
    }

    long fpsCounter = System.currentTimeMillis();

    public void drawStarSystem(Graphics g, double translateX, double translateY, double scale) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(Color.BLACK);
        if (skybox != null) {
            g2d.drawImage(skybox, (int) ((translateX / scale) * 0.025) - 30, (int) ((translateY / scale) * 0.025) - 30, null);
        }
        //Render bg image
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
            Planet planet = sys.getPlanet(i);
            //Draw orbit circle
            /*Ellipse2D.Double orbitCitcle = new Ellipse2D.Double(
                    (translateX + bounds.width / 2 - (p.getOrbitalDistance() * distanceRatio)) / scale,
                    (translateY + bounds.height / 2 - (p.getOrbitalDistance() * distanceRatio)) / scale,
                    (p.getOrbitalDistance()) * distanceRatio * 2 / scale,
                    (p.getOrbitalDistance()) * distanceRatio * 2 / scale);*/
            GeneralPath circle = new GeneralPath();

            //Do the same thing that we do for calculating position
            //    private RendererMath.Point calculatePoint(double smajor, double theta, double e, double rot) {
            RendererMath.Point ppt = calculatePoint(planet.getSemiMajorAxis(), 0, planet.getEccentricity(), planet.getRotation());

            double cx = (ppt.x * distanceRatio);
            double cy = (ppt.y * distanceRatio);
            circle.moveTo((translateX + bounds.width / 2 + cx) / scale, (translateY + bounds.width / 2 + cy) / scale);

            //So far, the accuracy does not make a large difference in terms of performance.
            //So, we do not even have to care about limiting drawing this shape lol
            double accuracy = 0.1;

            //Circle loop
            for (double k = 0; k <= 360; k += accuracy) {
                ppt = calculatePoint(planet.getSemiMajorAxis(), k, planet.getEccentricity(), planet.getRotation());
                cx = (ppt.x * distanceRatio);
                cy = (ppt.y * distanceRatio);
                circle.lineTo((translateX + bounds.width / 2 + cx) / scale, (translateY + bounds.width / 2 + cy) / scale);
            }
            circle.closePath();

            g2d.setColor(Color.WHITE);
            g2d.draw(circle);
            //g2d.draw(orbitCitcle);
        }

        for (int i = 0; i < sys.getPlanetCount(); i++) {
            Planet p = sys.getPlanet(i);

            //Check if out of bounds
            int planetX = (int) ((translateX + (p.getX()) * distanceRatio + bounds.width / 2) / scale);// - (p.getPlanetSize() / PLANET_DIVISOR / 2));
            int planetY = (int) ((translateY + (p.getY()) * distanceRatio + bounds.width / 2) / scale);// - (p.getPlanetSize() / PLANET_DIVISOR / 2));

            //A small size
            if ((planetX > -20 && planetX < (windowSize.getWidth() + 20)) && (planetY < (windowSize.getHeight() + 20) && planetY > -20)) {
                if (systemTerrain[i] != null) {
                    g2d.drawImage(systemTerrain[i],
                            planetX - (p.getPlanetHeight() / PLANET_DIVISOR / 2),
                            planetY - (p.getPlanetHeight() / PLANET_DIVISOR / 2), null);
                }

//            g2d.drawString(String.format("Planet position: %.5f, %.5f", p.getX(), p.getY()),
//                    planetX,
//                    planetY);
                //Draw shadow
                g2d.setColor(Color.BLACK);
                Arc2D.Float shadowArc = new Arc2D.Float(Arc2D.CHORD);
                shadowArc.height = ((p.getPlanetHeight() * 1.1f) / PLANET_DIVISOR);
                shadowArc.width = ((p.getPlanetHeight() * 1.1f) / PLANET_DIVISOR);
                shadowArc.x = (int) ((translateX + (p.getX()) * distanceRatio + bounds.width / 2) / scale - (p.getPlanetHeight() / PLANET_DIVISOR / 2));
                shadowArc.y = (int) ((translateY + (p.getY()) * distanceRatio + bounds.height / 2) / scale - (p.getPlanetHeight() / PLANET_DIVISOR / 2));
                shadowArc.start = (int) (p.getPlanetDegrees() - 100);
                shadowArc.extent = (200);
                g2d.fill(shadowArc);

                //Draw name and background
                if (!p.getName().equals("")) {
                    g2d.setColor(Color.gray);

                    if (p.isHabitated()) {
                        g2d.setColor(Color.red);
                        g2d.fill(new Rectangle2D.Double(
                                (translateX + (p.getX()) * distanceRatio + bounds.width / 2) / scale - (g2d.getFontMetrics().stringWidth(p.getName()) + 3) / 2,
                                (translateY + (p.getY()) * distanceRatio + bounds.width / 2) / scale + (p.getPlanetSize() / PLANET_DIVISOR / 2),
                                (g2d.getFontMetrics().stringWidth(p.getName()) + 3), (g2d.getFontMetrics().getHeight()) + 3)
                        );
                        g2d.setColor(Color.white);
                    }

                    g2d.drawString(p.getName(),
                            (float) ((translateX + (p.getX()) * distanceRatio + bounds.width / 2) / scale) - (g2d.getFontMetrics().stringWidth(p.getName()) + 3) / 2,
                            (float) ((translateY + (p.getY()) * distanceRatio + bounds.width / 2) / scale) + (p.getPlanetSize() / PLANET_DIVISOR / 2) + g2d.getFontMetrics().getHeight());
                }
            }
        }

        //Interstellar boundary...
        long interstellardistance = sys.getPlanet(sys.getPlanetCount() - 1).getOrbitalDistance();
        interstellardistance += 10 * 10_000_000;
        Ellipse2D.Double boundaryCircle = new Ellipse2D.Double(
                (translateX + bounds.width / 2 - interstellardistance * distanceRatio) / scale,
                (translateY + bounds.height / 2 - interstellardistance * distanceRatio) / scale,
                (interstellardistance) * distanceRatio * 2 / scale,
                (interstellardistance) * distanceRatio * 2 / scale);
        g2d.setColor(new Color(0, 127, 255));
        g2d.draw(boundaryCircle);

        //draw spaceships
        for (SpaceShip ship : sys.spaceShips) {
            double x = (ship.getX());
            double y = (ship.getY());
            //Draw dot
            g2d.setColor(Color.yellow);
            g2d.fill(new Ellipse2D.Double((translateX + x * distanceRatio + bounds.width / 2) / scale - 5,
                    (translateY + y * distanceRatio + bounds.width / 2) / scale - 5, 10, 10));
            //Show actions
            double previousX = ship.getX();
            double previousY = ship.getY();
            for (ShipAction act : ship.commands) {
                if (act instanceof ShipMoveAction) {
                    //Draw movement line
                    ShipMoveAction move = (ShipMoveAction) act;
                    Line2D.Double line = new Line2D.Double(
                            (translateX + previousX * distanceRatio + bounds.width / 2) / scale,
                            (translateY + previousY * distanceRatio + bounds.width / 2) / scale,
                            (translateX + move.getPosition().getX() * distanceRatio + bounds.width / 2) / scale,
                            (translateY + move.getPosition().getY() * distanceRatio + bounds.width / 2) / scale);
                    g2d.setColor(Color.green);

                    g2d.draw(line);
                    previousX = move.getPosition().getX();
                    previousY = move.getPosition().getY();
                    //Get current location and draw

                } else if (act instanceof ToOrbitAction) {
                    ToOrbitAction move = (ToOrbitAction) act;
                    Line2D.Double line = new Line2D.Double(
                            (translateX + previousX * distanceRatio + bounds.width / 2) / scale,
                            (translateY + previousY * distanceRatio + bounds.width / 2) / scale,
                            (translateX + move.getPosition().getX() * distanceRatio + bounds.width / 2) / scale,
                            (translateY + move.getPosition().getY() * distanceRatio + bounds.width / 2) / scale);
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
            //Length in km
            double spaceLength = distance * scale * 10_000_000 / sizeofAU;//(previousY * sizeofAU) = px 

            //Get the halfway point to draw the text
            g2d.setColor(Color.blue);
            g2d.drawString(String.format("%.3f km, %.3f AU", spaceLength, spaceLength / 149598000), (measureStart.x + measureEnd.x) / 2 + 10, (measureStart.y + measureEnd.y) / 2 + 10);
            g2d.setColor(Color.orange);

            Line2D.Float measureLine = new Line2D.Float(measureStart, measureEnd);

            g2d.draw(measureLine);
        }

//        if (mousePosition != null) {
//            String positionText = String.format("Space Position: %.5f, %.5f, %d, %d",
//                    (mousePosition.x * scale - translateX - bounds.width / 2) / (distanceRatio),
//                    (mousePosition.y * scale - translateY - bounds.width / 2) / (distanceRatio), mousePosition.x, mousePosition.y);
//            g2d.drawString(positionText,
//                    mousePosition.x - g.getFontMetrics().stringWidth(positionText), mousePosition.y - 10);
//        }
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

        //FPS
        long current = System.currentTimeMillis();

        g2d.drawString(String.format("%d", 1000 / (current - fpsCounter)) + " fps", 10, 40);
        fpsCounter = current;

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

    public void setMousePosition(Point pos) {
        mousePosition = pos;
    }

    public void setWindowSize(Dimension windowSize) {
        this.windowSize = windowSize;
    }

    private RendererMath.Point calculatePoint(double smajor, double theta, double e, double rot) {
        //Eccentrcity
        double r = (smajor * (1 - e * e)) / (1 - e * Math.cos(theta - rot));
        return RendererMath.polarCoordToCartesianCoord((long) r,
                theta, new RendererMath.Point(0, 0), 1);
    }
}
