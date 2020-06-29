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

import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.city.City;
import ConquerSpace.game.city.CityType;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.game.resources.Stratum;
import ConquerSpace.gui.GraphicsUtil;
import ConquerSpace.util.ResourceLoader;
import ConquerSpace.util.Utilities;
import ConquerSpace.util.logging.CQSPLogger;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
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

    private Planet p;
    private Civilization c;
    private Universe u;

    private double tileSize = 8;
    private double scale = 0.5;
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
    private Image planetMap;

    public PlanetMap(Planet p, Civilization c, Universe u, PlanetInfoSheet parent, Image planetMap) {
        this.p = p;
        this.c = c;
        this.u = u;
        this.planetMap = planetMap;

        this.parent = parent;

        setLayout(new BorderLayout());
        //Render map
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        mapWidth = planetMap.getWidth(null);
        mapHeight = planetMap.getHeight(null);

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

        buildMenuButton = new JCheckBoxMenuItem("Create New City");
        //viewMenuButtonGroup.add(buildMenuButton);
        buildMenuButton.addActionListener(viewActionListener);
        viewMenu.add(buildMenuButton);

        viewMenu.addSeparator();
        resetViewButton = new JMenuItem("Reset View");
        resetViewButton.addActionListener(l -> {
            scale = 0.5;
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

        scale = 0.5;
    }

    public void resetBuildingIndicator() {
        map.currentlyBuildingPoint = null;
        map.constructionActive = false;
        displayedView = NORMAL_VIEW;
        buildingViewButton.setSelected(true);
        showResourceButton.setSelected(false);
        buildMenuButton.setSelected(false);
        setCursor(Cursor.getDefaultCursor());
    }

    private class MapPanel extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

        private BufferedImage resourceImage = null;
        private BufferedImage cityMapImage = null;
        private Image planetSurfaceMap = null;

        private HashMap<String, Image[]> districtImages;

        //Pre save images...
        boolean needRefresh = false;

        GeographicPoint currentlyBuildingPoint;
        boolean constructionActive = false;

        int mouseX = 0;
        int mouseY = 0;

        public MapPanel() {
            setBackground(Color.white);

            addMouseWheelListener(this);
            addMouseMotionListener(this);
            addMouseListener(this);

            //Enlarge map
            //Because the pixel size is 2, we need to multiply the size by 4
            int enlargementSize = 2;
            planetSurfaceMap = planetMap.getScaledInstance(planetMap.getWidth(null) * enlargementSize, planetMap.getHeight(null) * enlargementSize, Image.SCALE_DEFAULT);

            //Read images
            //TODO: change where we read this to the start of the game.
            districtImages = new HashMap<>();
            File imagesDir = ResourceLoader.getResourceByFile("dirs.map.images");
            File[] dirs = imagesDir.listFiles();
            if (dirs != null) {
                for (File dirImage : dirs) {
                    ArrayList<Image> imageList = new ArrayList<>();
                    for (File imageFile : dirImage.listFiles()) {
                        try {
                            Image img = ImageIO.read(imageFile);
                            imageList.add(img);
                        } catch (IOException ex) {
                        }
                    }
                    districtImages.put(dirImage.getName(), Arrays.copyOf(imageList.toArray(), imageList.size(), Image[].class));
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            //Turn on AA.
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            g2d.scale(1 / scale, 1 / scale);
            g2d.translate(translateX, translateY);

            //Terrain
            if (showTerrain && planetSurfaceMap != null) {
                g2d.drawImage(planetSurfaceMap, 0, 0, null);
            }

            if (displayedView == RESOURCE_VIEW || displayedView == BOTH_VIEW || displayedView == CONSTRUCTION_VIEW) {
                //Show resources
                if (resourceImage == null || needRefresh) {
                    resourceImage = new BufferedImage((int) (p.getPlanetWidth() * tileSize), (int) (p.getPlanetHeight() * tileSize), BufferedImage.TYPE_INT_ARGB);

                    Graphics2D resourceGraphics = resourceImage.createGraphics();
                    //resourceGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.325f));

                    for (int i = 0; i < p.strata.size(); i++) {
                        Stratum v = p.strata.get(i);
                        //Draw stratum
                        Ellipse2D.Double circe = new Ellipse2D.Double((v.getX() - v.getRadius()) * tileSize,
                                (v.getY() - v.getRadius()) * tileSize,
                                v.getRadius() * 2 * tileSize,
                                v.getRadius() * 2 * tileSize);
                        resourceGraphics.setColor(new Color(Color.ORANGE.getRed(), Color.ORANGE.getBlue(), Color.ORANGE.getGreen(), 100));
                        resourceGraphics.fill(circe);
                    }
                }
                if (resourceImage != null) {
                    g2d.drawImage(resourceImage, 0, 0, null);
                }
                for (int i = 0; i < p.strata.size(); i++) {
                    Stratum v = p.strata.get(i);
                    float fontSize = 35;
                    Font derivedFont = getFont().deriveFont(fontSize);
                    int width = getFontMetrics(derivedFont).stringWidth(v.getName());
                    //Draw circle
                    GraphicsUtil.paintTextWithOutline(v.getName(), g2d, fontSize, v.getX() * tileSize - width / 2, v.getY() * tileSize - getFontMetrics(derivedFont).getHeight() / 2);
                }
            }
            //Normal view 
            if (displayedView == NORMAL_VIEW || displayedView == CONSTRUCTION_VIEW || displayedView == BOTH_VIEW) {
                //Draw buildings
                if (cityMapImage == null || needRefresh || true) {
                    //g2d
                    Graphics2D mapGraphics = g2d;

                    Iterator<GeographicPoint> distIterator = p.cityDistributions.keySet().iterator();

                    //for (int i = 0; i < size(); i++) {
                    Rectangle r = g.getClipBounds();
                    while (distIterator.hasNext()) {

                        GeographicPoint point = distIterator.next();
                        City c = p.cityDistributions.get(point);
                        int xPos = (int) (point.getX() * tileSize);
                        int yPos = (int) (point.getY() * tileSize);
                        if (r.contains(new Point(xPos, yPos))) {
                            //Draw city
                            Rectangle2D.Double rect = new Rectangle2D.Double(xPos, yPos, tileSize, tileSize);

                            //Draw tile color
                            mapGraphics.setColor(CityType.getDistrictColor(c.getCityType()));
                            mapGraphics.fill(rect);

                            //Draw image
                            Image[] list = districtImages.get(c.getCityType().name());
                            if (list != null) {
                                int listSize = list.length;
                                //Id helps make sure that image is the same
                                Image im = list[point.hashCode() % listSize];
                                mapGraphics.drawImage(im, (xPos), (yPos), null);
                            }
                            //Draw city name

                            //Draw background
                            mapGraphics.setColor(Color.black);

                            //Get font stats
                            float fontSize = 12;
                            Font derivedFont = getFont().deriveFont(fontSize);
                            int width = getFontMetrics(derivedFont).stringWidth(c.getName());

                            //Draw fancy text
                            GraphicsUtil.paintTextWithOutline(c.getName(), g, fontSize, xPos - width / 2, yPos + getFontMetrics(derivedFont).getHeight() / 2);
                        }
                    }

                    if (constructionActive) {
                        //Draw it
                        Rectangle2D.Double buildingPointOutside = new Rectangle2D.Double((currentlyBuildingPoint.getX() - 1) * tileSize, (currentlyBuildingPoint.getY() - 1) * tileSize, tileSize * 3, tileSize * 3);
                        mapGraphics.setColor(Color.RED);
                        mapGraphics.fill(buildingPointOutside);
                        Rectangle2D.Double buildingPointInside = new Rectangle2D.Double((currentlyBuildingPoint.getX()) * tileSize, (currentlyBuildingPoint.getY()) * tileSize, tileSize, tileSize);
                        mapGraphics.setColor(Color.BLUE);
                        mapGraphics.fill(buildingPointInside);
                    }
                }
                //Draw
                g2d.drawImage(cityMapImage, 0, 0, null);
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
                //Limit scroll amount

                double newTranslateX = translateX - (startPoint.x - e.getX()) * scale;
                double newTranslateY = translateY - (startPoint.y - e.getY()) * scale;

                if (planetSurfaceMap != null) {
                    int width = planetSurfaceMap.getWidth(null);
                    int height = planetSurfaceMap.getHeight(null);
                    if (newTranslateX > (width * 0.02 * scale)) {
                        newTranslateX = width * 0.02 * scale;
                    }

                    double translateLimit = -width;

                    if (newTranslateX < translateLimit) {
                    newTranslateX = translateLimit;
                    }
                    
                    if (newTranslateY > (height * 0.05 * scale)) {
                        newTranslateY = height * 0.05 * scale;
                    }
                    
                    //Some ratio to limit the thing
                    translateLimit = -height;
                    if (newTranslateY < translateLimit) {
                        newTranslateY = translateLimit;
                    }
                }
                translateX = newTranslateX;
                translateY = newTranslateY;
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

                City b = p.cityDistributions.get(new GeographicPoint(mapX, mapY));
                if (b != null) {
                    showBuildingInfo(b);
                }
            } else if (SwingUtilities.isRightMouseButton(e) && displayedView == CONSTRUCTION_VIEW) {
                //Make the things
                //Create construction panel
                //Check if point is within bounds
                if (mouseX > 0 && mouseY > 0 && mouseX < (p.getPlanetSize() * 2) && mouseY < p.getPlanetSize() && !constructionActive) {
                    GeographicPoint pt = new GeographicPoint(mouseX, mouseY);
                    currentlyBuildingPoint = pt;
                    constructionActive = true;

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
            double scrollBefore = scale;
            double newScale = (Math.exp(scroll * 0.01) * scale);

            //Limit scale
            if (newScale > 0.1 && newScale < 2d) {
                //Change scale
                //double scaleChanged = newScale - scrollBefore;
                scale = newScale;

                double msX = (e.getX() * scale);
                double msY = (e.getY() * scale);
                double scaleChanged = scale - scrollBefore;

                translateX += (msX * scaleChanged) / scale;
                translateY += (msY * scaleChanged) / scale;
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
                City b = p.cityDistributions.get(new GeographicPoint(mapX, mapY));
                if (b != null) {
                    String cityName = "No City";
                    cityName = b.getName();
                    String text = ("<html>&nbsp;&nbsp;&nbsp;City: " + cityName + "<br/>Type: " + b.getCityType() + "<br/>Position: " + mapX + ", " + mapY + "<br/></html>");

                    toolTip.setTipText(text);
                    popup = popupFactory.getPopup(this, toolTip, e.getXOnScreen(), e.getYOnScreen());
                    popup.show();
                }
            } else if (displayedView == RESOURCE_VIEW || displayedView == CONSTRUCTION_VIEW) {
                //Check if lying on strata
                ArrayList<Stratum> strataToShow = new ArrayList<>();
                for (int i = 0; i < p.strata.size(); i++) {
                    Stratum stratum = p.strata.get(i);
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

        public void showBuildingInfo(City city) {
            //Get building type, determine what menu to show...
            if (city != null) {
                parent.population.showCity(city);
                hideToolTip();

                //Switch tabs
                parent.tpane.setSelectedComponent(parent.population);
            }
        }

        private Point convertPoint(int x, int y) {
            return new Point((int) (((x * scale - translateX)) / tileSize),
                    (int) (((y * scale - translateY)) / tileSize));
        }
    }
}
