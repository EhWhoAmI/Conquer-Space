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
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.actions.InterstellarTravelAction;
import ConquerSpace.common.actions.ShipAction;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.civilization.vision.VisionTypes;
import ConquerSpace.common.game.ships.SpaceShip;
import ConquerSpace.common.game.universe.Orbit;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.StarSystem;
import ConquerSpace.common.game.universe.bodies.StarSystemBody;
import ConquerSpace.common.game.universe.bodies.StarType;
import ConquerSpace.common.util.Utilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author EhWhoAmI
 */
public class UniverseRenderer {

    public double sizeOfLTYR = 1;
    private Dimension bounds;

    Galaxy universe;
    public int universeDrawnDiameter;
    private Civilization civViewer;

    private final long KM_IN_LTYR = 9460730472580800L;

    GameState gameState;

    private double scale;
    private double translateX;
    private double translateY;

    private int starSystemFocused = 0;
    private int lastStarSystemFocused = -1;

    private int[] starX;
    private int[] starY;
    private int[] starRadius;

    private final int planetDrawnDiameter = 10;
    private final int sizeOfStar = 10;

    private final int starCount = 1000;

    BufferedImage[] systemTerrain;

    public UniverseRenderer(GameState gameState, Dimension bounds, Civilization c) {
        this.gameState = gameState;
        this.bounds = bounds;
        this.universe = gameState.getUniverse();
        this.civViewer = c;
        long universeRadius = 0;
        //Check for size of universe
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            StarSystem s = universe.getStarSystemObject(i);
            if (s.getOrbit().getSemiMajorAxis() > universeRadius) {
                universeRadius = (long) s.getOrbit().getSemiMajorAxis();
            }
        }

        systemTerrain = null;

        universeRadius++;
        //universeDimensionsLTYR = universeRadius;
        universeDrawnDiameter = (bounds.height > bounds.width) ? bounds.height : bounds.width;

        //Do fancy math to calculate the size of 1 light year. Divide the universe drawn size with universe details' diameter
        //Multiply by 2 because it is a radius
        sizeOfLTYR = ((double) universeDrawnDiameter / (double) (universeRadius * 2));

