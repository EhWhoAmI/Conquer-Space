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
package ConquerSpace.client.gui.game.planetdisplayer;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.resources.Stratum;
import ConquerSpace.common.game.universe.GeographicPoint;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.PlanetTypes;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetMinimapViewer extends JPanel {

    private final int SHOW_ALL = -1;
    int resourceToShow = SHOW_ALL;

    private int whatToShow = PLANET_BUILDINGS;
    static final int PLANET_BUILDINGS = 0;
    static final int PLANET_RESOURCES = 1;
    static final int SHOW_ALL_RESOURCES = 2;
    private JPopupMenu menu;
    private Civilization civ;
    private Color color;
    private Point point;
    private Point lastClicked;
    private PlanetMapProvider planetMap;
    private GameState gameState;
    private Planet planet;

    public PlanetMinimapViewer(PlanetMapProvider planetMap, GameState gameState, Planet p, Civilization c) {
        this.gameState = gameState;
        this.planetMap = planetMap;
        double scale = 2;
        
        this.planet = p;

        this.civ = c;
        if (p.getPlanetType() == PlanetTypes.GAS) {
            scale = .5;
        }
        setPreferredSize(
                new Dimension((int) (p.getPlanetSize() * 2 * scale), (int) (p.getPlanetSize() * scale)));
        menu = new JPopupMenu();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //The thingy has to be a square number
        //Times to draw the thingy
        if (whatToShow == PLANET_RESOURCES) {
            //Set opacity
            //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        if (planetMap.isLoaded()) {
            g2d.drawImage(planetMap.getImage(), 0, 0, null);
        } else {
            g2d.drawString("Rendering Planet Map", 0, 0);
        }

        if (whatToShow == PLANET_RESOURCES || whatToShow == SHOW_ALL_RESOURCES) {
            //Set opacity
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
            //Draw the circles
            for (Integer strataId : planet.strata) {
                Stratum v = gameState.getObject(strataId, Stratum.class);
                //Draw...
                if (resourceToShow == SHOW_ALL) {
                    Ellipse2D.Float circe = new Ellipse2D.Float(v.getX() * 2, v.getY() * 2, v.getRadius() * 2, v.getRadius() * 2);
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.fill(circe);
                }
            }
        }
        if (whatToShow == PLANET_BUILDINGS || whatToShow == SHOW_ALL_RESOURCES) {
            //Draw buildings

            if (planet.cityDistributions != null) {
                for (Map.Entry<GeographicPoint, Integer> en : planet.cityDistributions.entrySet()) {
                    GeographicPoint point = en.getKey();

                    //Draw
                    Rectangle2D.Float rect = new Rectangle2D.Float(point.getX() * 2, point.getY() * 2, 2, 2);
                    g2d.setColor(Color.red);
                    g2d.fill(rect);
                }
            }
        }
        if (whatToShow == SHOW_ALL_RESOURCES && point != null && color != null) {
            //Show thingy
            //Surround with yellow marker
            Color invc = new Color(255 - color.getRed(),
                    255 - color.getGreen(),
                    255 - color.getBlue());
            Rectangle2D.Float bgRect = new Rectangle2D.Float((float) point.getX() * 2 - 2, (float) point.getY() * 2 - 2, 6, 6);
            g2d.setColor(invc);
            g2d.fill(bgRect);
            Rectangle2D.Float rect = new Rectangle2D.Float((float) point.getX() * 2, (float) point.getY() * 2, 2, 2);
            g2d.setColor(color);
            g2d.fill(rect);
        }
    }

    public void setWhatToShow(int what) {
        whatToShow = what;
        repaint();
    }

    public void setResourceViewing(int wat) {
        resourceToShow = wat;
    }

    public Point getLastClicked() {
        return lastClicked;
    }

    public void showLocation(Point pt, Color c) {
        point = pt;
        color = c;
    }
}
