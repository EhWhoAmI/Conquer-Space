package ConquerSpace.gui.game;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.Point;
import ConquerSpace.gui.game.planetdisplayer.SpacePortMenu;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.gui.game.planetdisplayer.AtmosphereInfo;
import ConquerSpace.gui.game.planetdisplayer.construction.ConstructionMenu;
import ConquerSpace.gui.game.planetdisplayer.LocalLifeMenu;
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
    private ConstructionMenu building;
    private PlanetIndustry industry;
    private LocalLifeMenu localLifeMenu;
    
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
        building = new ConstructionMenu(u, p, c);
        industry = new PlanetIndustry(p, c);
        localLifeMenu = new LocalLifeMenu(p, c);

        tpane.add("Overview", overview);
        //tpane.add("Resources", new PlanetResources(p));
        tpane.add("Population", population);
        tpane.add("Space Port", spacePort);
        tpane.add("Atmosphere", atmosphere);
        tpane.add("Construction", building);
        tpane.add("Industry", industry);
        tpane.add("Local Life", localLifeMenu);

        tpane.setEnabledAt(2, false);
        //Check if planet contains space port
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
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
        industry.update();
        population.update();
        
        tpane.setEnabledAt(2, false);
        //Check if planet contains space port
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
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