        starX = new int[starCount];
        starY = new int[starCount];
        starRadius = new int[starCount];
        Random rand = new Random(gameState.getSeed());
        for (int i = 0; i < starCount; i++) {
            starX[i] = (rand.nextInt(3000));
            starY[i] = (rand.nextInt(3000));
            starRadius[i] = (rand.nextInt(3));
        }
    }

    public void drawUniverse(Graphics g, double translateX, double translateY, double scale) {
        this.scale = scale;
        this.translateX = translateX;
        this.translateY = translateY;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setBackground(Color.BLACK);

        int imageX = (int) ((translateX) * 0.020);
        int imageY = (int) ((translateY) * 0.020);
        for (int i = 0; i < starCount; i++) {
            Ellipse2D.Double star = new Ellipse2D.Double(starX[i] + imageX, starY[i] + imageY, starRadius[i], starRadius[i]);
            g2d.setColor(Color.white);
            g2d.fill(star);
        }

        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            StarSystem sys = universe.getStarSystemObject(i);

            int x = (int) convertPointToScreen(sys.getX(), translateX, scale);
            int y = (int) convertPointToScreen(sys.getY(), translateY, scale);
            Point systemPosition = new Point(x, y);
            Rectangle r = g.getClipBounds();

            //Occulsion culling
            if (!r.contains(systemPosition)) {
                continue;
            }

            drawStarSystemDot(g2d, sys, systemPosition);

            //Draw system id, later do names when we get to it
            g2d.setColor(Color.white);
            g2d.drawString(Integer.toString(sys.getIndex()), (float) x - sizeOfStar,
                    (float) (y + sizeOfStar * 1.5));

            starSystemFocused = i;
        }

        //Check if the zoom is really high, then do stuff I guess
        if (drawingStarSystemDetails(scale)) {
            //Get star system focused on
            StarSystem sys = universe.getStarSystemObject(starSystemFocused);

            if (lastStarSystemFocused != starSystemFocused) {
                lastStarSystemFocused = starSystemFocused;
                //Create new thread to render stuff
                createPlanetSurfaces(sys);
            }
            drawStarSystemDetails(g2d, sys);

        }

        //Spaceships
        drawSpaceShips(g2d);

        //Draw scale line
        //30 light years
        int scaleLength = 30;
        //Line2D.Double line = new Line2D.Double(10, 20, (sizeOfLTYR * scaleLength * KM_IN_LTYR / scale + 10), 20);
        Line2D.Double line = new Line2D.Double(10, 20, (sizeOfLTYR * 100 / scale + 10), 20);
        g2d.setColor(Color.YELLOW);
        g2d.draw(line);
        g2d.drawString(String.format("%d light years", scaleLength), 10, 10);
        g2d.drawString("Translate " + translateX + " " + translateY, 10, 35);
        g2d.drawString("Scale " + scale, 10, 50);

    }

    private void drawStarSystemDot(Graphics2D g2d, StarSystem sys, Point systemPosition) {
        // Check vision
        if (civViewer.getVision().get(sys.getUniversePath()) > VisionTypes.UNDISCOVERED) {
            drawControl(g2d, sys, systemPosition);

            //Set star color
            Color c = getStarColor();
            g2d.setColor(c);

            Ellipse2D.Double systemEllipse = new Ellipse2D.Double(
                    systemPosition.getX() - sizeOfStar / 2,
                    systemPosition.getY() - sizeOfStar / 2,
                    sizeOfStar, sizeOfStar);
            g2d.fill(systemEllipse);
        } else {
            //Not seen, so gray color
            g2d.setColor(Color.GRAY);
            Ellipse2D.Double systemEllipse = new Ellipse2D.Double(
                    systemPosition.getX() - sizeOfStar / 2,
                    systemPosition.getY() - sizeOfStar / 2,
                    sizeOfStar, sizeOfStar);
            g2d.fill(systemEllipse);
        }
    }

    private void drawControl(Graphics2D g2d, StarSystem system, Point systemPosition) {
        if (universe.control.get(system.getReference()) != ObjectReference.INVALID_REFERENCE) {
            switch (civViewer.getVision().get(system.getUniversePath())) {
                case VisionTypes.EXISTS:
                    g2d.setColor(Color.gray);
                    break;
                default:
                    ObjectReference id = universe.control.get(system.getReference());
                    Civilization civ = gameState.getObject(id, Civilization.class);
                    if (civ != null) {
                        g2d.setColor(civ.getColor());
                    }
                    break;
            }

            Ellipse2D.Double control = new Ellipse2D.Double(
                    systemPosition.getX()
                    - (sizeOfStar + 10) / 2,
                    systemPosition.getY()
                    - (sizeOfStar + 10) / 2,
                    (sizeOfStar + 10),
                    (sizeOfStar + 10));

            g2d.fill(control);
        }
    }

    private Color getStarColor() {
        Color c;
        switch (StarType.TYPE_G) {
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
                break;
        }

        return c;
    }

    private void drawSpaceShips(Graphics2D g2d) {
        for (ObjectReference shipId : universe.spaceShips) {
            SpaceShip ship = gameState.getObject(shipId, SpaceShip.class);
            double x = (ship.getX());
            double y = (ship.getY());

            //Draw dot
            g2d.setColor(Color.yellow);
            Ellipse2D.Double shipShape = new Ellipse2D.Double(
                    convertPointToScreen(x, translateX, scale),
                    convertPointToScreen(y, translateY, scale),
                    sizeOfStar, sizeOfStar);
            g2d.fill(shipShape);

            //Show actions
            double previousX = ship.getX();
            double previousY = ship.getY();
            for (ShipAction act : ship.commands) {
                if (act instanceof InterstellarTravelAction) {
                    //Draw line
                    InterstellarTravelAction move = (InterstellarTravelAction) act;
                    Line2D.Double line = new Line2D.Double(
                            convertPointToScreen(previousX, translateX, scale),
                            convertPointToScreen(previousY, translateY, scale),
                            convertPointToScreen(move.getPositionX(), translateX, scale),
                            convertPointToScreen(move.getPositionY(), translateY, scale));
                    g2d.setColor(Color.green);

                    g2d.draw(line);
                    previousX = move.getPositionX();
                    previousY = move.getPositionY();
                }
            }
        }
    }

    private RendererMath.Point calculatePoint(double smajor, double theta, double e, double rot) {
        double r = (smajor * (1 - e * e)) / (1 - e * Math.cos(Math.toRadians(theta - rot)));
        return RendererMath.polarCoordToCartesianCoord((long) r,
                theta, new RendererMath.Point(0, 0), 1);
    }

    private GeneralPath createGeneralPath(Orbit orbit, StarSystem system, double accuracy) {
        GeneralPath circlePath = new GeneralPath();

        //Do the same thing that we do for calculating position
        RendererMath.Point ppt = calculatePoint(orbit.getSemiMajorAxis(), 0, orbit.getEccentricity(), orbit.getRotation());

        double cx = (ppt.x + system.getX());
        double cy = (ppt.y + system.getY());
        circlePath.moveTo(convertPointToScreen(cx, translateX, scale), convertPointToScreen(cy, translateY, scale));

        //So far, the accuracy does not make a large difference in terms of performance. 
        //But is signifant enough to be noticable. 
        //Circle loop
        {
            for (double k = 0; k <= 360; k += accuracy) {
                ppt = calculatePoint(orbit.getSemiMajorAxis(), k, orbit.getEccentricity(), orbit.getRotation());
                cx = (ppt.x + system.getX());
                cy = (ppt.y + system.getY());

                double paintX = convertPointToScreen(cx, translateX, scale);
                double paintY = convertPointToScreen(cy, translateY, scale);
                circlePath.lineTo(paintX, paintY);
            }
        }
        circlePath.closePath();

        return circlePath;
    }

    /**
     * Draws star system orbits and planets
     *
     * @param g2d
     * @param sys
     */
    private void drawStarSystemDetails(Graphics2D g2d, StarSystem sys) {
        long boundary = (long) (sys.getBoundary() * 1.1);
        int starSystemX = (int) convertPointToScreen(sys.getX(), translateX, scale);
        int starSystemY = (int) convertPointToScreen(sys.getY(), translateY, scale);
        Ellipse2D.Double systemEllipse = new Ellipse2D.Double(
                starSystemX - boundary * sizeOfLTYR / (scale),
                starSystemY - boundary * sizeOfLTYR / (scale),
                boundary * 2 * sizeOfLTYR / scale, boundary * 2 * sizeOfLTYR / scale);
        g2d.setColor(new Color(0, 127, 255));
        g2d.draw(systemEllipse);

        for (int j = 0; j < sys.getBodyCount(); j++) {
            StarSystemBody body = sys.getBodyObject(j);
            //Move it so that it's in the center..
            GeneralPath orbitPath = createGeneralPath(body.getOrbit(), sys, 0.1);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.draw(orbitPath);

            //Draw planet
            double bodyX = body.getX();
            double bodyY = body.getY();

            int planetX = (int) convertPointToScreen(bodyX, translateX, scale);
            int planetY = (int) convertPointToScreen(bodyY, translateY, scale);

            Ellipse2D.Double bodyEllipse = new Ellipse2D.Double(
                    planetX - planetDrawnDiameter / 2,
                    planetY - planetDrawnDiameter / 2,
                    planetDrawnDiameter, planetDrawnDiameter
            );

            g2d.setColor(Color.orange);
            g2d.fill(bodyEllipse);

            //If planet radius is larger than the circle, draw the planet's true size
            double planetRealSizeDiameter = (body.getDiameter() * sizeOfLTYR) / scale;

            Ellipse2D.Double ell = new Ellipse2D.Double(
                    planetX - planetRealSizeDiameter / 2,
                    planetY - planetRealSizeDiameter / 2,
                    planetRealSizeDiameter, planetRealSizeDiameter
            );

            if (systemTerrain != null && systemTerrain[j] != null
                    && g2d.getClipBounds().intersects(ell.getBounds())
                    && planetDrawnDiameter < planetRealSizeDiameter) {
                //Draw
                //Resize image so it fits
                if ((int) ((body.getDiameter() * sizeOfLTYR) / scale) > 0) {
                    g2d.drawImage(Utilities.resizeImage(systemTerrain[j], (int) planetRealSizeDiameter, (int) planetRealSizeDiameter),
                            (int) (planetX - planetRealSizeDiameter / 2),
                            (int) (planetY - planetRealSizeDiameter / 2), null);

                    //Umbra
                    drawUmbra(g2d, planetX, planetY, body.getDegrees(), planetRealSizeDiameter);
                }
            }
            //Draw planet name
            if (body instanceof Planet) {
                //If planet small, then draw it right below
                String planetName = ((Planet) body).getName();
                int stringWidth = g2d.getFontMetrics().stringWidth(planetName);

                g2d.setColor(Color.WHITE);
                if (planetDrawnDiameter > planetRealSizeDiameter) {
                    g2d.drawString(planetName, planetX - stringWidth / 2,
                            planetY + 20);
                } else {
                    //Draw it under the large planet size
                    g2d.drawString(planetName, planetX - stringWidth / 2,
                            (int) (planetY + planetRealSizeDiameter / 2 + g2d.getFontMetrics().getHeight()));
                }
            }
        }
    }

    private void drawUmbra(Graphics2D g2d, int planetX, int planetY, double degrees, double diameter) {
        g2d.setColor(Color.BLACK);
        Arc2D.Float shadowArc = new Arc2D.Float(Arc2D.CHORD);
        shadowArc.width = shadowArc.height = (float) diameter;
        shadowArc.x = (int) (planetX - diameter / 2);
        shadowArc.y = (int) (planetY - diameter / 2);
        shadowArc.start = (int) (270 - degrees);
        shadowArc.extent = (180);
        g2d.fill(shadowArc);
    }

    private void createPlanetSurfaces(StarSystem sys) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                systemTerrain = new BufferedImage[sys.getBodyCount()];
                //Render terrain
                for (int i = 0; i < sys.getBodyCount(); i++) {
                    StarSystemBody body = sys.getBodyObject(i);
                    if (body instanceof Planet) {
                        Planet planet = (Planet) body;
                        //render terrain
                        TerrainRenderer tr = new TerrainRenderer(planet);
                        systemTerrain[i] = Utilities.toBufferedImage(tr.getSquareImage(1));

                        int centerX = systemTerrain[i].getWidth() / 2;
                        int centerY = systemTerrain[i].getHeight() / 2;
                        int radius = systemTerrain[i].getHeight() / 2;

                        for (int x = 0; x < systemTerrain[i].getWidth(); x++) {
                            for (int y = 0; y < systemTerrain[i].getHeight(); y++) {
                                //Limit within circle
                                if (Math.sqrt((centerY - y) * (centerY - y) + (centerX - x) * (centerX - x)) > radius) {
                                    systemTerrain[i].setRGB(x, y, 0);
                                }
                            }
                        }
                    }
                }
            }
        };

        Thread runner = new Thread(r);
        runner.start();
    }

    public int getStarSystemFocused() {
        return starSystemFocused;
    }

    public void setStarSystemFocused(int starSystemFocused) {
        this.starSystemFocused = starSystemFocused;
    }

    public double convertPointToScreen(double position, double translate, double scale) {
        return (position * sizeOfLTYR + translate) / scale;
    }

    public boolean drawingStarSystemDetails(double scale) {
        return sizeOfLTYR * KM_IN_LTYR / scale > universeDrawnDiameter;
    }

    public int getPlanetDrawnDiameter() {
        return planetDrawnDiameter;
    }

    public int getSizeOfStar() {
        return sizeOfStar;
    }
}
