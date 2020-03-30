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
package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.gui.game.planetdisplayer.construction.ConstructionPanel;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.buildings.ResourceStorage;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.Stratum;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.gui.renderers.TerrainRenderer;
import ConquerSpace.util.Utilities;
import ConquerSpace.util.logging.CQSPLogger;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetMap extends JPanel {

    private static final Logger LOGGER = CQSPLogger.getLogger(PlanetMap.class.getName());

    private PlanetInfoSheet parent;
    private Image mapImage;

    private Planet p;
    private Civilization c;
    private Universe u;

    private double tileSize = 2;
    private double scale = 1;
    private double translateX = 0;
    private double translateY = 0;
    private int mapWidth;
    private int mapHeight;
    private Point startPoint = new Point();
    private boolean isDragging = false;

    private PopupFactory popupFactory = PopupFactory.getSharedInstance();
    private Popup popup;
    private JToolTip toolTip = this.createToolTip();

    MapPanel map;

    private JMenuBar menuBar;

    private JMenu viewMenu;

    private JCheckBoxMenuItem buildingViewButton;
    private JCheckBoxMenuItem buildMenuButton;
    private JCheckBoxMenuItem showResourceButton;
    private JMenuItem showOnlyResources;
    private JMenuItem showOnlyBuildings;

    private JMenuItem resetViewButton;
    private JMenuItem hideTerrainButton;

    private final int NORMAL_VIEW = 0;
    private final int RESOURCE_VIEW = 1;
    private final int BOTH_VIEW = 2;
    private final int CONSTRUCTION_VIEW = 3;
    private final int NONE_VIEW = -1;

    //if -1, then all resources
    private int resourceShown = -1;
    private int displayedView = NORMAL_VIEW;
    private boolean showTerrain = true;

    public PlanetMap(Planet p, Civilization c, Universe u, PlanetInfoSheet parent) {
        this.p = p;
        this.c = c;
        this.u = u;
        this.parent = parent;

        setLayout(new BorderLayout());
        //Render map
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        TerrainRenderer renderer = new TerrainRenderer(p);
        //Base on height
        //Get window size...
        mapImage = renderer.getImage(tileSize);

        mapWidth = mapImage.getWidth(null);
        mapHeight = mapImage.getHeight(null);

        menuBar = new JMenuBar();

        viewMenu = new JMenu("View");

        ActionListener viewActionListener = (e) -> {
            //Get button pressed..
            setCursor(Cursor.getDefaultCursor());
            buildMenuButton.isSelected();
            if (e.getSource().equals(buildingViewButton) || e.getSource().equals(showResourceButton)) {
                buildMenuButton.setSelected(false);
            } else if (e.getSource().equals(buildMenuButton)) {
                displayedView = CONSTRUCTION_VIEW;
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                //Deselect everything else
                buildingViewButton.setSelected(false);
                showResourceButton.setSelected(false);
            } else if (e.getSource().equals(showOnlyResources)) {
                buildingViewButton.setSelected(false);
                showResourceButton.setSelected(true);
                displayedView = RESOURCE_VIEW;
            } else if (e.getSource().equals(showOnlyBuildings)) {
                buildingViewButton.setSelected(true);
                showResourceButton.setSelected(false);
                displayedView = NORMAL_VIEW;
            }

            if (buildingViewButton.isSelected() && showResourceButton.isSelected()) {
                displayedView = BOTH_VIEW;
            } else if (buildingViewButton.isSelected()) {
                displayedView = NORMAL_VIEW;
            } else if (showResourceButton.isSelected()) {
                displayedView = RESOURCE_VIEW;
            } else if (!buildMenuButton.isSelected()) {
                //Just show nothing
                displayedView = NONE_VIEW;
            }

            map.repaint();
        };

        buildingViewButton = new JCheckBoxMenuItem("Building View", true);
        buildingViewButton.addActionListener(viewActionListener);
        //viewMenuButtonGroup.add(normalViewButton);
        viewMenu.add(buildingViewButton);

        showResourceButton = new JCheckBoxMenuItem("Resource View");
        //viewMenuButtonGroup.add(showResourceButton);
        showResourceButton.addActionListener(viewActionListener);
        viewMenu.add(showResourceButton);

        viewMenu.addSeparator();

        showOnlyBuildings = new JMenuItem("Only show buildings");
        showOnlyBuildings.addActionListener(viewActionListener);
        viewMenu.add(showOnlyBuildings);

        showOnlyResources = new JMenuItem("Only show resources");
        showOnlyResources.addActionListener(viewActionListener);
        viewMenu.add(showOnlyResources);

        viewMenu.addSeparator();

        buildMenuButton = new JCheckBoxMenuItem("Construction View");
        //viewMenuButtonGroup.add(buildMenuButton);
        buildMenuButton.addActionListener(viewActionListener);
        viewMenu.add(buildMenuButton);

        viewMenu.addSeparator();
        resetViewButton = new JMenuItem("Reset View");
        resetViewButton.addActionListener(l -> {
            scale = 1;
            translateX = 0;
            translateY = 0;
            map.repaint();
        });

        viewMenu.add(resetViewButton);

        hideTerrainButton = new JMenuItem("Show/Hide terrain");
        hideTerrainButton.addActionListener(l -> {
            showTerrain = !showTerrain;
        });
        viewMenu.add(hideTerrainButton);

        menuBar.add(viewMenu);

        map = new MapPanel();

        add(menuBar, BorderLayout.NORTH);
        add(map, BorderLayout.CENTER);

        scale = 3;
    }

    public void resetBuildingIndicator() {
        map.currentlyBuildingPoint = null;
        map.isActive = false;
        displayedView = NORMAL_VIEW;
        buildingViewButton.setSelected(true);
        showResourceButton.setSelected(false);
        buildMenuButton.setSelected(false);
        setCursor(Cursor.getDefaultCursor());
    }

    private class MapPanel extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

        private BufferedImage resourceImage = null;
        boolean needRefresh = false;

        GeographicPoint currentlyBuildingPoint;
        boolean isActive = false;

        int mouseX = 0;
        int mouseY = 0;

        public MapPanel() {
            setBackground(Color.white);

            addMouseWheelListener(this);
            addMouseMotionListener(this);
            addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(translateX, translateY);
            g2d.scale(scale, scale);

            //Terrain
            if (showTerrain) {
                g2d.drawImage(mapImage, 0, 0, null);
            }

            if (displayedView == RESOURCE_VIEW || displayedView == BOTH_VIEW || displayedView == CONSTRUCTION_VIEW) {
                //Show resources
                if (resourceImage == null || needRefresh) {
                    resourceImage = new BufferedImage((int) (p.getPlanetWidth() * tileSize), (int) (p.getPlanetHeight() * tileSize), BufferedImage.TYPE_INT_ARGB);

                    Graphics2D resourceGraphics = resourceImage.createGraphics();
                    resourceGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.325f));

                    for (Stratum v : p.strata) {
                        //Draw...
                        Ellipse2D.Double circe = new Ellipse2D.Double((v.getX() - v.getRadius()) * tileSize,
                                (v.getY() - v.getRadius()) * tileSize,
                                v.getRadius() * 2 * tileSize,
                                v.getRadius() * 2 * tileSize);
                        resourceGraphics.setColor(Color.ORANGE);
                        resourceGraphics.fill(circe);
                    }
                }

                g2d.drawImage(resourceImage, 0, 0, null);
            }
            //Normal view 
            if (displayedView == NORMAL_VIEW || displayedView == CONSTRUCTION_VIEW || displayedView == BOTH_VIEW) {
                //Draw buildings
                for (Map.Entry<GeographicPoint, Building> en : p.buildings.entrySet()) {
                    GeographicPoint p = en.getKey();
                    Building Building = en.getValue();
                    //Draw
                    Rectangle2D.Double rect = new Rectangle2D.Double(p.getX() * tileSize, p.getY() * tileSize, tileSize, tileSize);
                    g2d.setColor(Building.getColor());
                    g2d.fill(rect);
                }

                if (isActive) {
                    //Draw it
                    Rectangle2D.Double buildingPointOutside = new Rectangle2D.Double((currentlyBuildingPoint.getX() - 1) * tileSize, (currentlyBuildingPoint.getY() - 1) * tileSize, tileSize * 3, tileSize * 3);
                    g2d.setColor(Color.RED);
                    g2d.fill(buildingPointOutside);
                    Rectangle2D.Double buildingPointInside = new Rectangle2D.Double((currentlyBuildingPoint.getX()) * tileSize, (currentlyBuildingPoint.getY()) * tileSize, tileSize, tileSize);
                    g2d.setColor(Color.BLUE);
                    g2d.fill(buildingPointInside);
                }
            }

            if (displayedView == CONSTRUCTION_VIEW) {
                //Building UI
                Rectangle2D.Double mouseBox = new Rectangle2D.Double(mouseX * tileSize, mouseY * tileSize, tileSize, tileSize);
                g2d.setColor(Color.PINK);
                g2d.fill(mouseBox);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                //Move it
                //Check if still in view
                translateX -= ((startPoint.x - e.getX()));
                translateY -= ((startPoint.y - e.getY()));
                startPoint = e.getPoint();
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //Track mouse position
            Point pt = convertPoint(e.getX(), e.getY());
            mouseX = pt.x;
            mouseY = pt.y;

            hideToolTip();
            showToolTip(e);
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int x = e.getX();
                int y = e.getY();

                Point pt = convertPoint(x, y);
                int mapX = pt.x;
                int mapY = pt.y;

                Building b = p.buildings.get(new GeographicPoint(mapX, mapY));
                if (b != null) {
                    showBuildingInfo(b);
                }
            } else if (SwingUtilities.isRightMouseButton(e) && displayedView == CONSTRUCTION_VIEW) {
                //Make the things
                //Create construction panel
                //Check if point is within bounds
                if (mouseX > 0 && mouseY > 0 && mouseX < (p.getPlanetSize() * 2) && mouseY < p.getPlanetSize() && !isActive) {
                    GeographicPoint pt = new GeographicPoint(mouseX, mouseY);
                    currentlyBuildingPoint = pt;
                    isActive = true;
                    ConstructionPanel construction = new ConstructionPanel(c, p, u, pt, PlanetMap.this);
                    ((JDesktopPane) SwingUtilities.getAncestorOfClass(JDesktopPane.class, this)).add(construction);

                    construction.toFront();

                    try {
                        construction.setSelected(true);
                    } catch (PropertyVetoException ex) {
                        //Ignore, because it doesn't need to show anyway.
                        LOGGER.trace("Property veto exception for showing construction window", ex);
                    }

                    //Switch to normal view
                    displayedView = NORMAL_VIEW;
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                //Start dragging
                startPoint = e.getPoint();
                isDragging = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                //End Dragging
                isDragging = false;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            showToolTip(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            hideToolTip();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            //Scroll in
            double scroll = (double) e.getUnitsToScroll();
            double newScale = (Math.exp(scroll * 0.01) * scale);
            //Limit scale
            if (newScale > 0.05) {
                scale = newScale;
            }
            //Now repaint
            repaint();
        }

        private void showToolTip(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            Point pt = convertPoint(x, y);
            int mapX = pt.x;
            int mapY = pt.y;

            if (displayedView == NORMAL_VIEW || displayedView == BOTH_VIEW) {
                Building b = p.buildings.get(new GeographicPoint(mapX, mapY));
                if (b != null) {
                    toolTip.setTipText(("<html>&nbsp;&nbsp;&nbsp;" + b.getTooltipText() + "<br/>" + b.getType() + "<br/>" + mapX + ", " + mapY + "<br/></html>"));

                    popup = popupFactory.getPopup(this, toolTip, e.getXOnScreen(), e.getYOnScreen());
                    popup.show();
                }
            } else if (displayedView == RESOURCE_VIEW || displayedView == CONSTRUCTION_VIEW) {
                //Check if lying on strata
                ArrayList<Stratum> strataToShow = new ArrayList<>();
                for (Stratum stratum : p.strata) {
                    if (inCircle(stratum.getX(), stratum.getY(), stratum.getRadius(), mapX, mapY)) {
                        strataToShow.add(stratum);
                    }
                }

                if (!strataToShow.isEmpty()) {
                    toolTip.setTipText("<html>&nbsp;Layers: " + strataToShow.toString());
                    popup = popupFactory.getPopup(this, toolTip, e.getXOnScreen(), e.getYOnScreen());
                    popup.show();
                }
            }
        }

        private boolean inCircle(int xC, int yC, int r, int x, int y) {
            return (Utilities.distanceBetweenPoints(xC, yC, x, y) <= r);
        }

        private void hideToolTip() {
            if (popup != null) {
                popup.hide();
            }
        }

        public void showBuildingInfo(Building building) {
            //Get building type, determine what menu to show...
            if (building instanceof CityDistrict) {
                parent.population.showCity(((CityDistrict) building).getCity());
                hideToolTip();

                //Switch tabs
                parent.tpane.setSelectedComponent(parent.population);
            } else if (building instanceof SpacePort) {
                //parent.spacePort;
                //Switch tabs
                parent.tpane.setSelectedComponent(parent.spacePort);
            } else if (building instanceof ResourceStorage) {

            }
        }

        private Point convertPoint(int x, int y) {
            return new Point((int) (((x - translateX) / scale) / tileSize),
                    (int) (((y - translateY) / scale) / tileSize));
        }
    }
}
