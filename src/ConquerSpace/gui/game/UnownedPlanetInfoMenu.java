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
package ConquerSpace.gui.game;

import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.city.City;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.PolarCoordinate;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.PlanetTypes;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.game.resources.Stratum;
import ConquerSpace.gui.game.planetdisplayer.AtmosphereInfo;
import ConquerSpace.gui.renderers.TerrainRenderer;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

/**
 * Window for the planet that is unowned, but surveyed
 *
 * @author EhWhoAmI
 */
public class UnownedPlanetInfoMenu extends JPanel {

    private static final int TILE_SIZE = 7;
    private JPanel planetOverview;
    private JPanel planetSectors;
    //private JList<Orbitable> satelliteList;
    private JLabel planetName;
    private JLabel planetPath;
    private JLabel planetType;
    private JLabel ownerLabel;
    private JLabel orbitDistance;
    private JLabel disclaimerLabel;
    private Planet p;
    //private ButtonGroup resourceButtonGroup;
    //private JRadioButton[] showResources;

    private JTabbedPane infoPane;

    private AtmosphereInfo atmosphereInfo;
    private NumberFormat numberFormatter;

    public UnownedPlanetInfoMenu(Universe u, Planet p, Civilization c) {
        this.p = p;
        infoPane = new JTabbedPane();

        JPanel planetOverviewPanel = new JPanel();

        planetOverviewPanel.setLayout(new GridLayout(2, 1));
        numberFormatter = NumberFormat.getInstance();

        planetOverview = new JPanel();
        planetOverview.setLayout(new VerticalFlowLayout(5, 3));
        planetOverview.setBorder(new TitledBorder("Planet Info"));
        //If name is nothing, then call it unnamed planet
        planetName = new JLabel();
        planetPath = new JLabel();
        planetType = new JLabel("Planet type: " + p.getPlanetType());
        ownerLabel = new JLabel();
        PolarCoordinate pos = p.orbit.toPolarCoordinate();
        orbitDistance = new JLabel("Distance: " + numberFormatter.format(pos.getDistance()) + " km, " + numberFormatter.format((double) pos.getDistance() / 149598000d) + " AU");

        //Init planetname
        if (p.getName().equals("")) {
            planetName.setText("Unnamed Planet");
        } else {
            planetName.setText(p.getName());
        }

        //Init planetPath
        StringBuilder name = new StringBuilder();
        name.append("Star System ");
        name.append("" + p.getParentStarSystem());
        name.append(" Planet id " + p.getId());
        planetPath.setText(name.toString());

        //Init owner
        if (p.getOwnerID() > -1) {
            ownerLabel.setText("Owner: " + c.getName());
        } else {
            ownerLabel.setText("No owner");
        }

        planetSectors = new JPanel();
        PlanetSectorDisplayer sectorDisplayer = new PlanetSectorDisplayer(p, c);
        JPanel wrapper = new JPanel();
        wrapper.add(sectorDisplayer);
        JTabbedPane buildingPanel = new JTabbedPane();

        JPanel buttonsWrapper = new JPanel();

        buildingPanel.add("Map", buttonsWrapper);

        buildingPanel.addChangeListener(a -> {
            if (buildingPanel.getSelectedIndex() == 1) {
                sectorDisplayer.whatToShow = PlanetSectorDisplayer.SHOW_ALL_RESOURCES;
            } else if (buildingPanel.getSelectedIndex() == 0) {
                sectorDisplayer.whatToShow = PlanetSectorDisplayer.PLANET_BUILDINGS;
            }
        });
        JScrollPane sectorsScrollPane = new JScrollPane(wrapper);
        planetSectors.add(sectorsScrollPane);
        //Add components
        planetOverview.add(planetName);
        planetOverview.add(planetPath);
        planetOverview.add(planetType);
        planetOverview.add(ownerLabel);
        planetOverview.add(orbitDistance);

        planetOverviewPanel.add(planetOverview);
        planetOverviewPanel.add(planetSectors);
        infoPane.add("Planet Overview", planetOverviewPanel);

        //Add atmosphere info
        atmosphereInfo = new AtmosphereInfo(p, c);
        infoPane.add("Atmosphere Info", atmosphereInfo);
        add(infoPane);
        //Add empty panel
        //add(new JPanel());
    }

    private class PlanetSectorDisplayer extends JPanel implements MouseListener {

        private final int SHOW_ALL = -1;
        int resourceToShow = SHOW_ALL;

        private int whatToShow = PLANET_BUILDINGS;
        static final int PLANET_BUILDINGS = 0;
        static final int PLANET_RESOURCES = 1;
        static final int SHOW_ALL_RESOURCES = 2;
        private JPopupMenu menu;
        private Civilization c;
        private Color color;
        private Point point;
        private Image img;
        private Point lastClicked;
        private TerrainRenderer renderer;

        public PlanetSectorDisplayer(Planet p, Civilization c) {
            double scale = 2;

            this.c = c;
            if (p.getPlanetType() == PlanetTypes.GAS) {
                scale = .5;
            }
            setPreferredSize(
                    new Dimension((int) (p.getPlanetSize() * 2 * scale), (int) (p.getPlanetSize() * scale)));
            menu = new JPopupMenu();
            addMouseListener(this);
            renderer = new TerrainRenderer(p);

            img = renderer.getImage(scale);
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
            g2d.drawImage(img, 0, 0, null);

            if (whatToShow == PLANET_RESOURCES || whatToShow == SHOW_ALL_RESOURCES) {
                //Set opacity
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
                //Draw the circles
                for (Stratum v : p.strata) {
                    //Draw...
                    if (resourceToShow == SHOW_ALL) {
                        Ellipse2D.Float circe = new Ellipse2D.Float(v.getX() * 2, v.getY() * 2, v.getRadius() * 2, v.getRadius() * 2);
                        g2d.setColor(Color.GRAY);
                        g2d.fill(circe);
                    }
                }
            }
            if (whatToShow == PLANET_BUILDINGS || whatToShow == SHOW_ALL_RESOURCES) {
                //Draw buildings
                for (Map.Entry<GeographicPoint, City> en : p.cityDistributions.entrySet()) {
                    GeographicPoint p = en.getKey();
                    City Building = en.getValue();
                    //Draw
                    Rectangle2D.Float rect = new Rectangle2D.Float(p.getX() * 2, p.getY() * 2, 2, 2);
                    g2d.setColor(Color.red);
                    g2d.fill(rect);
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

        @Override
        public void mouseClicked(MouseEvent e) {
            lastClicked = e.getPoint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
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
}
