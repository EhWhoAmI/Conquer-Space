package ConquerSpace.gui.game;

import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.SpacePortLaunchPad;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public class LaunchSatelliteMenu extends JInternalFrame{
    private JPanel satelliteSelectPanel;
    private JList<Satellite> satelliteSelectList;
    private DefaultListModel<Satellite> listModel;
    private JLabel title;
    
    private JPanel statsPanel;
    private JLabel satelliteName;
    private JLabel satelliteMass;
    private JButton buildAndLaunchButton;
    public LaunchSatelliteMenu(SpacePortLaunchPad pad, Civilization c, Planet p) {
        //The launch pad type and stuff as title
        setTitle("Launch a satellite using " + pad.getType().getName());
        //title = new JLabel("Launch Satellite");
        setLayout(new GridLayout(1, 2));
        
        //Satellite select content
        satelliteSelectPanel = new JPanel();
        
        listModel = new DefaultListModel<>();
        
        for(Satellite s : c.satellites) {
            listModel.addElement(s);
        }
        satelliteSelectList = new JList(listModel);
        //So that one is selected every time
        satelliteSelectList.setSelectedIndex(0);
        
        satelliteSelectPanel.add(satelliteSelectList);
        
        //Satellite stats content
        statsPanel = new JPanel();
        statsPanel.setLayout(new VerticalFlowLayout());
        satelliteName = new JLabel(satelliteSelectList.getSelectedValue().toString());
        statsPanel.add(satelliteName);
        
        satelliteMass = new JLabel("Mass: " + satelliteSelectList.getSelectedValue().getMass());
        statsPanel.add(satelliteMass);
        
        buildAndLaunchButton = new JButton("Launch!");
        buildAndLaunchButton.setFocusable(false);
        buildAndLaunchButton.addActionListener(e -> {
            Actions.launchSatellite(satelliteSelectList.getSelectedValue(), p, 100, c);
            dispose();
        });
        statsPanel.add(buildAndLaunchButton);
        
        //Add update code for the satellite selection
        satelliteSelectList.addListSelectionListener(e -> {
            satelliteName.setText(satelliteSelectList.getSelectedValue().getName());
            satelliteMass.setText("Mass: " + satelliteSelectList.getSelectedValue().getMass());
        });
        
        add(satelliteSelectList);
        add(statsPanel);
        setResizable(true);
        setVisible(true);
        setClosable(true);
        pack();
        
        //Bring to front
        toFront();
    }
    
}