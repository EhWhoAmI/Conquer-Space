package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.PopulationStorage;
import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.population.Job;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author Zyun
 */
public class PlanetIndustry extends JPanel {

    private JTabbedPane tabs;

    private JPanel areaContainer;
    private DefaultListModel<Area> areaDefaultListModel;
    private JList<Area> areaList;

    

    private Planet p;

    public PlanetIndustry(Planet p, Civilization c) {
        this.p = p;
        setLayout(new VerticalFlowLayout());
        add(new JLabel("Industry"));
        tabs = new JTabbedPane();
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
        areaContainer.setLayout(new BorderLayout());
        areaDefaultListModel = new DefaultListModel<>();
        for (Building city : p.buildings.values()) {
            for (Area a : city.areas) {
                areaDefaultListModel.addElement(a);
            }
        }
        areaList = new JList<>(areaDefaultListModel);
        JScrollPane scrollPane = new JScrollPane(areaList);
        areaContainer.add(scrollPane, BorderLayout.WEST);
        tabs.add(areaContainer, "Areas");

        
        
        //Do other UI
        add(tabs);
    }

    public void update() {
//        
        int selectedArea = areaList.getSelectedIndex();
        areaDefaultListModel.clear();
        for (Building city : p.buildings.values()) {
            for (Area a : city.areas) {
                areaDefaultListModel.addElement(a);
            }
        }
        areaList.setSelectedIndex(selectedArea);
    }
}
