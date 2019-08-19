package ConquerSpace.gui.game;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.Point;
import ConquerSpace.gui.game.planetdisplayer.SpacePortMenu;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.gui.game.planetdisplayer.AtmosphereInfo;
import ConquerSpace.gui.game.planetdisplayer.BuildingMenu;
import ConquerSpace.gui.game.planetdisplayer.PlanetIndustry;
import ConquerSpace.gui.game.planetdisplayer.PlanetOverview;
import ConquerSpace.gui.game.planetdisplayer.PlanetPopulation;
import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Zyun
 */
public class PlanetInfoSheet extends JPanel {

    private JTabbedPane tpane;
    private PlanetOverview overview;
    private PlanetPopulation population;
    private SpacePortMenu spacePort;
    private AtmosphereInfo atmosphere;
    private BuildingMenu building;
    private PlanetIndustry industry;
    
    private Civilization c;
    private Planet p;
    
    public PlanetInfoSheet(Universe u, Planet p, Civilization c) {
        this.c = c;
        this.p = p;
        setLayout(new BorderLayout());
        tpane = new JTabbedPane();
        overview = new PlanetOverview(u, p, c);
        atmosphere = new AtmosphereInfo(p, c);
        population = new PlanetPopulation(p, 0);
        spacePort = new SpacePortMenu(p, c);
        building = new BuildingMenu(u, p, c);
        industry = new PlanetIndustry();

        tpane.add("Overview", overview);
        //tpane.add("Resources", new PlanetResources(p));
        tpane.add("Population", population);
        tpane.add("Space Port", spacePort);
        tpane.add("Atmosphere", atmosphere);
        tpane.add("Building", building);
        tpane.add("Industry", industry);

        tpane.setEnabledAt(2, false);
        //Check if planet contains space port
        for (Map.Entry<Point, Building> entry : p.buildings.entrySet()) {
            Point key = entry.getKey();
            Building value = entry.getValue();

            //Do stuff...
            if (value instanceof SpacePort) {
                tpane.setEnabledAt(2, true);
                break;
            }
        }
        if (c.values.get("haslaunch") != 1) {
            tpane.setEnabledAt(2, false);
        }

        add(tpane, BorderLayout.CENTER);
    }

    public void init() {

    }

    public void update() {
        spacePort.update();
        
        tpane.setEnabledAt(2, false);
        //Check if planet contains space port
        for (Map.Entry<Point, Building> entry : p.buildings.entrySet()) {
            Point key = entry.getKey();
            Building value = entry.getValue();

            //Do stuff...
            if (value instanceof SpacePort) {
                tpane.setEnabledAt(2, true);
                break;
            }
        }
        if (c.values.get("haslaunch") != 1) {
            tpane.setEnabledAt(2, false);
        }
        building.update();
    }
}
