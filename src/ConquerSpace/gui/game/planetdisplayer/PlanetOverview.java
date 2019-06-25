package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.GameController;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Universe;
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
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

/**
 * Display sectors and stuff.
 *
 * @author Zyun
 */
public class PlanetOverview extends JPanel {

    private static final int TILE_SIZE = 7;
    private JPanel planetOverview;
    private JPanel planetSectors;
    //private JList<Orbitable> satelliteList;
    private JLabel planetName;
    private JLabel planetPath;
    private JLabel planetType;
    private JLabel ownerLabel;
    private JLabel orbitDistance;
    private Planet p;
    private JButton switchButton;
    private ButtonGroup resourceButtonGroup;
    private JRadioButton[] showResources;

    public PlanetOverview(Universe u, Planet p, Civilization c) {
        this.p = p;
        setLayout(new GridLayout(2, 1));

        planetOverview = new JPanel();
        planetOverview.setLayout(new VerticalFlowLayout(5, 3));
        planetOverview.setBorder(new TitledBorder("Planet Info"));
        //If name is nothing, then call it unnamed planet
        planetName = new JLabel();
        planetPath = new JLabel();
        planetType = new JLabel("Planet type: " + p.getPlanetType());
        ownerLabel = new JLabel();
        orbitDistance = new JLabel("Distance: " + p.getOrbitalDistance() + " km");

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
        switchButton = new JButton("Change view");
        switchButton.addActionListener(a -> {
            if (sectorDisplayer.whatToShow == 0) {
                sectorDisplayer.setWhatToShow(1);
            } else {
                sectorDisplayer.setWhatToShow(0);
            }
        });
        JPanel buttonGroupWrapper = new JPanel();
        buttonGroupWrapper.setLayout(new VerticalFlowLayout());
        resourceButtonGroup = new ButtonGroup();
        showResources = new JRadioButton[GameController.resources.size() + 1];
        showResources[0] = new JRadioButton("All Resources");
        showResources[0].addActionListener(a -> {
            sectorDisplayer.resourceToShow = -1;
        });
        buttonGroupWrapper.add(showResources[0]);
        resourceButtonGroup.add(showResources[0]);
        for (int i = 1; i < GameController.resources.size() + 1; i++) {
            showResources[i] = new JRadioButton(GameController.resources.get(i - 1).getName());
            int val = i - 1;
            showResources[i].addActionListener(a -> {
                sectorDisplayer.setResourceViewing(val);
            });
            resourceButtonGroup.add(showResources[i]);
            buttonGroupWrapper.add(showResources[i]);
        }

        buttonsWrapper.add(switchButton);
        buttonsWrapper.add(buttonGroupWrapper);
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
        planetSectors.add(buildingPanel);
        //Add components
        planetOverview.add(planetName);
        planetOverview.add(planetPath);
        planetOverview.add(planetType);
        planetOverview.add(ownerLabel);
        planetOverview.add(orbitDistance);

        add(planetOverview);
        add(planetSectors);
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
            this.c = c;
            setPreferredSize(
                    new Dimension(p.getPlanetSize() * 4, p.getPlanetSize() * 2));
            menu = new JPopupMenu();
            addMouseListener(this);
            renderer = new TerrainRenderer(p);
            img = renderer.getImage(2);

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
                for (ResourceVein v : p.resourceVeins) {
                    //Draw...
                    if (resourceToShow == SHOW_ALL || resourceToShow == v.getResourceType().getId()) {
                        Ellipse2D.Float circe = new Ellipse2D.Float(v.getX() * 2, v.getY() * 2, v.getRadius() * 2, v.getRadius() * 2);
                        g2d.setColor(v.getResourceType().getColor());
                        g2d.fill(circe);
                    }
                }
            }
            if (whatToShow == PLANET_BUILDINGS || whatToShow == SHOW_ALL_RESOURCES) {
                //Draw buildings
                for (Map.Entry<ConquerSpace.game.universe.Point, Building> en : p.buildings.entrySet()) {
                    ConquerSpace.game.universe.Point p = en.getKey();
                    Building Building = en.getValue();
                    //Draw
                    Rectangle2D.Float rect = new Rectangle2D.Float(p.getX() * 2, p.getY() * 2, 2, 2);
                    g2d.setColor(Building.getColor());
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
