package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * All the info and charts and stuff of the civ.
 *
 * @author Zyun
 */
public class CivInfoOverview extends JPanel {

    private JPanel mainPanel;
    private JPanel resourcesPanel;
    private JPanel populationPanel;
    private JTabbedPane mainTabs;

    public CivInfoOverview(Civilization c, Universe u) {
        setLayout(new BorderLayout());
        //Civ name
        mainTabs = new JTabbedPane(JTabbedPane.BOTTOM);

        mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalFlowLayout());

        JLabel civName = new JLabel(c.getName());
        JLabel civHomePlanet = new JLabel("From " + c.getHomePlanetName());
        JLabel civTechLevel = new JLabel("Tech level: " + c.getTechLevel());
        JLabel civFoundingSpecies = new JLabel("Founding Species: " + c.getFoundingSpecies().getName());
        JLabel capital = new JLabel("Capital: " + c.getCapitalCity().getName() + " on " + c.getCapitalPlanet().getName());

        mainPanel.add(civName);
        mainPanel.add(civHomePlanet);
        mainPanel.add(civTechLevel);
        mainPanel.add(civFoundingSpecies);
        mainPanel.add(capital);

        resourcesPanel = new JPanel();

        populationPanel = new JPanel();
        //Add population count and stuff
        JLabel populationLabel = new JLabel("Population: " + (c.population.size() * 10) + " million");
        populationPanel.add(populationLabel);

        mainTabs.addTab("Civilization", mainPanel);
        //mainTabs.addTab("Resources", resourcesPanel);
        mainTabs.addTab("Population", populationPanel);
        add(mainTabs, BorderLayout.CENTER);

        //Updating code
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                //Update info...
                populationLabel.setText("Population: " + (c.population.size() * 10) + " million");
            }
        });
        setVisible(true);
    }

}
