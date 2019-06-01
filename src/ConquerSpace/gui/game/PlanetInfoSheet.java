package ConquerSpace.gui.game;

import ConquerSpace.gui.game.planetdisplayer.SpacePortMenu;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.gui.game.planetdisplayer.AtmosphereInfo;
import ConquerSpace.gui.game.planetdisplayer.PlanetOverview;
import ConquerSpace.gui.game.planetdisplayer.PlanetResources;
import ConquerSpace.gui.game.planetdisplayer.PlayerPopulation;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Zyun
 */
public class PlanetInfoSheet extends JPanel {

    private JTabbedPane tpane;

    public PlanetInfoSheet(Universe u, Planet p, Civilization c) {
        tpane = new JTabbedPane();
        tpane.add("Overview", new PlanetOverview(u, p, c));
        tpane.add("Resources", new PlanetResources(p));
        tpane.add("Population", new PlayerPopulation(p, 0));
        tpane.add("Space Port", new SpacePortMenu(p, c));
        tpane.add("Atmosphere", new AtmosphereInfo(p, c));
        
        //Check if planet contains space port
        tpane.setEnabledAt(3, false);
        
        add(tpane);
    }
    
    public void init() {
        
    }
    
    public void update() {
        
    }
}
