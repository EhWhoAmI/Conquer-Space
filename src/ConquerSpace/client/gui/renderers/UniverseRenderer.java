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

import ConquerSpace.client.gui.game.GameWindow;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.actions.InterstellarTravelAction;
import ConquerSpace.common.actions.ShipAction;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.civilization.vision.VisionTypes;
import ConquerSpace.common.game.ships.SpaceShip;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.universe.bodies.StarSystem;
import ConquerSpace.common.game.universe.bodies.StarType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author EhWhoAmI
 */
public class UniverseRenderer {

    public double sizeOfLTYR = 1;
    private Dimension bounds;

    Galaxy universe;
    public int universeDrawnSize;
    private Civilization c;

    GameState gameState;

    public UniverseRenderer(GameState gameState, Dimension bounds, Civilization c) {
        this.gameState = gameState;
        this.bounds = bounds;
        this.universe = gameState.getUniverse();
        this.c = c;
        long universeRadius = 0;
        //Check for size of universe
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            StarSystem s = universe.getStarSystemObject(i);
            if (s.getOrbit().getSemiMajorAxis() > universeRadius) {
                universeRadius = (long) s.getOrbit().getSemiMajorAxis();
            }
        }

        universeRadius++;
        //universeDimensionsLTYR = universeRadius;
        universeDrawnSize = (bounds.height > bounds.width) ? bounds.height : bounds.width;

        //Do fancy math to calculate the size of 1 light year. Divide the universe drawn size with universe details' diameter
        //Multiply by 2 because it is a radius
        sizeOfLTYR = ((double) universeDrawnSize / (double) (universeRadius * 2));

        //Calculate size
    }

    public void drawUniverse(Graphics g, double translateX, double translateY, double scale) {
        //Paint bounds dark blue.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(new Color(0, 0, 255));
        g2d.fill(bg);

        Ellipse2D.Double universeCircle = new Ellipse2D.Double(translateX / scale, translateY / scale, universeDrawnSize / scale, universeDrawnSize / scale);
        g2d.setColor(Color.BLACK);
        g2d.fill(universeCircle);

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
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            //Check vision
            StarSystem sys = universe.getStarSystemObject(i);
            // Draw star systems   
            // Check vision...
            if (c.vision.get(sys.getUniversePath()) > VisionTypes.UNDISCOVERED) {
                //Control
                if (universe.control.get(sys.getReference()) != ObjectReference.INVALID_REFERENCE) {
                    switch (c.vision.
                            get(sys.getUniversePath())) {
                        case VisionTypes.EXISTS:
                            g2d.setColor(Color.gray);
                            break;
                        default:
                            ObjectReference id = universe.control.get(sys.getReference());
                            Civilization civ = gameState.getObject(id, Civilization.class);
                            if (civ != null) {
                                g2d.setColor(civ.getColor());
                            }
                    }
                    //Control, if any...
                    Ellipse2D.Double control = new Ellipse2D.Double(
                            convertPointToScreen(sys.getX(), translateX, scale) - GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2,
                            convertPointToScreen(sys.getY(), translateY, scale) - GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2,
                            (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10),
                            (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR + 10));

                    g2d.fill(control);
                }
                //End of control
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
                }

                g2d.setColor(c);

                Ellipse2D.Double system = new Ellipse2D.Double(
                        convertPointToScreen(sys.getX(), translateX, scale),
                        convertPointToScreen(sys.getY(), translateY, scale),
                        GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR, GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR);
                g2d.fill(system);
            } else {
                //Draw stars and stuff...
                g2d.setColor(Color.GRAY);
                Ellipse2D.Double system = new Ellipse2D.Double(
                        convertPointToScreen(sys.getX(), translateX, scale),
                        convertPointToScreen(sys.getY(), translateY, scale),
                        GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR, GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR);
                g2d.fill(system);
            }

            g2d.drawString(Integer.toString(sys.getIndex()), (float) convertPointToScreen(sys.getX(), translateX, scale),
                    (float) convertPointToScreen(sys.getY(), translateY, scale));
        }

        //Spaceships
        //draw spaceships
        for (ObjectReference shipId : universe.spaceShips) {
            SpaceShip ship = gameState.getObject(shipId, SpaceShip.class);
            double x = (ship.getX());
            double y = (ship.getY());
            //Draw dot
            g2d.setColor(Color.yellow);
            Ellipse2D.Double shipShape = new Ellipse2D.Double(
                    convertPointToScreen(x, translateX, scale),
                    convertPointToScreen(y, translateY, scale),
                    GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR, GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR);
            g2d.fill(shipShape);
            //Show actions
            double previousX = ship.getX();
            double previousY = ship.getY();
            for (ShipAction act : ship.commands) {
                if (act instanceof InterstellarTravelAction) {
                    //Draw line
                    InterstellarTravelAction move = (InterstellarTravelAction) act;
                    Line2D.Double line = new Line2D.Double(
                            (translateX + previousX * sizeOfLTYR + bounds.width / 2) / scale,
                            (translateY + previousY * sizeOfLTYR + bounds.width / 2) / scale,
                            (translateX + move.getPositionX() * sizeOfLTYR + bounds.width / 2) / scale,
                            (translateY + move.getPositionY() * sizeOfLTYR + bounds.width / 2) / scale);
                    g2d.setColor(Color.green);

                    g2d.draw(line);
                    previousX = move.getPositionX();
                    previousY = move.getPositionY();
                    //Get current location and draw

                }/* else if (act instanceof ToOrbitAction) {
                    ToOrbitAction move = (ToOrbitAction) act;
                    Line2D.Double line = new Line2D.Double(
                            (translateX + previousX * sizeOfLTYR / 10_000_000 + bounds.width / 2) / scale,
                            (translateY + previousY * sizeOfLTYR / 10_000_000 + bounds.width / 2) / scale,
                            (translateX + move.getPosition().getX() * sizeOfLTYR / 10_000_000 + bounds.width / 2) / scale,
                            (translateY + move.getPosition().getY() * sizeOfLTYR / 10_000_000 + bounds.width / 2) / scale);
                    g2d.setColor(Color.cyan);
                    g2d.draw(line);
                    previousX = move.getPosition().getX();
                    previousY = move.getPosition().getY();
                }
            }*/
            }
            //ship.getName();
        }
        //Draw scale line
        // TODO: MAKE ACCURATE
        Line2D.Double line = new Line2D.Double(10, 20, (sizeOfLTYR * 30 * 63241 / scale + 10), 20);

        g2d.setColor(Color.YELLOW);
        g2d.draw(line);
        g2d.drawString("30 light years", 10, 10);

        /*Ellipse2D.Double system = new Ellipse2D.Double(
                (-632410 * sizeOfLTYR + translateX + bounds.height / 2) / scale - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2),
                (632410 * sizeOfLTYR + translateY + bounds.height / 2) / scale - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2),
                GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR, GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR);
        g2d.fill(system);
        system = new Ellipse2D.Double(
                (632410 * sizeOfLTYR + translateX + bounds.height / 2) / scale - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2),
                (-632410 * sizeOfLTYR + translateY + bounds.height / 2) / scale - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2),
                GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR, GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR);
        g2d.setColor(Color.blue);

        g2d.fill(system);*/
    }

    private double convertPointToScreen(double position, double translate, double scale) {
        return (position * sizeOfLTYR + translate + bounds.height / 2) / scale - (GameWindow.CQSPDesktop.SIZE_OF_STAR_ON_SECTOR / 2);
    }
}
