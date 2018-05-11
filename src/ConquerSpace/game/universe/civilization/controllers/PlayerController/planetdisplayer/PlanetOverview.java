package ConquerSpace.game.universe.civilization.controllers.PlayerController.planetdisplayer;

import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;
import ConquerSpace.game.universe.spaceObjects.pSectors.RawResource;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

/**
 * Display sectors and stuff.
 *
 * @author Zyun
 */
public class PlanetOverview extends JPanel {

    private JPanel planetOverview;
    private JPanel planetSectors;
    private JLabel planetName;
    private JLabel planetPath;

    public PlanetOverview(Planet p) {
        setLayout(new GridLayout(1, 2));
        
        planetOverview = new JPanel();
        //If name is nothing, then call it unnamed planet
        planetName = new JLabel();
        planetPath = new JLabel();

        //Init planetname
        if (p.getName() == "") {
            planetName.setText("Unnamed Planet");
        } else {
            planetName.setText(p.getName());
        }

        //Init planetPath
        StringBuilder name = new StringBuilder();
        name.append("Sector ");
        name.append("" + p.getParentSector());
        name.append(" Star System ");
        name.append("" + p.getParentStarSystem());
        planetPath.setText(name.toString());

        planetSectors = new JPanel();
        PlanetSectorDisplayer sectorDisplayer = new PlanetSectorDisplayer(p);
        JPanel wrapper = new JPanel();
        wrapper.add(sectorDisplayer);
        JScrollPane sectorsScrollPane = new JScrollPane(wrapper);
        planetSectors.add(sectorsScrollPane);
        
        //Add components
        planetOverview.add(planetName);
        planetOverview.add(planetPath);
        
        add(planetOverview);
        add(planetSectors);
        
    }

    private class PlanetSectorDisplayer extends JPanel implements MouseListener{

        private PlanetSector[] sectors;
        private int times;
        private JPopupMenu menu;
        public PlanetSectorDisplayer(Planet p) {
            sectors = p.planetSectors;
            times = (int) Math.sqrt(sectors.length);
            setPreferredSize(new Dimension(times * 7 + 2, times * 7 + 2));
            menu = new JPopupMenu();
            addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            //The thingy has to be a square number
            //Times to draw the thingy
            int count = 0;
            for (int i = 0; i < times; i++) {
                for (int n = 0; n < times; n++) {
                    //Draw box
                    Rectangle2D.Float rect = new Rectangle2D.Float(7*n, 7*i, 7, 7);
                    //Draw the boxes.
                    //Get type of sectors
                    if (sectors[count] instanceof RawResource) {
                        g2d.setColor(Color.GREEN);
                    } else if (sectors[count] instanceof PopulationStorage) {
                        g2d.setColor(Color.blue);
                    }
                    
                    g2d.fill(rect);
                    if (sectors[count].getOwner() <= 0) {
                        g2d.setColor(Color.black);
                    } else {
                        g2d.setColor(Color.red);
                    }
                    
                    g2d.draw(rect);
                    count ++;
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON3) {
                menu.removeAll();
                
                int width = e.getX()/7;
                int height = e.getY()/7;
                
                int index = height*times + width;
                menu.add("Planet sector id " + (index + 1));
                //Add other options, like build, get info... short info, etc...
                JMenuItem infoItem = new JMenuItem("Info");
                infoItem.addActionListener((l) -> {
                    JFrame info = new JFrame("Planet sector " + (index + 1));
                    info.add(sectors[index].getInfoPanel());
                    info.pack();
                    info.setLocation(200, 100);
                    info.setVisible(true);
                });
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
