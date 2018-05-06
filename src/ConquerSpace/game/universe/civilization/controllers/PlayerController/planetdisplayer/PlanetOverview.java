package ConquerSpace.game.universe.civilization.controllers.PlayerController.planetdisplayer;

import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.BuildingBuilding;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;
import ConquerSpace.game.universe.spaceObjects.pSectors.RawResource;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

    private class PlanetSectorDisplayer extends JPanel {

        private PlanetSector[] sectors;
        private int times;

        public PlanetSectorDisplayer(Planet p) {
            sectors = p.planetSectors;
            times = (int) Math.sqrt(sectors.length);
            setPreferredSize(new Dimension(times * 7, times * 7));
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
                    Rectangle2D.Float rect = new Rectangle2D.Float(7*i, 7*n, 7, 7);
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
    }
}
