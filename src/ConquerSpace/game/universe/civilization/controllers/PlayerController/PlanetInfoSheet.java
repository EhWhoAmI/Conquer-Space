package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.planetdisplayer.PlanetOverview;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.planetdisplayer.PlayerPopulation;
import ConquerSpace.game.universe.spaceObjects.Planet;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author Zyun
 */
public class PlanetInfoSheet extends JInternalFrame {

    private JTabbedPane tpane;

    public PlanetInfoSheet(Planet p, Civilization c) {
        tpane = new JTabbedPane();
        tpane.add("Overview", new PlanetOverview(p, c));
        tpane.add("Population", new PlayerPopulation(p, 0));
        add(tpane);
        setSize(500, 500);
        setResizable(true);
        setClosable(true);
        setVisible(true);
    }
}
