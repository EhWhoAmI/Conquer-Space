package ConquerSpace.gui.game;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.Point;
import ConquerSpace.gui.game.planetdisplayer.SpacePortMenu;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.gui.game.planetdisplayer.AtmosphereInfo;
import ConquerSpace.gui.game.planetdisplayer.PlanetOverview;
import ConquerSpace.gui.game.planetdisplayer.PlanetResources;
import ConquerSpace.gui.game.planetdisplayer.PlayerPopulation;
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

    public PlanetInfoSheet(Universe u, Planet p, Civilization c) {
        setLayout(new BorderLayout());
        tpane = new JTabbedPane();
        tpane.add("Overview", new PlanetOverview(u, p, c));
        //tpane.add("Resources", new PlanetResources(p));
        tpane.add("Population", new PlayerPopulation(p, 0));
        tpane.add("Space Port", new SpacePortMenu(p, c));
        tpane.add("Atmosphere", new AtmosphereInfo(p, c));

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

    }
}
