package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.gui.game.planetdisplayer.PlanetOverview;
import ConquerSpace.gui.game.planetdisplayer.PlanetResources;
import ConquerSpace.gui.game.planetdisplayer.PlayerPopulation;
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
        tpane.add("Resources", new PlanetResources(p));
        tpane.add("Population", new PlayerPopulation(p, 0));
        add(tpane);
        pack();
        setResizable(true);
        setClosable(true);
        setVisible(true);
    }
}
