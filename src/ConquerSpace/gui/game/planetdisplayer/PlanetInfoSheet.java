package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Zyun
 */
public class PlanetInfoSheet extends JPanel {

    JTabbedPane tpane;
    PlanetOverview overview;
    PlanetPopulation population;
    SpacePortMenu spacePort;
    AtmosphereInfo atmosphere;
    PlanetIndustry industry;
    LocalLifeMenu localLifeMenu;
    PlanetMap planetMap;
    PlanetEconomy planetEconomy;

    private Civilization c;
    private Planet p;
    
    private final int spacePortIndex = 3;

    public PlanetInfoSheet(Universe u, Planet p, Civilization c) {
        this.c = c;
        this.p = p;
        setLayout(new BorderLayout());
        tpane = new JTabbedPane();
        
        overview = new PlanetOverview(u, p, c);
        atmosphere = new AtmosphereInfo(p, c);
        population = new PlanetPopulation(u, p, 0);
        spacePort = new SpacePortMenu(p, c);
        //building = new ConstructionMenu(u, p, c);
        industry = new PlanetIndustry(p, c);
        localLifeMenu = new LocalLifeMenu(p, c);
        planetMap = new PlanetMap(p, c, u, this);
        planetEconomy = new PlanetEconomy();

        tpane.add("Overview", overview);
        tpane.add("Map", planetMap);
        tpane.add("Cities", population);
        tpane.add("Space Port", spacePort);
        tpane.add("Atmosphere", atmosphere);
        tpane.add("Industry", industry);
        tpane.add("Local Life", localLifeMenu);

        tpane.setEnabledAt(spacePortIndex, false);
        //Check if planet contains space port
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
            Building value = entry.getValue();

            //Do stuff...
            if (value instanceof SpacePort) {
                tpane.setEnabledAt(spacePortIndex, true);
                break;
            }
        }
        if (c.values.get("haslaunch") != 1) {
            tpane.setEnabledAt(spacePortIndex, false);
        }

        add(tpane, BorderLayout.CENTER);
    }

    public void init() {

    }

    public void update() {
        spacePort.update();
        industry.update();
        population.update();

        tpane.setEnabledAt(spacePortIndex, false);
        //Check if planet contains space port
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
            Building value = entry.getValue();

            //Enable space port tab
            if (value instanceof SpacePort) {
                tpane.setEnabledAt(spacePortIndex, true);
                break;
            }
        }
        if (c.values.get("haslaunch") != 1) {
            tpane.setEnabledAt(spacePortIndex, false);
        }
    }
}