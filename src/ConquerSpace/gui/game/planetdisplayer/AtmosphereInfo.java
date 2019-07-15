package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.Orbitable;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.gui.game.ShipInformationMenu;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author zyunl
 */
public class AtmosphereInfo extends JPanel {

    private JPanel atmosphereInfoPanel;
    private JPanel stuffInOrbitPanel;
    private ShipInformationMenu shipInformationMenu;
    private JList<Orbitable> shipsInOrbitList;
    private DefaultListModel<Orbitable> shipsInOrbitListModel;
    private JPanel shipInfoContainer;

    private Planet planet;

    public AtmosphereInfo(Planet p, Civilization c) {
        setLayout(new BorderLayout());
        planet = p;
        //Draw panels and stuff
        //The objects in orbit
        stuffInOrbitPanel = new JPanel(new GridLayout(1, 2));
        stuffInOrbitPanel.setBorder(new TitledBorder("In Orbit"));

        //Show all the things in orbit
        shipsInOrbitListModel = new DefaultListModel<>();
        updateList();

        shipsInOrbitList = new JList<>(shipsInOrbitListModel);
        JScrollPane shipScrollPane = new JScrollPane(shipsInOrbitList);
        stuffInOrbitPanel.add(shipScrollPane);

        shipInfoContainer = new JPanel();
        stuffInOrbitPanel.add(shipInfoContainer);
        //Display ship info....
        shipsInOrbitList.addListSelectionListener(a -> {
            if (shipsInOrbitList.getSelectedValue() instanceof Ship) {
                shipInformationMenu = new ShipInformationMenu((Ship) shipsInOrbitList.getSelectedValue(), c);
                shipInfoContainer.removeAll();
                shipInfoContainer.add(shipInformationMenu);
            }
        });
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent arg0) {
            }

            @Override
            public void componentMoved(ComponentEvent arg0) {
            }

            @Override
            public void componentShown(ComponentEvent arg0) {
            }

            @Override
            public void componentHidden(ComponentEvent arg0) {
                updateList();
            }
        });
        add(stuffInOrbitPanel, BorderLayout.NORTH);
    }

    public void updateList() {
        for (int i = 0; i < planet.getSatelliteCount(); i++) {
            Orbitable orb = planet.getSatellite(i);
            if (!shipsInOrbitListModel.contains(orb)) {
                shipsInOrbitListModel.addElement(orb);
            }
        }
    }
}
