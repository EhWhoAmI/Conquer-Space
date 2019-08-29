package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.City;
import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Zyun
 */
public class PlanetIndustry extends JPanel{
    private JPanel areaContainer = new JPanel();
    private DefaultListModel<Area> areaDefaultListModel;
    private JList<Area> areaList;
    public PlanetIndustry(Planet p, Civilization c) {
        setLayout(new VerticalFlowLayout());
        add(new JLabel("Industry"));
        //Industry stuff:
        //Includes:
        //Labs,
        //Foundry,
        //Ship yard,
        //Production line,
        //Mill
        //Etc... 
        //Get the cities of the planet...
        areaContainer = new JPanel();
        areaDefaultListModel = new DefaultListModel<>();
        for(Building city : p.buildings.values()) {
            for(Area a : city.areas) {
                areaDefaultListModel.addElement(a);
            }
        }
        areaList = new JList<>(areaDefaultListModel);
        JScrollPane scrollPane = new JScrollPane(areaList);
        
        areaContainer.add(scrollPane);
        //Do other UI
        add(areaContainer);
    }
    
}
