package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import ConquerSpace.Globals;
import ConquerSpace.game.StarDate;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.gui.game.LaunchSatelliteMenu;
import ConquerSpace.gui.game.LaunchSpaceShipMenu;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public class SpacePortBuilding extends PlanetSector {

    private int techLevel;
    private int ports;
    public SpacePortLaunchPad[] launchPads;
    private Planet planet;
    
    public SpacePortBuilding(int techLevel, int ports, LaunchSystem type, Planet p) {
        this.techLevel = techLevel;
        this.ports = ports;
        this.planet = p;
        launchPads = new SpacePortLaunchPad[ports];
        for (int i = 0; i < launchPads.length; i++) {
            launchPads[i] = new SpacePortLaunchPad(type);
        }
    }

    @Override
    public JPanel getInfoPanel() {
        JPanel root = new JPanel();
        JPanel launchInfo = new JPanel();

        JPanel launchingPanel = new JPanel();

        JList<SpacePortLaunchPad> pads = new JList<>(launchPads);
        JPanel pademptyPanel = new JPanel();
        JLabel pademptyPanel_type = new JLabel();
        JButton createSatelliteButton = new JButton("Launch Satellite");
        JButton createSpaceshipButton = new JButton("Launch Spaceship");
        //Show launch Satellite menu.
        createSatelliteButton.setFocusable(false);
        createSatelliteButton.addActionListener((e) -> {
            LaunchSatelliteMenu launch = new LaunchSatelliteMenu(pads.getSelectedValue(), Globals.universe.getCivilization(0), planet);
            Component c;
            for (c = root.getParent(); 
                    !(c instanceof JInternalFrame) || c != null; 
                    c = c.getParent()) {
                if (c instanceof JInternalFrame) {
                    ((JInternalFrame) c).getDesktopPane().add(launch);
                    break;
                }
            }
            launch.setVisible(true);
        });

        createSpaceshipButton.addActionListener(a -> {
            LaunchSpaceShipMenu launch = new LaunchSpaceShipMenu(pads.getSelectedValue(), Globals.universe.getCivilization(0), planet);
            Component c;
            for (c = root.getParent(); 
                    !(c instanceof JInternalFrame) || c != null; 
                    c = c.getParent()) {
                if (c instanceof JInternalFrame) {
                    ((JInternalFrame) c).getDesktopPane().add(launch);
                    break;
                }
            }
            launch.setVisible(true);
        });
        pademptyPanel.setLayout(new VerticalFlowLayout());
        pademptyPanel.add(pademptyPanel_type);
        pademptyPanel.add(createSatelliteButton);
        pademptyPanel.add(createSpaceshipButton);

        JPanel nothingSelectedPanel = new JPanel();

        CardLayout layout = new CardLayout();
        root.setLayout(new GridLayout(1, 2));

        pads.addListSelectionListener((e) -> {
            SpacePortLaunchPad launchPad = pads.getSelectedValue();
            if (launchPad.isLaunching()) {
                layout.show(launchInfo, "launching");
            } else {
                pademptyPanel_type.setText("Type: " + launchPad.getType().getName());

                layout.show(launchInfo, "pad-e");
            }
        });
        root.add(pads);

        launchInfo.setLayout(layout);
        launchInfo.add(launchingPanel, "launching");
        launchInfo.add(pademptyPanel, "pad-e");
        launchInfo.add(nothingSelectedPanel, "nothing");

        layout.show(launchInfo, "nothing");
        //Nothing at first
        root.add(launchInfo);
        return root;
    }
}
