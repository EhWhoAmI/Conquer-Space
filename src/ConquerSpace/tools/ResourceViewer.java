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
package ConquerSpace.tools;

import ConquerSpace.game.AssetReader;
import static ConquerSpace.game.AssetReader.readHjsonFromDirInArray;
import ConquerSpace.game.GameController;
import ConquerSpace.game.GameLoader;
import ConquerSpace.game.universe.resources.Resource;
import com.alee.extended.layout.HorizontalFlowLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author EhWhoAmI
 */
public class ResourceViewer extends JFrame {

    public static void main(String[] args) {
        new ResourceViewer();
    }

    private JList<Resource> resourceList;
    private DefaultListModel<Resource> resourceListModel;
    private ArrayList<Resource> resources;

    private JPanel resourceInfoPanel;

    private JTextField resourceNameField;
    private JSpinner id;
    private JSpinner rarity;
    private JSpinner value;
    private JSpinner density;
    private JSpinner difficulty;
    private JSpinner distribution;
    private JButton setColorButton;
    private JColorChooser colorChooser;
    private JCheckBox mineable;
    private JCheckBox raw;

    private Color resourceColor = Color.BLACK;

    public ResourceViewer() {
        setTitle("Resource Config");

        //Open resource file
        setLayout(new HorizontalFlowLayout());

        GameLoader.loadResources();

        resources = GameController.resources;

        //Open the text...
        resourceListModel = new DefaultListModel<>();
        for (Resource r : resources) {
            resourceListModel.addElement(r);
        }

        resourceList = new JList<>(resourceListModel);
        resourceList.addListSelectionListener(l -> {
            //Add components...
            Resource selected = resourceList.getSelectedValue();
            resourceNameField.setText(selected.getName());
            id.setValue(selected.getId());
            rarity.setValue(selected.getRarity());
            value.setValue(selected.getValue());
            density.setValue(selected.getDensity());
            difficulty.setValue(selected.getDifficulty());;
            //distribution.setValue(selected.getD);
            resourceColor = selected.getColor();
            setColorButton.setBackground(resourceColor);
            mineable.setSelected(selected.isMineable());
        });

        add(new JScrollPane(resourceList));

        resourceInfoPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        resourceInfoPanel.add(new JLabel("Name: "), constraints);

        resourceNameField = new JTextField(16);
        constraints.gridx = 1;
        constraints.gridy = 0;
        resourceInfoPanel.add(resourceNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        resourceInfoPanel.add(new JLabel("Id: "), constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        id = new JSpinner(new SpinnerNumberModel());
        resourceInfoPanel.add(id, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        resourceInfoPanel.add(new JLabel("Rarity: "), constraints);

        rarity = new JSpinner();
        constraints.gridx = 1;
        constraints.gridy = 2;
        id = new JSpinner(new SpinnerNumberModel());
        resourceInfoPanel.add(rarity, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        resourceInfoPanel.add(new JLabel("Value: "), constraints);

        value = new JSpinner();
        constraints.gridx = 1;
        constraints.gridy = 3;
        resourceInfoPanel.add(value, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        resourceInfoPanel.add(new JLabel("Density: "), constraints);
        density = new JSpinner();
        constraints.gridx = 1;
        constraints.gridy = 4;
        resourceInfoPanel.add(density, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        resourceInfoPanel.add(new JLabel("Difficulty: "), constraints);
        difficulty = new JSpinner();
        constraints.gridx = 1;
        constraints.gridy = 5;
        resourceInfoPanel.add(difficulty, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        resourceInfoPanel.add(new JLabel("Distribution: "), constraints);
        distribution = new JSpinner();
        constraints.gridx = 1;
        constraints.gridy = 6;
        resourceInfoPanel.add(distribution, constraints);

        setColorButton = new JButton("Choose color");
        setColorButton.addActionListener(l -> {
            resourceColor = JColorChooser.showDialog(null,
                    "JColorChooser Sample", resourceColor);
            setColorButton.setBackground(resourceColor);
        });
        constraints.gridx = 0;
        constraints.gridy = 7;
        resourceInfoPanel.add(setColorButton, constraints);

        mineable = new JCheckBox("Mineable? ");
        constraints.gridx = 0;
        constraints.gridy = 8;
        resourceInfoPanel.add(mineable, constraints);

        raw = new JCheckBox("Raw Resource? ");
        constraints.gridx = 0;
        constraints.gridy = 9;
        resourceInfoPanel.add(raw, constraints);

        add(resourceInfoPanel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
