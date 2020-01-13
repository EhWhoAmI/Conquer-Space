package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.GameController;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.spaceObjects.Planet;
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
 * @author zyunl
 */
public class PlanetMap extends JPanel {

    private PlanetInfoSheet parent;
    private Image mapImage;
    private Planet p;
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

    private JRadioButton showResourceButton;
    private JMenuItem resetViewButton;
    private JMenuItem hideTerrainButton;

    private final int NORMAL_VIEW = 0;
    private final int RESOURCE_VIEW = 1;
    //if -1, then all
    private int resourceShown = -1;
    private int displayedView = NORMAL_VIEW;
    private boolean showTerrain = true;

    public PlanetMap(Planet p, PlanetInfoSheet parent) {
        this.p = p;
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

    private class MapPanel extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

        private BufferedImage resourceImage = null;
        boolean needRefresh = false;

        public MapPanel() {
            setBackground(Color.black);
            
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
            if (displayedView == NORMAL_VIEW) {
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

                    for (ResourceVein v : p.resourceVeins) {
                        if (v.getResourceType().getId() == resourceShown || resourceShown == -1) {
                            //Draw...
                            Ellipse2D.Double circe = new Ellipse2D.Double((v.getX() - v.getRadius()) * tileSize,
                                    (v.getY() - v.getRadius()) * tileSize,
                                    v.getRadius() * 2 * tileSize,
                                    v.getRadius() * 2 * tileSize);
                            resourceGraphics.setColor(v.getResourceType().getColor());
                            resourceGraphics.fill(circe);
                        }
                    }
                }
                g2d.drawImage(resourceImage, 0, 0, null);
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
            }
        }

        private Point convertPoint(int x, int y) {
            return new Point((int) (((x - translateX) / scale) / tileSize),
                    (int) (((y - translateY) / scale) / tileSize));
        }
    }
}
