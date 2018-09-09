package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.game.universe.spaceObjects.pSectors.SpacePortLaunchPad;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author Zyun
 */
public class LaunchSatelliteMenu extends JInternalFrame{
    private JList<Satellite> satelliteSelectList;
    private DefaultListModel<Satellite> listModel;
    private JLabel title;
    public LaunchSatelliteMenu(SpacePortLaunchPad pad, Civilization c) {
        //The launch pad type and stuff as title
        setTitle("Launch a satellite using " + pad.getType().getName());
        title = new JLabel("Launch Satellite");
        listModel = new DefaultListModel<>();

        for(Satellite s : c.satellites) {
            listModel.addElement(s);
            
        }
        satelliteSelectList = new JList(listModel);
        
        add(title);
        add(satelliteSelectList);
        setResizable(true);
        setVisible(true);
        setClosable(true);
        pack();
    }
    
}
