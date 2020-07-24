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
package ConquerSpace.client.gui.renderers;

import ConquerSpace.common.GameState;
import ConquerSpace.common.actions.ShipAction;
import ConquerSpace.common.actions.ShipMoveAction;
import ConquerSpace.common.actions.ToOrbitAction;
import ConquerSpace.common.game.ships.SpaceShip;
import ConquerSpace.common.game.universe.bodies.Body;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.Star;
import ConquerSpace.common.game.universe.bodies.StarSystem;
import ConquerSpace.common.util.Utilities;
import ConquerSpace.common.util.logging.CQSPLogger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Random;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class SystemRenderer {

    private static final Logger LOGGER = CQSPLogger.getLogger(SystemRenderer.class.getName());

    public static final int PLANET_DIVISOR = 27;
    private Dimension bounds;

    private Galaxy universe;
    private StarSystem sys;
    public int sizeofAU;

    private BufferedImage[] systemTerrain;

    double previousScale;
//Background skybox

    private Thread rendererThread;
    boolean processedRenders = false;
    double scaleSize = 20;

    private Point measureStart;
    private Point measureEnd;
    private boolean measuring = false;

    private final boolean xygrid = false;
    private final boolean debugMouse = false;

    private Point mousePosition;

    private Dimension windowSize = new Dimension(1000, 1000);

    private double distanceRatio = 1;

    private double smallestAccuracy = 5;

    long fpsCounter = System.currentTimeMillis();
    double previousFps = 1;
    double smoothing = 0.95; // larger=more smoothing

    private GameState gameState;

    private int[] starX;
    private int[] starY;
    private int[] starRadius;

    private final int starCount = 1000;

    public SystemRenderer(GameState gameState, StarSystem sys, Galaxy u, Dimension bounds) {
        this.gameState = gameState;
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

                int k = 0;

                //Render terrain
                for (int i = 0; i < sys.getBodyCount(); i++) {
                    Body body = sys.getBodyObject(i);
                    if (body instanceof Planet) {
                        Planet planet = (Planet) body;
                        //render terrain
                        TerrainRenderer tr = new TerrainRenderer(planet);
                        systemTerrain[k] = toBufferedImage(tr.getSquareImage(1));

                        //Create copy
                        int width = systemTerrain[k].getWidth() / PLANET_DIVISOR;
                        int height = systemTerrain[k].getHeight() / PLANET_DIVISOR;
                        BufferedImage temp = resize(systemTerrain[k], height, height);
                        systemTerrain[k] = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);

                        for (int x = 0; x < temp.getWidth(); x++) {
                            for (int y = 0; y < temp.getHeight(); y++) {
                                //Draw
                                int centerX = temp.getWidth() / 2;

                                int centerY = temp.getHeight() / 2;

                                //Limit within circle
                                if (Math.sqrt((centerY - y) * (centerY - y) + (centerX - x) * (centerX - x)) <= temp.getWidth() / 2) {
                                    systemTerrain[k].setRGB(x, y, temp.getRGB(x, y));
                                }
                            }
                        }
                        k++;
                    }
                }

                System.gc();
                long end = System.currentTimeMillis();
                LOGGER.info("Time to render system: " + (end - beginning) + " ms");
            }
        };

        rendererThread = new Thread(r);
        rendererThread.setName("system " + sys.getId() + " renderer");
        sizeofAU = 15;
        distanceRatio = sizeofAU / 10_000_000d;

        starX = new int[starCount];
        starY = new int[starCount];
        starRadius = new int[starCount];
        Random rand = new Random(sys.getIndex());
        for (int i = 0; i < starCount; i++) {
            starX[i] = (rand.nextInt(3000));
            starY[i] = (rand.nextInt(3000));
            starRadius[i] = (rand.nextInt(3));
        }
    }

    public void drawStarSystem(Graphics g, double translateX, double translateY, double scale) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        Rectangle boundsRectangle = g.getClipBounds();

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(Color.BLACK);
        g2d.draw(bg);

        //Draw stars
        int imageX = (int) ((translateX) * 0.020);
        int imageY = (int) ((translateY) * 0.020);
        for (int i = 0; i < starCount; i++) {
            if (boundsRectangle.contains(starX[i], starY[i])) {
                Ellipse2D.Double star = new Ellipse2D.Double(starX[i] + imageX, starY[i] + imageY, starRadius[i], starRadius[i]);
                g2d.setColor(Color.white);
                g2d.fill(star);
            }
        }
        //Render bg image
        //g2d.fill(bg);
        if (!processedRenders) {
            rendererThread.start();
            processedRenders = true;
        }

        //X Y grid for reference
        if (xygrid) {
            Line2D.Double xline = new Line2D.Double(
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
            g2d.draw(yline);
        }

        //Draw orbit lines first, so that the planets are on top of the ornit line
        for (int i = 0; i < sys.getBodyCount(); i++) {
            Body planet = sys.getBodyObject(i);
            //Change accuracy based on scale
            double accuracy = scale;
            if (accuracy < 0.1) {
                accuracy = 0.1;
            }
            if (accuracy > smallestAccuracy) {
                accuracy = smallestAccuracy;
            }
            //Need to do less calculations when not visible
            GeneralPath orbitPath = createGeneralPath(planet, boundsRectangle, scale, translateX, translateY, accuracy);
            g2d.setColor(Color.WHITE);
            g2d.draw(orbitPath);
        }

        int planetCount = 0;
        for (int i = 0; i < sys.getBodyCount(); i++) {
            Body body = sys.getBodyObject(i);
            //  (translate + position * distanceRatio + bounds.width / 2) / scale;
            // (translateX + body.get * distanceRatio + bounds.width / 2) / scale);
            int bodyXScreenX = (int) convertPointX(body.getX(), translateX, scale);
            int bodyYScreenY = (int) convertPointY(body.getY(), translateY, scale);

            //Draw terrain
            if (body instanceof Planet) {
                Planet p = (Planet) body;
                if ((bodyXScreenX > -20 && bodyXScreenX < (windowSize.getWidth() + 20)) && (bodyYScreenY < (windowSize.getHeight() + 20) && bodyYScreenY > -20)) {
                    double planetSize = p.getPlanetSize() * 200 * sizeofAU / (scale * 10_000_000);
                    if (boundsRectangle.contains(bodyXScreenX, bodyYScreenY)) {

                        if (systemTerrain[planetCount] != null) {
                            //Occlusion culling
                            g2d.drawImage(systemTerrain[planetCount],
                                    bodyXScreenX - (p.getPlanetHeight() / PLANET_DIVISOR / 2),
                                    bodyYScreenY - (p.getPlanetHeight() / PLANET_DIVISOR / 2), null);
                        }

                        //Draw real planet size
                        g2d.setColor(Color.cyan);
                        g2d.fill(new Ellipse2D.Double(
                                bodyXScreenX - (planetSize / 2),
                                bodyYScreenY - (planetSize / 2),
                                planetSize, planetSize
                        ));

                        //Draw shadow - change to gradient to make it look better...
                        g2d.setColor(Color.BLACK);
                        Arc2D.Float shadowArc = new Arc2D.Float(Arc2D.CHORD);
                        shadowArc.width = shadowArc.height = ((p.getPlanetHeight() * 1.1f) / PLANET_DIVISOR);
                        shadowArc.x = (int) (bodyXScreenX - (p.getPlanetHeight() / PLANET_DIVISOR / 2));
                        shadowArc.y = (int) (bodyYScreenY - (p.getPlanetHeight() / PLANET_DIVISOR / 2));
                        shadowArc.start = (int) (p.getDegrees() - 100);
                        shadowArc.extent = (200);
                        g2d.fill(shadowArc);

                        //Draw name and background
                        if (!p.getName().equals("")) {
                            g2d.setColor(Color.gray);

                            if (p.isHabitated()) {
                                g2d.setColor(Color.red);
                                g2d.fill(new Rectangle2D.Double(
                                        bodyXScreenX - (g2d.getFontMetrics().stringWidth(p.getName()) + 3) / 2,
                                        bodyYScreenY + (p.getPlanetSize() / PLANET_DIVISOR / 2),
                                        (g2d.getFontMetrics().stringWidth(p.getName()) + 3), (g2d.getFontMetrics().getHeight()) + 3)
                                );
                                g2d.setColor(Color.white);
                            }

                            g2d.drawString(p.getName(),
                                    (float) (bodyXScreenX - (g2d.getFontMetrics().stringWidth(p.getName()) + 3) / 2),
                                    (float) (bodyYScreenY + (p.getPlanetSize() / PLANET_DIVISOR / 2) + g2d.getFontMetrics().getHeight()));
                        }
                    }
                }
                planetCount++;
            } else if (body instanceof Star) {
                Star star = (Star) body;
                Ellipse2D.Double starCircle = new Ellipse2D.Double(
                        bodyXScreenX - star.starSize / 50000 / 2,
                        bodyYScreenY - star.starSize / 50000 / 2,
                        star.starSize / 50000, star.starSize / 50000);
                Color c;
                switch (star.type) {
                    case TYPE_A:
                        c = Color.decode("#D5E0FF");
                        break;
                    case TYPE_B:
                        c = Color.decode("#A2C0FF");
                        break;
                    case TYPE_O:
                        c = Color.decode("#92B5FF");
                        break;
                    case TYPE_F:
                        c = Color.decode("#F9F5FF");
                        break;
                    case TYPE_G:
                        c = Color.decode("#fff4ea");
                        break;
                    case TYPE_K:
                        c = Color.decode("#FFDAB5");
                        break;
                    case TYPE_M:
                        c = Color.decode("#FFB56C");
                        break;
                    default:
                        c = Color.BLACK;
                }
                g2d.setColor(c);
                g2d.fill(starCircle);
            }
        }

        //Interstellar boundary...
        /*long interstellardistance = sys.getPlanet(sys.getPlanetCount() - 1).();
        interstellardistance += 10 * 10_000_000;
        Ellipse2D.Double boundaryCircle = new Ellipse2D.Double(
                (translateX + bounds.width / 2 - interstellardistance * distanceRatio) / scale,
                (translateY + bounds.height / 2 - interstellardistance * distanceRatio) / scale,
                (interstellardistance) * distanceRatio * 2 / scale,
                (interstellardistance) * distanceRatio * 2 / scale);
        g2d.setColor(new Color(0, 127, 255));
        g2d.draw(boundaryCircle);*/
        //draw spaceships
        for (Integer id : sys.spaceShips) {
            SpaceShip ship = gameState.getObject(id, SpaceShip.class);

            double x = (ship.getX());
            double y = (ship.getY());
            //Draw dot
            g2d.setColor(Color.yellow);

            g2d.fill(new Ellipse2D.Double(convertPointX(x, translateX, scale) - 5,
                    convertPointY(y, translateY, scale) - 5, 10, 10));
            //Show actions

            double previousX = ship.getX();
            double previousY = ship.getY();

            double actionXStart = convertPointX(previousX, translateX, scale);
            double actionYStart = convertPointY(previousY, translateY, scale);

            for (ShipAction act : ship.commands) {
                if (act instanceof ShipMoveAction) {
                    //Draw movement line
                    ShipMoveAction move = (ShipMoveAction) act;
                    Line2D.Double line = new Line2D.Double(
                            actionXStart,
                            actionYStart,
                            convertPointX(move.getPosition().getX(), translateX, scale),
                            convertPointY(move.getPosition().getY(), translateY, scale));

                    g2d.setColor(Color.green);
                    g2d.draw(line);
                } else if (act instanceof ToOrbitAction) {
                    ToOrbitAction move = (ToOrbitAction) act;
                    Line2D.Double line = new Line2D.Double(
                            actionXStart,
                            actionYStart,
                            convertPointX(move.getPosition().getX(), translateX, scale),
                            convertPointY(move.getPosition().getY(), translateY, scale));

                    g2d.setColor(Color.cyan);
                    g2d.draw(line);
                }
            }
        }

        //Draw measurement line
        if (measuring) {
            //Draw the line

            //Get length of line to get the distance
            double distance = Utilities.distanceBetweenPoints(measureStart.x, measureStart.y, measureEnd.x, measureEnd.y);
            //Length in km
            double spaceLength = distance * scale * 10_000_000 / sizeofAU;//(previousY * sizeofAU) = px 

            //Get the halfway point to draw the text
            g2d.setColor(Color.blue);
            g2d.drawString(String.format("%.3f km, %.3f AU", spaceLength, spaceLength / 149598000), (measureStart.x + measureEnd.x) / 2 + 10, (measureStart.y + measureEnd.y) / 2 + 10);
            g2d.setColor(Color.orange);

            Line2D.Float measureLine = new Line2D.Float(measureStart, measureEnd);

            g2d.draw(measureLine);
        }

        //Debug mouse info
        if (debugMouse) {
            if (mousePosition != null) {
                String positionText = String.format("Space Position: %.5f, %.5f, %d, %d",
                        (mousePosition.x * scale - translateX - bounds.width / 2) / (distanceRatio),
                        (mousePosition.y * scale - translateY - bounds.width / 2) / (distanceRatio), mousePosition.x, mousePosition.y);
                g2d.drawString(positionText,
                        mousePosition.x - g.getFontMetrics().stringWidth(positionText), mousePosition.y - 10);
            }
        }
        //Draw scale line
        //Limit size of scale...
        for (int i = 0;
                i < 20; i++) {
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

        if (fpsCounter == 0) {
            fpsCounter = 1;
        }
        //Ensure no / by 0 error
        long diff = (current - fpsCounter);
        if (diff == 0) {
            diff = 1;
        }

        double currentFps = 1000d / diff;

        previousFps = (previousFps * smoothing) + (currentFps * (1.0 - smoothing));

        g2d.drawString(String.format("%d", (int) previousFps) + " fps", 10, 40);
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

    public void setMousePosition(Point pos) {
        mousePosition = pos;
    }

    public void setWindowSize(Dimension windowSize) {
        this.windowSize = windowSize;
    }

    private RendererMath.Point calculatePoint(double smajor, double theta, double e, double rot) {
        double r = (smajor * (1 - e * e)) / (1 - e * Math.cos(Math.toRadians(theta - rot)));
        return RendererMath.polarCoordToCartesianCoord((long) r,
                theta, new RendererMath.Point(0, 0), 1);
    }

    private GeneralPath createGeneralPath(Body planet, Rectangle drawingBounds, double scale, double translateX, double translateY, double accuracy) {
        GeneralPath circlePath = new GeneralPath();

        //Do the same thing that we do for calculating position
        RendererMath.Point ppt = calculatePoint(planet.getSemiMajorAxis(), 0, planet.getEccentricity(), planet.getRotation());

        double cx = (ppt.x * distanceRatio);
        double cy = (ppt.y * distanceRatio);
        circlePath.moveTo((translateX + bounds.width / 2 + cx) / scale, (translateY + bounds.width / 2 - cy) / scale);

        //So far, the accuracy does not make a large difference in terms of performance. 
        //But is signifant enough to be noticable. 
        //Circle loop
        {
            for (double k = 0; k <= 360; k += accuracy) {
                ppt = calculatePoint(planet.getSemiMajorAxis(), k, planet.getEccentricity(), planet.getRotation());
                cx = (ppt.x * distanceRatio);
                cy = (ppt.y * distanceRatio);

                double paintX = (translateX + bounds.width / 2 + cx) / scale;
                double paintY = (translateY + bounds.width / 2 - cy) / scale;

                circlePath.lineTo(paintX, paintY);
            }
        }
        circlePath.closePath();

        return circlePath;
    }

    private int pointQuardrant(long x, long y) {
        y = (y >>> 63L);
        return (int) (((x >>> 63L) ^ y) + y + y + 1L);
    }

    private double convertPointX(double position, double translate, double scale) {
        return (translate + position * distanceRatio + bounds.width / 2) / scale;
    }

    private double convertPointY(double position, double translate, double scale) {
        return (translate - position * distanceRatio + bounds.width / 2) / scale;
    }
}
