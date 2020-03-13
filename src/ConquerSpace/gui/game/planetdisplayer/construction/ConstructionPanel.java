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
package ConquerSpace.gui.game.planetdisplayer.construction;

import ConquerSpace.game.Calculators;
import ConquerSpace.game.GameUpdater;
import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.buildings.Observatory;
import ConquerSpace.game.buildings.ResourceMinerDistrict;
import ConquerSpace.game.buildings.ResourceStorage;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.Stratum;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.gui.game.planetdisplayer.PlanetMap;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 *
 * @author EhWhoAmI
 */
public class ConstructionPanel extends JInternalFrame implements InternalFrameListener {

    private JComboBox<String> buildingType;
    private DefaultComboBoxModel<String> buildingModel;

    private CardLayout buildCardLayout;

    private BuildPopulationStorage popStoragePanel;

    private BuildSpaceLaunchSite buildSpaceLaunchSite;

    private BuildObservatoryMenu buildObservatoryMenu;

    private BuildResourceStorageMenu buildResourceStorageMenu;

    private BuildResourceGenerationMenu buildMiningStorageMenu;

    private BuildIndustrialAreaMenu buildIndustrialAreaMenu;

    private final String RESIDENTIAL = "Residential area";
    private final String LAUNCH = "Launch Systems";
    private final String OBSERVATORY = "Observatory";
    private final String RESOURCE_STOCKPILE = "Resource Storage";
    private final String RESOURCE_MINER = "Resource Miner";
    private final String INDUSTRIAL_DISTRICT = "Industrial Area";

    JPanel mainItemContainer = new JPanel();

    private JButton buildButton;

    PlanetMap parent;

