package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.universe.civilization.controllers.PlayerController.planetdisplayer.PlanetOverview;
import ConquerSpace.game.universe.spaceObjects.Planet;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author Zyun
 */
public class PlanetInfoSheet extends JFrame{
    private JTabbedPane tpane;
    public PlanetInfoSheet(Planet p) {
        tpane = new JTabbedPane();
        tpane.add("Overview", new PlanetOverview());
        add(tpane);
        pack();
        setVisible(true);
    }
}
