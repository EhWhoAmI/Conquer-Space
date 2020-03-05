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

import ConquerSpace.game.GameController;
import ConquerSpace.game.universe.resources.Resource;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author EhWhoAmI
 */
public class BuildResourceStorageMenu extends JPanel {

    JLabel resourceSize;
    JSpinner resourceSizeSpinner;
    JButton insertResourceButton;
    JButton removeResourceButton;
    JList<Resource> resourceToPut;
    JList<Resource> resourceInserted;
    DefaultListModel<Resource> resourceToPutListModel;
    DefaultListModel<Resource> resourceInsertedListModel;

    @SuppressWarnings("unchecked")
    public BuildResourceStorageMenu() {
        setLayout(new BorderLayout());
        JPanel ResourceAmountStorage = new JPanel();
        resourceSize = new JLabel("Amount of Resources:");
        SpinnerNumberModel mod = new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 100);
        resourceSizeSpinner = new JSpinner(mod);

        ResourceAmountStorage.add(resourceSize);
        ResourceAmountStorage.add(resourceSizeSpinner);

        JPanel resourcePanel = new JPanel();
        resourcePanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        removeResourceButton = new JButton("Remove Resource");
        removeResourceButton.addActionListener(a -> {
            if (resourceToPut.getSelectedIndex() > -1) {
                resourceInsertedListModel.addElement(resourceToPut.getSelectedValue());
                resourceToPutListModel.remove(resourceToPut.getSelectedIndex());
            }
        });

        resourceToPutListModel = new DefaultListModel<>();
        resourceToPut = new JList(resourceToPutListModel);

        JScrollPane resourceToPutScrollPane = new JScrollPane(resourceToPut);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        resourcePanel.add(removeResourceButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        resourcePanel.add(resourceToPutScrollPane, constraints);

        insertResourceButton = new JButton("Add resource");

        insertResourceButton.addActionListener(a -> {
            if (resourceInserted.getSelectedIndex() > -1) {
                resourceToPutListModel.addElement(resourceInserted.getSelectedValue());
                resourceInsertedListModel.remove(resourceInserted.getSelectedIndex());
            }
        });
        resourceInsertedListModel = new DefaultListModel<>();
        for (Resource res : GameController.resources) {
            resourceInsertedListModel.addElement(res);
        }
        resourceInserted = new JList<>(resourceInsertedListModel);
        JScrollPane resourceInsertedScrollPane = new JScrollPane(resourceInserted);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        resourcePanel.add(insertResourceButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        resourcePanel.add(resourceInsertedScrollPane, constraints);

        add(ResourceAmountStorage, BorderLayout.NORTH);
        add(resourcePanel, BorderLayout.SOUTH);
    }
}