    public ConstructionPanel(Civilization c, Planet p, Universe u, GeographicPoint point, PlanetMap parent) {
        setLayout(new VerticalFlowLayout());

        this.parent = parent;

        buildingModel = new DefaultComboBoxModel<>();
        buildingModel.addElement(RESIDENTIAL);
        if (c.values.containsKey("haslaunch") && c.values.get("haslaunch") == 1) {
            //Do things
            buildingModel.addElement(LAUNCH);
        }
        buildingModel.addElement(OBSERVATORY);
        buildingModel.addElement(RESOURCE_STOCKPILE);
        buildingModel.addElement(RESOURCE_MINER);
        buildingModel.addElement(INDUSTRIAL_DISTRICT);
        buildingType = new JComboBox<>(buildingModel);

        buildingType.addActionListener(l -> {//Then update
            int selected = buildingType.getSelectedIndex();
            buildingModel.removeAllElements();
            buildingModel.addElement(RESIDENTIAL);
            if (c.values.containsKey("haslaunch") && c.values.get("haslaunch") == 1) {
                //Do things
                buildingModel.addElement(LAUNCH);
            }
            buildingModel.addElement(OBSERVATORY);
            buildingModel.addElement(RESOURCE_STOCKPILE);
            buildingModel.addElement(RESOURCE_MINER);
            buildingModel.addElement(INDUSTRIAL_DISTRICT);

            buildingType.setSelectedIndex(selected);
        });

        buildingType.addActionListener(a -> {
            if (buildingType.getSelectedIndex() > -1) {
                switch ((String) buildingType.getSelectedItem()) {
                    case RESIDENTIAL:
                        //Configure cost
                        //BuildingCostGetter.getCost("", c);
                        buildCardLayout.show(mainItemContainer, RESIDENTIAL);
                        break;
                    case LAUNCH:
                        //Configure cost
                        buildCardLayout.show(mainItemContainer, LAUNCH);
                        break;
                    case OBSERVATORY:
                        //Configure cost
                        //setBuildingCost(BuildingCostGetter.getCost("observatory", c));
                        buildCardLayout.show(mainItemContainer, OBSERVATORY);
                        break;
                    case RESOURCE_STOCKPILE:
                        //Configure cost
                        buildCardLayout.show(mainItemContainer, RESOURCE_STOCKPILE);
                        break;
                    case RESOURCE_MINER:
                        //Configure cost
                        buildCardLayout.show(mainItemContainer, RESOURCE_MINER);
                        break;
                    case INDUSTRIAL_DISTRICT:
                        buildCardLayout.show(mainItemContainer, INDUSTRIAL_DISTRICT);
                        break;
                }
            }
        });

        add(buildingType);

        popStoragePanel = new BuildPopulationStorage();

        buildSpaceLaunchSite = new BuildSpaceLaunchSite(c);

        buildObservatoryMenu = new BuildObservatoryMenu();

        buildResourceStorageMenu = new BuildResourceStorageMenu();

        buildMiningStorageMenu = new BuildResourceGenerationMenu(p, c);

        buildIndustrialAreaMenu = new BuildIndustrialAreaMenu();

        buildCardLayout = new CardLayout();
        mainItemContainer.setLayout(buildCardLayout);

        JPanel container = new JPanel(new BorderLayout());
        mainItemContainer.add(popStoragePanel, RESIDENTIAL);

        mainItemContainer.add(buildSpaceLaunchSite, LAUNCH);

        container = new JPanel(new BorderLayout());
        container.add(buildObservatoryMenu, BorderLayout.CENTER);
        mainItemContainer.add(container, OBSERVATORY);

        container = new JPanel(new BorderLayout());
        container.add(buildResourceStorageMenu, BorderLayout.CENTER);
        mainItemContainer.add(container, RESOURCE_STOCKPILE);

        mainItemContainer.add(buildMiningStorageMenu, RESOURCE_MINER);

        container = new JPanel(new BorderLayout());
        container.add(buildIndustrialAreaMenu, BorderLayout.CENTER);
        mainItemContainer.add(container, INDUSTRIAL_DISTRICT);

        add(mainItemContainer);

        buildButton = new JButton("Build!");
        buildButton.addActionListener(a -> {
            String item = (String) buildingType.getSelectedItem();
            boolean toReset = false;
            //Get x and y position
            GeographicPoint buildingPos = point;
            //Check if any buildings there:
            int toBuild = JOptionPane.YES_OPTION;
            if (p.buildings.containsKey(buildingPos)) {
                toBuild = JOptionPane.showConfirmDialog(null, "Do you want to demolish a " + p.buildings.get(buildingPos).toString() + " on the tile you are building?",
                        "Are you sure you want to demolish a building?", JOptionPane.YES_NO_OPTION);
            }
            if (toBuild == JOptionPane.YES_OPTION) {
                if (item.equals(RESIDENTIAL)) {
                    CityDistrict storage = new CityDistrict();
                    storage.setMaxStorage((int) popStoragePanel.maxPopulationTextField.getValue());
                    Actions.buildBuilding(p, buildingPos, storage, c, 1);
                    //Reset...
                    toReset = true;
                } else if (item.equals(LAUNCH)) {
                    //Get civ launching type...
                    SpacePort port = new SpacePort((LaunchSystem) buildSpaceLaunchSite.launchTypesValue.getSelectedItem(), (Integer) buildSpaceLaunchSite.maxPopulation.getValue());
                    Actions.buildBuilding(p, buildingPos, port, c, 1);
                    toReset = true;
                } else if (item.equals(OBSERVATORY)) {
                    StarSystem sys = u.getStarSystem(p.getParentStarSystem());
                    Observatory observatory = new Observatory(
                            Calculators.Optics.getRange(1, (int) buildObservatoryMenu.lensSizeSpinner.getValue()),
                            (Integer) buildObservatoryMenu.lensSizeSpinner.getValue(),
                            c.getID(), new ConquerSpace.game.universe.Point((long) sys.getX(), (long) sys.getY()));
                    //Add visionpoint to civ
                    c.visionPoints.add(observatory);
                    Actions.buildBuilding(p, buildingPos, observatory, c, 1);
                    toReset = true;
                } else if (item.equals(RESOURCE_STOCKPILE)) {
                    ResourceStorage stor = new ResourceStorage(p);
                    
                    stor.setMaximumStorage((int) buildResourceStorageMenu.resourceStorageSizeSpinner.getValue());
                    
                    //Add to planet...
                    c.resourceStorages.add(stor);
                    Actions.buildBuilding(p, buildingPos, stor, c, 1);
                    toReset = true;
                } else if (item.equals(RESOURCE_MINER)) {
                    ResourceMinerDistrict miner = new ResourceMinerDistrict(null, (double) buildMiningStorageMenu.miningSpeedSpinner.getValue());
                    //Add the stuff...
                    //Get the map of things, and calculate the stuff
                    int x = buildingPos.getX();
                    int y = buildingPos.getY();
                    //TODO make it read resources
                    //Resource res = (Resource) buildMiningStorageMenu.resourceToMine.getSelectedItem();
                    for (Stratum v : p.strata) {
                        if (Math.hypot(x - v.getX(), y - v.getY()) < v.getRadius()) {
                            //miner.setVeinMining(v);
                            break;
                        }
                    }
                    //Add miners
                    miner.population.add(new PopulationUnit(c.getFoundingSpecies()));
                    Actions.buildBuilding(p, buildingPos, miner, c, 1);
                    toReset = true;
                }
                //Close the current window
                if (toReset) {
                    parent.resetBuildingIndicator();
                    dispose();
                }
            }
        });
        add(buildButton);
        pack();

        addInternalFrameListener(this);
        setVisible(true);
        setClosable(true);
        setResizable(true);
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        //Deactivate point
        parent.resetBuildingIndicator();
        dispose();
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }

}
