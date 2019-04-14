package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.Orbitable;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.SpaceShip;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.BuildingBuilding;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;
import ConquerSpace.game.universe.spaceObjects.pSectors.RawResource;
import ConquerSpace.gui.game.BuildPlanetSectorMenu;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
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
    private JPanel satellitesPanel;
    private JList<Orbitable> satelliteList;
    private JLabel planetName;
    private JLabel planetPath;
    private JLabel planetType;
    private JLabel ownerLabel;
    private JLabel orbitDistance;
    private Planet p;

    public PlanetOverview(Planet p, Civilization c) {
        this.p = p;
        setLayout(new GridLayout(1, 3));

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
        if (p.getName() == "") {
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
        JScrollPane sectorsScrollPane = new JScrollPane(wrapper);
        planetSectors.add(sectorsScrollPane);

        //Add components
        planetOverview.add(planetName);
        planetOverview.add(planetPath);
        planetOverview.add(planetType);
        planetOverview.add(ownerLabel);
        planetOverview.add(orbitDistance);

        satellitesPanel = new JPanel();
        satellitesPanel.setBorder(new TitledBorder("Satellites"));

        satelliteList = new JList<>(new Vector(p.getSatellites()));
        satellitesPanel.add(satelliteList);
        satelliteList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Orbitable orb = satelliteList.getSelectedValue();
                if(orb != null && orb instanceof SpaceShip) {
                    //Cast to space ship and show ship stats.
                    Ship ship = (Ship) orb;
                    //Show stats and stuff
                    //...
                }
            }
            
        });

        add(planetOverview);
        add(planetSectors);
        add(satellitesPanel);
        //Add empty panel
        add(new JPanel());
    }

    private class PlanetSectorDisplayer extends JPanel implements MouseListener {

        private PlanetSector[] sectors;
        private int times;
        private JPopupMenu menu;
        private Civilization c;

        public PlanetSectorDisplayer(Planet p, Civilization c) {
            this.c = c;
            sectors = p.planetSectors;
            times = (int) Math.sqrt(sectors.length);
            setPreferredSize(new Dimension(times * TILE_SIZE + 2, times * TILE_SIZE + 2));
            menu = new JPopupMenu();
            addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            //The thingy has to be a square number
            //Times to draw the thingy
            int count = 0;
            for (int w = 0; w < times; w++) {
                for (int h = 0; h < times; h++) {
                    //Draw box
                    Rectangle2D.Float rect = new Rectangle2D.Float(TILE_SIZE * h, TILE_SIZE * w, TILE_SIZE, TILE_SIZE);
                    //Draw the boxes.
                    //Get type of sectors
                    if (sectors[count] instanceof RawResource) {
                        g2d.setColor(Color.GREEN);
                    } else if (sectors[count] instanceof PopulationStorage) {
                        g2d.setColor(Color.blue);
                    } else if (sectors[count] instanceof BuildingBuilding) {
                        g2d.setColor(Color.yellow);
                    }

                    g2d.fill(rect);
                    g2d.setColor(Color.black);

                    g2d.draw(rect);
                    count++;
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                menu.removeAll();

                int width = e.getX() / TILE_SIZE;
                int height = e.getY() / TILE_SIZE;

                int index = height * times + width;
                menu.add("Planet sector id " + (index + 1));
                //Add other options, like build, get info... short info, etc...
                JMenuItem infoItem = new JMenuItem("Info");
                infoItem.addActionListener((l) -> {
                    JInternalFrame info = new JInternalFrame("Planet sector " + (index + 1));
                    info.add(sectors[index].getInfoPanel());
                    info.pack();
                    info.setLocation(200, 100);
                    info.setVisible(true);
                    info.setClosable(true);
                    info.setResizable(true);

                    //Hacky code to get parent internal frame.
                    Component c;
                    for (c = getParent(); !(c instanceof JInternalFrame) || c != null; c = c.getParent()) {
                        if (c instanceof JInternalFrame) {
                            break;
                        }
                    }
                    if (c != null) {
                        ((JInternalFrame) c).getDesktopPane().add(info);
                    }
                });

                if (p.getOwnerID() == 0 && p.planetSectors[index] instanceof RawResource) {
                    JMenuItem build = new JMenuItem("Build");
                    build.addActionListener((l) -> {
                        BuildPlanetSectorMenu sector = new BuildPlanetSectorMenu(p, index, c);
                        //sector.addWindowListener();
                        Component c;
                        for (c = getParent(); !(c instanceof JInternalFrame) || c != null; c = c.getParent()) {
                            if (c instanceof JInternalFrame) {
                                break;
                            }
                        }
                        if (c != null) {
                            ((JInternalFrame) c).getDesktopPane().add(sector);
                        }
                        sector.setVisible(true);
                    });
                    menu.add(build);
                }

                menu.add(infoItem);
                menu.show(this, e.getX(), e.getY());
            }
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
    }
}
