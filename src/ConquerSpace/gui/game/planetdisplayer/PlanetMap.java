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
import ConquerSpace.game.GameController;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.BuildingBuilding;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.resources.Stratum;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.gui.renderers.TerrainRenderer;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetMap extends JPanel {

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

    private ButtonGroup resourceButtonGroup;
    private JMenu showResourceMenu;
    private JRadioButton[] resourceList;

    private JMenu viewMenu;

    private ButtonGroup viewMenuButtonGroup;
    private JRadioButton normalViewButton;
    private JRadioButton buildMenuButton;
    private JRadioButton showResourceButton;

    private JMenuItem resetViewButton;
    private JMenuItem hideTerrainButton;

    private final int NORMAL_VIEW = 0;
    private final int RESOURCE_VIEW = 1;
    private final int BUILDING_VIEW = 2;
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

        viewMenuButtonGroup = new ButtonGroup();
        viewMenu = new JMenu("View");

        ActionListener viewActionListener = (e) -> {
            //Get button pressed..
            if (e.getSource().equals(normalViewButton)) {
                displayedView = NORMAL_VIEW;
            } else if (e.getSource().equals(showResourceButton)) {
                displayedView = RESOURCE_VIEW;
            } else if (e.getSource().equals(buildMenuButton)) {
                displayedView = BUILDING_VIEW;
                //Show building menu
            }

            map.repaint();
        };

        normalViewButton = new JRadioButton("Normal View", true);
        normalViewButton.addActionListener(viewActionListener);
        viewMenuButtonGroup.add(normalViewButton);
        viewMenu.add(normalViewButton);

        showResourceButton = new JRadioButton("Resource View");
        viewMenuButtonGroup.add(showResourceButton);
        showResourceButton.addActionListener(viewActionListener);
        viewMenu.add(showResourceButton);

        buildMenuButton = new JRadioButton("Construction View");
        viewMenuButtonGroup.add(buildMenuButton);
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

        showResourceMenu = new JMenu("Resources");

        resourceButtonGroup = new ButtonGroup();
        //Add the list
        resourceList = new JRadioButton[GameController.resources.size() + 1];
        resourceList[0] = new JRadioButton("All resources", true);
        resourceList[0].addActionListener(l -> {
            resourceShown = -1;
            map.needRefresh = true;
        });

        resourceButtonGroup.add(resourceList[0]);
        showResourceMenu.add(resourceList[0]);

        for (int i = 1; i < GameController.resources.size() + 1; i++) {
            resourceList[i] = new JRadioButton(GameController.resources.get(i - 1).getName());

            int val = i - 1;
            resourceList[i].addActionListener(l -> {
                resourceShown = val;
                map.needRefresh = true;
            });
            resourceButtonGroup.add(resourceList[i]);
            showResourceMenu.add(resourceList[i]);
        }

        menuBar.add(viewMenu);
        menuBar.add(showResourceMenu);

        map = new MapPanel();

        add(menuBar, BorderLayout.NORTH);
        add(map, BorderLayout.CENTER);

    }

    public void resetBuildingIndicator() {
        map.currentlyBuildingPoint = null;
        map.isActive = false;
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

            //Normal view 
            if (displayedView == NORMAL_VIEW || displayedView == BUILDING_VIEW) {
                //Draw buildings
                for (Map.Entry<GeographicPoint, Building> en : p.buildings.entrySet()) {
                    GeographicPoint p = en.getKey();
                    Building Building = en.getValue();
                    //Draw
                    Rectangle2D.Double rect = new Rectangle2D.Double(p.getX() * tileSize, p.getY() * tileSize, tileSize, tileSize);
                    g2d.setColor(Building.getColor());
                    g2d.fill(rect);
                }
            } else if (displayedView == RESOURCE_VIEW) {
                //Show resources
                if (resourceImage == null || needRefresh) {
                    resourceImage = new BufferedImage((int) (p.getPlanetSize() * 2 * tileSize), (int) (p.getPlanetSize() * tileSize), BufferedImage.TYPE_INT_ARGB);

                    Graphics2D resourceGraphics = resourceImage.createGraphics();
                    resourceGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.325f));

                    for (Stratum v : p.strata) {
                        //Draw...
                        Ellipse2D.Double circe = new Ellipse2D.Double((v.getX() - v.getRadius()) * tileSize,
                                (v.getY() - v.getRadius()) * tileSize,
                                v.getRadius() * 2 * tileSize,
                                v.getRadius() * 2 * tileSize);
                        resourceGraphics.setColor(Color.GRAY);
                        resourceGraphics.fill(circe);
                    }
                }

                g2d.drawImage(resourceImage, 0, 0, null);
            }

            if (displayedView == BUILDING_VIEW) {
                //Building UI
                Rectangle2D.Double mouseBox = new Rectangle2D.Double(mouseX * tileSize, mouseY * tileSize, tileSize, tileSize);
                g2d.setColor(Color.PINK);
                g2d.fill(mouseBox);
                if (currentlyBuildingPoint != null) {
                    //Draw it
                    Rectangle2D.Double buildingPointOutside = new Rectangle2D.Double((currentlyBuildingPoint.getX() - 1) * tileSize, (currentlyBuildingPoint.getY() - 1) * tileSize, tileSize * 3, tileSize * 3);
                    g2d.setColor(Color.RED);
                    g2d.fill(buildingPointOutside);
                    Rectangle2D.Double buildingPointInside = new Rectangle2D.Double((currentlyBuildingPoint.getX()) * tileSize, (currentlyBuildingPoint.getY()) * tileSize, tileSize, tileSize);
                    g2d.setColor(Color.BLUE);
                    g2d.fill(buildingPointInside);
                }
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
            } else if (SwingUtilities.isRightMouseButton(e) && displayedView == BUILDING_VIEW) {
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

            Building b = p.buildings.get(new GeographicPoint(mapX, mapY));
            if (b != null) {
                String topText = "";
                if (b instanceof CityDistrict) {
                    topText = ((CityDistrict) b).getCity().getName();
                } else if (b instanceof BuildingBuilding) {
                    topText = "Building " + ((BuildingBuilding) b).getToBuild().getType() + ", with " + ((BuildingBuilding) b).getLength() + " left";
                }

                toolTip.setTipText(("<html>&nbsp;&nbsp;&nbsp;" + topText + "<br/>" + b.getType() + "<br/>" + mapX + ", " + mapY + "<br/></html>"));

                popup = popupFactory.getPopup(this, toolTip, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
            }
        }

        private void hideToolTip() {
            if (popup != null) {
                popup.hide();
            }
        }

        public void showBuildingInfo(Building building) {
            //Get building type, determine what to show...
            if (building instanceof CityDistrict) {
                parent.population.showCity(((CityDistrict) building).getCity());
                hideToolTip();

                //Switch tabs
                parent.tpane.setSelectedComponent(parent.population);
            } else if (building instanceof SpacePort) {
                //parent.spacePort;
                //Switch tabs
                parent.tpane.setSelectedComponent(parent.spacePort);
            }
        }

        private Point convertPoint(int x, int y) {
            return new Point((int) (((x - translateX) / scale) / tileSize),
                    (int) (((y - translateY) / scale) / tileSize));
        }
    }
}
