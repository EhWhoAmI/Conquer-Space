package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.gui.game.LaunchSatelliteMenu;
import ConquerSpace.gui.game.LaunchSpaceShipMenu;
import com.alee.extended.layout.VerticalFlowLayout;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author Ehwhoami
 */
public class SpacePortMenu extends JPanel {

    private ArrayList<SpacePortLaunchPad> launchPads;
    
    private JList<SpacePortLaunchPad> launchPadList;

    public SpacePortMenu(Planet p, Civilization c) {
        setLayout(new VerticalFlowLayout());
        launchPads = new ArrayList<>();
        //Do stuff
        //Compile all the launch pads on the planet

        DefaultListModel<SpacePortLaunchPad> spaceLPModel = new DefaultListModel<>();

        for (Map.Entry<Point, Building> entry : p.buildings.entrySet()) {
            Point key = entry.getKey();
            Building value = entry.getValue();

            //Do stuff...
            if (value instanceof SpacePort) {
                SpacePort port = (SpacePort) value;
                //Do things
                for (SpacePortLaunchPad lp : port.launchPads) {
                    launchPads.add(lp);
                    spaceLPModel.addElement(lp);
                }
            }
        }
        //Add components and stuff

        launchPadList = new JList<>(spaceLPModel);
        JScrollPane launchPadListScrollPane = new JScrollPane(launchPadList);
        JTabbedPane pane = new JTabbedPane();
        JPanel launchSatelliteMenuContainer = new JPanel();
        pane.add("Satellite", launchSatelliteMenuContainer);
        JPanel launchSpaceShipJPanelMenuContainer = new JPanel();
        pane.add("Space Ship", launchSpaceShipJPanelMenuContainer);

        //LaunchSatelliteMenu launchSatelliteMenu = new LaunchSatelliteMenu(pad, c, p, sys);
        launchPadList.addListSelectionListener(a -> {
            //Show the thingy
            launchSatelliteMenuContainer.removeAll();
            LaunchSatelliteMenu launchSatelliteMenu = new LaunchSatelliteMenu(launchPadList.getSelectedValue(), c, p);
            launchSatelliteMenuContainer.add(launchSatelliteMenu);
            launchSpaceShipJPanelMenuContainer.removeAll();
            LaunchSpaceShipMenu launchSpaceShipMenu = new LaunchSpaceShipMenu(launchPadList.getSelectedValue(), c, p);
            launchSpaceShipJPanelMenuContainer.add(launchSpaceShipMenu);
        });
        
        add(launchPadListScrollPane);
        add(pane);
    }
}
