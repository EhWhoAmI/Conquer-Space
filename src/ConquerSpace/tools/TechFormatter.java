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

import ConquerSpace.game.science.tech.Technologies;
import ConquerSpace.game.science.tech.Technology;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
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
public class TechFormatter extends JFrame {
    private DefaultListModel<Technology> technologyListModel;
    private JList<Technology> technologyList;

    private JTextField nameField;

    private JSpinner levelSpinner;
    private DefaultListModel<String> dependencyListModel;
    private JList<String> dependencies;

    private JSpinner difficulitySpinner;

    private DefaultListModel<String> actionDefaultListModel;
    private JList<String> actionsList;

    private DefaultListModel<String> fieldsListModel;
    private JList<String> fieldsList;

    private DefaultListModel<String> tagsListModel;
    private JList<String> tagsList;

    private ArrayList<Technology> technologys;

    public TechFormatter() {
        setTitle("Technologies");
        setLayout(new HorizontalFlowLayout());
        Technologies.readTech();

        technologys = new ArrayList<>();//Technologies.techonologies;
        JPanel techPanel = new JPanel(new VerticalFlowLayout());
        JTextField searchField = new JTextField(16);
        searchField.addActionListener(l -> {
            technologyListModel.removeAllElements();
            for (Technology g : technologys) {
                if (g.getName().toLowerCase().contains(searchField.getText().toLowerCase())) {
                    technologyListModel.addElement(g);
                }
            }
        });
        techPanel.add(searchField);

        technologyListModel = new DefaultListModel<>();
//        for (Technology tech : Technologies.techonologies) {
//            technologyListModel.addElement(tech);
//        }

        technologyList = new JList<>(technologyListModel);
        technologyList.setVisibleRowCount(36);
        
        technologyList.addListSelectionListener(l -> {
            Technology selectedTech = technologyList.getSelectedValue();
            if (selectedTech != null) {
                nameField.setText(selectedTech.getName());
                levelSpinner.setValue(selectedTech.getLevel());

                dependencyListModel.removeAllElements();
                for (String depName : selectedTech.getDeps()) {
                    dependencyListModel.addElement(depName);
                }

                difficulitySpinner.setValue(selectedTech.getDifficulty());

                actionDefaultListModel.removeAllElements();
                for (String actionName : selectedTech.getActions()) {
                    actionDefaultListModel.addElement(actionName);
                }

                fieldsListModel.removeAllElements();
                for (String fieldName : selectedTech.getFields()) {
                    fieldsListModel.addElement(fieldName);
                }

                tagsListModel.removeAllElements();
                for (String tagsName : selectedTech.getTags()) {
                    tagsListModel.addElement(tagsName);
                }
            }

        });

        techPanel.add(new JScrollPane(technologyList));

        JPanel techInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        techInfoPanel.add(new JLabel("Name: "), constraints);

        nameField = new JTextField(16);
        constraints.gridx = 1;
        constraints.gridy = 0;
        techInfoPanel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        techInfoPanel.add(new JLabel("Level: "), constraints);

        levelSpinner = new JSpinner(new SpinnerNumberModel());
        constraints.gridx = 1;
        constraints.gridy = 1;
        techInfoPanel.add(levelSpinner, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        techInfoPanel.add(new JLabel("Dependencies: "), constraints);

        dependencyListModel = new DefaultListModel<>();
        dependencies = new JList<>(dependencyListModel);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        techInfoPanel.add(new JScrollPane(dependencies), constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        techInfoPanel.add(new JLabel("Difficulty: "), constraints);

        difficulitySpinner = new JSpinner(new SpinnerNumberModel());
        constraints.gridx = 1;
        constraints.gridy = 4;
        techInfoPanel.add(difficulitySpinner, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        techInfoPanel.add(new JLabel("Actions: "), constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        actionDefaultListModel = new DefaultListModel<>();
        actionsList = new JList<>(actionDefaultListModel);
        techInfoPanel.add(new JScrollPane(actionsList), constraints);

        constraints.gridx = 0;
        constraints.gridy = 7;
        techInfoPanel.add(new JLabel("Fields: "), constraints);

        fieldsListModel = new DefaultListModel<>();
        fieldsList = new JList<>(fieldsListModel);
        constraints.gridx = 0;
        constraints.gridy = 8;
        techInfoPanel.add(new JScrollPane(fieldsList), constraints);

        constraints.gridx = 0;
        constraints.gridy = 9;
        techInfoPanel.add(new JLabel("Tags: "), constraints);

        tagsListModel = new DefaultListModel<>();
        tagsList = new JList<>(tagsListModel);
        constraints.gridx = 0;
        constraints.gridy = 10;
        techInfoPanel.add(new JScrollPane(tagsList), constraints);

        add(techPanel);
        add(techInfoPanel);

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
