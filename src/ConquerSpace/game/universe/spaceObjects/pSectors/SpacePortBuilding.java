package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.StarDate;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
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

    public SpacePortBuilding(int techLevel, int ports, int type) {
        this.techLevel = techLevel;
        this.ports = ports;
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

        JPanel pademptyPanel = new JPanel();
        JLabel pademptyPanel_type = new JLabel();
        JButton createSatelliteButton = new JButton("Launch Satellite");
        pademptyPanel.add(pademptyPanel_type);
        pademptyPanel.add(createSatelliteButton);

        JPanel nothingSelectedPanel = new JPanel();

        CardLayout layout = new CardLayout();
        root.setLayout(new GridLayout(1, 2));

        JList<SpacePortLaunchPad> pads = new JList<>(launchPads);
        pads.addListSelectionListener((e) -> {
            SpacePortLaunchPad launchPad = pads.getSelectedValue();
            if (launchPad.isLaunching()) {
                layout.show(launchInfo, "launching");
            } else {
                pademptyPanel_type.setText("Type: " + LaunchPadTypes.getLaunchPadTypeName(launchPad.getType()));
                
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

    @Override
    public void processTurn(int GameRefreshRate, StarDate stardate) {
        for (SpacePortLaunchPad launchPad : launchPads) {
            //Change ticks
            launchPad.ticks += GameRefreshRate;
            //Check for launch...
        }
    }
}
