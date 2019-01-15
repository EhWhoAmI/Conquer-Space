package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * All the info and charts and stuff of the civ.
 * @author Zyun
 */
public class CivInfoOverview extends JInternalFrame{
    private JPanel mainPanel;
    private JPanel resourcesPanel;
    private JTabbedPane mainTabs;
    public CivInfoOverview(Civilization c, Universe u) {
        //Civ name
        mainTabs = new JTabbedPane(JTabbedPane.BOTTOM);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalFlowLayout());
        
        JLabel civName = new JLabel(c.getName());
        JLabel civHomePlanet = new JLabel("From " + c.getHomePlanetName());
        JLabel civTechLevel = new JLabel("Tech level: " + c.getTechLevel());
        
        mainPanel.add(civName);
        mainPanel.add(civHomePlanet);
        mainPanel.add(civTechLevel);
        resourcesPanel = new JPanel();
        mainTabs.addTab("Civilization", mainPanel);
        mainTabs.addTab("Resources", resourcesPanel);
        add(mainTabs);
        
        setSize(500, 400);
        setClosable(true);
        setResizable(true);
        setVisible(true);
    }
    
}
