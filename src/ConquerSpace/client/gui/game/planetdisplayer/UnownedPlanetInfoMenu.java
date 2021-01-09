/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.client.gui.game.planetdisplayer;

import ConquerSpace.client.gui.game.PlayerRegister;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.universe.PolarCoordinate;
import ConquerSpace.common.game.universe.bodies.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

/**
 * Window for the planet that is unowned, but surveyed
 *
 * @author EhWhoAmI
 */
public class UnownedPlanetInfoMenu extends JPanel {

    private static final int TILE_SIZE = 7;
    private JPanel planetOverview;
    private JPanel planetSectors;
    //private JList<Orbitable> satelliteList;
    private JLabel planetName;
    private JLabel planetPath;
    private JLabel planetType;
    private JLabel ownerLabel;
    private JLabel orbitDistance;
    
    //private ButtonGroup resourceButtonGroup;
    //private JRadioButton[] showResources;

    private JTabbedPane infoPane;

    private AtmosphereInfo atmosphereInfo;
    private NumberFormat numberFormatter;

    public UnownedPlanetInfoMenu(GameState gameState, Planet p, Civilization c, PlayerRegister register) {
        infoPane = new JTabbedPane();

        JPanel planetOverviewPanel = new JPanel();

        planetOverviewPanel.setLayout(new GridLayout(2, 1));
        numberFormatter = NumberFormat.getInstance();

        planetOverview = new JPanel();
        planetOverview.setLayout(new VerticalFlowLayout(5, 3));
        planetOverview.setBorder(new TitledBorder("Planet Info"));
        //If name is nothing, then call it unnamed planet
        planetName = new JLabel();
        planetPath = new JLabel();
        planetType = new JLabel("Planet type: " + p.getPlanetType());
        ownerLabel = new JLabel();
        PolarCoordinate pos = p.orbit.toPolarCoordinate();
        orbitDistance = new JLabel("Distance: " + numberFormatter.format(pos.getDistance()) + " km, " + numberFormatter.format((double) pos.getDistance() / 149598000d) + " AU");

        //Init planetname
        if (p.getName().equals("")) {
            planetName.setText("Unnamed Planet");
        } else {
            planetName.setText(p.getName());
        }

        //Init planetPath
        StringBuilder name = new StringBuilder();
        name.append("Star System ");
        name.append(Integer.toString(p.getParentIndex()));
        name.append(" Planet id " + p.getReference());
        planetPath.setText(name.toString());

        //Init owner
        if (p.getOwnerReference()!= ObjectReference.INVALID_REFERENCE) {
            ownerLabel.setText("Owner: " + c.getName());
        } else {
            ownerLabel.setText("No owner");
        }

        planetSectors = new JPanel();
        PlanetMapProvider planetMapProvider = new PlanetMapProvider(p);
        PlanetMinimapViewer planetMinimapViewer = new PlanetMinimapViewer(planetMapProvider, gameState, p, c);
        JPanel wrapper = new JPanel();
        wrapper.add(planetMinimapViewer);
        JTabbedPane buildingPanel = new JTabbedPane();

        JPanel buttonsWrapper = new JPanel();

        buildingPanel.add("Map", buttonsWrapper);

        buildingPanel.addChangeListener(a -> {
            if (buildingPanel.getSelectedIndex() == 1) {
                planetMinimapViewer.setWhatToShow(PlanetMinimapViewer.SHOW_ALL_RESOURCES);
            } else if (buildingPanel.getSelectedIndex() == 0) {
                planetMinimapViewer.setWhatToShow(PlanetMinimapViewer.PLANET_BUILDINGS);
            }
        });
        JScrollPane sectorsScrollPane = new JScrollPane(wrapper);
        planetSectors.add(sectorsScrollPane);
        //Add components
        planetOverview.add(planetName);
        planetOverview.add(planetPath);
        planetOverview.add(planetType);
        planetOverview.add(ownerLabel);
        planetOverview.add(orbitDistance);

        planetOverviewPanel.add(planetOverview);
        planetOverviewPanel.add(planetSectors);
        infoPane.add("Planet Overview", planetOverviewPanel);

        //Add atmosphere info
        atmosphereInfo = new AtmosphereInfo(p, c, register);
        infoPane.add("Atmosphere Info", atmosphereInfo);
        add(infoPane);
        //Add empty panel
        //add(new JPanel());
    }
}
