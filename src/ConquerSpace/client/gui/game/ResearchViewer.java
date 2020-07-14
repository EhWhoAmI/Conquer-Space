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
package ConquerSpace.client.gui.game;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.GameState;
import ConquerSpace.common.actions.Alert;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.characters.Person;
import ConquerSpace.common.game.characters.Scientist;
import ConquerSpace.common.game.science.Technologies;
import ConquerSpace.common.game.science.Technology;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author EhWhoAmI
 */
public class ResearchViewer extends JPanel implements ListSelectionListener, PropertyChangeListener {

    private JTabbedPane pane;

    private JLabel techPointLabel;
    private JLabel techPointCount;
    private JButton instantReshButton;
    private JPanel techResearcher;
    private JList<Technology> tech;
    private DefaultListModel<Technology> list;
    private JPanel techInfoPanel;
    private JLabel techName;
    private JLabel techdifficulity;
    private JLabel techEstTime;

    private DefaultListModel<String> fieldListModel;
    private JList<String> fieldList;
    private JButton researchButton;

    private DefaultComboBoxModel<Person> personComboBoxModel;
    private JComboBox<Person> personComboBox;

    private JPanel researchProgressPanel;
    private JLabel researchingTech;
    private JLabel researcher;
    private JLabel estTimeLeft;
    private TechonologyViewer techonologyViewer;
    private JTable techTable;
    private DefaultTableModel techTableModel;

    private FieldViewer fieldViewer;
    private Civilization civilization;

    private GameState gameState;

    public ResearchViewer(GameState gameState, Civilization c) {
        this.civilization = c;
        this.gameState = gameState;
        init();

        update();

        add(pane, BorderLayout.CENTER);
    }

    public void init() {
        setLayout(new BorderLayout());

        pane = new JTabbedPane();
        techResearcher = new JPanel();
        techResearcher.setLayout(new GridLayout(1, 2));

        //Get the list of researched tech
        list = new DefaultListModel<>();
        //list.addElement(element);
        for (Technology t : civilization.civTechs.keySet()) {
            if (civilization.civTechs.get(t) == Technologies.REVEALED) {
                list.addElement(t);
            }
        }
        tech = new JList<>(list);
        tech.addListSelectionListener(this);

        techInfoPanel = new JPanel();
        techInfoPanel.setLayout(new VerticalFlowLayout());

        techName = new JLabel("");
        techInfoPanel.add(techName);

        techdifficulity = new JLabel("");
        techInfoPanel.add(techdifficulity);

        techEstTime = new JLabel("");
        techInfoPanel.add(techEstTime);

        fieldListModel = new DefaultListModel<>();
        fieldList = new JList<>(fieldListModel);
        JScrollPane listPane = new JScrollPane(fieldList);
        techInfoPanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.research.fields") + ":"));
        techInfoPanel.add(listPane);

        personComboBoxModel = new DefaultComboBoxModel<>();
        for (Integer id : civilization.people) {
            Person p = gameState.getObject(id, Person.class);
            if (p instanceof Scientist) {
                personComboBoxModel.addElement(p);
            }
        }
        techInfoPanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.research.person.researcher") + ":"));
        personComboBox = new JComboBox<>(personComboBoxModel);
        techInfoPanel.add(personComboBox);

        researchButton = new JButton(LOCALE_MESSAGES.getMessage("game.research.fields"));
        researchButton.setFocusable(false);
        researchButton.addActionListener((e) -> {
            //Get first researcher to research
            if (!tech.isSelectionEmpty()) {
                civilization.assignResearch(tech.getSelectedValue(), (Scientist) personComboBox.getSelectedItem());
                list.removeElement(tech.getSelectedValue());
                //Set everything empty
                techName.setText("");
                techdifficulity.setText("");
                techEstTime.setText("");
            }
        });
        techInfoPanel.add(researchButton);
        //If has tech points
        techPointLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.research.techpointsleft", civilization.getTechPoints()));
        techPointCount = new JLabel(LOCALE_MESSAGES.getMessage("game.research.techpointsworth"));
        instantReshButton = new JButton(LOCALE_MESSAGES.getMessage("game.research.instantlyresearchbutton"));
        instantReshButton.setToolTipText(LOCALE_MESSAGES.getMessage("game.research.instantlyresearchbutton.tooltip"));
        instantReshButton.addActionListener(l -> {
            //calculate and add
            if (!tech.isSelectionEmpty()) {
                if (tech.getSelectedValue().getDifficulty() <= civilization.getTechPoints()) {
                    Technology t = tech.getSelectedValue();
                    civilization.researchTech(gameState, t);
                    civilization.setTechPoints(civilization.getTechPoints() - t.getDifficulty());
                    techPointLabel.setText(LOCALE_MESSAGES.getMessage("game.research.techpointsleft", civilization.getTechPoints()));
                    list.removeElement(tech.getSelectedValue());
                    civilization.controller.alert(new Alert(0, 0, LOCALE_MESSAGES.getMessage("game.research.alert.autoresearch", t.getName())));
                }

                //Remove from list
                if (civilization.getTechPoints() <= 0) {
                    //Remove
                    techInfoPanel.remove(techPointLabel);
                    techInfoPanel.remove(techPointCount);
                    techInfoPanel.remove(instantReshButton);
                }
            }
        });

        if (civilization.getTechPoints() > 0) {
            techInfoPanel.add(techPointLabel);
            techInfoPanel.add(techPointCount);
            techInfoPanel.add(instantReshButton);
        }

        //Hide all the UI because nothing is selected
        techInfoPanel.setVisible(false);

        researchProgressPanel = new JPanel();
        researchProgressPanel.setLayout(new VerticalFlowLayout());

        techTableModel = new DefaultTableModel(new String[]{
            LOCALE_MESSAGES.getMessage("game.research.table.currentlyresearchingtech"),
            LOCALE_MESSAGES.getMessage("game.research.table.researcher"),
            LOCALE_MESSAGES.getMessage("game.research.table.timeleft")},
                0);
        techTable = new JTable(techTableModel);
        JScrollPane techTableContainer = new JScrollPane(techTable);
        researchProgressPanel.add(techTableContainer);

        researchingTech = new JLabel("");
        //researchProgressPanel.add(researchingTech);

        researcher = new JLabel("");
        //researchProgressPanel.add(researcher);

        estTimeLeft = new JLabel("");
        //researchProgressPanel.add(estTimeLeft);

        techResearcher.add(tech);
        techResearcher.add(techInfoPanel);
        pane.addChangeListener((e) -> {
            techTableModel.setRowCount(0);
            for (Technology t : civilization.currentlyResearchingTechonologys.keySet()) {
                //researchingTech.setText(t.getName());
                //researcher.setText("Researcher: " + c.currentlyResearchingTechonologys.get(t).getName());
                //estTimeLeft.setText("Estimated time left: " + ((Technologies.estFinishTime(t) - c.civResearch.get(t) / c.currentlyResearchingTechonologys.get(t).getSkill()) / 720) + " months");
                techTableModel.addRow(new String[]{t.getName(), civilization.currentlyResearchingTechonologys.get(t).getName(), ((Technologies.estFinishTime(t) - civilization.civResearch.get(t) / civilization.currentlyResearchingTechonologys.get(t).getSkill()) / 720) + " months"});
            }
            personComboBoxModel.removeAllElements();
            for (Integer id : civilization.people) {
                Person p = gameState.getObject(id, Person.class);
                if (p instanceof Scientist) {
                    personComboBoxModel.addElement(p);
                }
            }
        });
        techonologyViewer = new TechonologyViewer(civilization);

        fieldViewer = new FieldViewer(civilization);

        pane.addTab(LOCALE_MESSAGES.getMessage("game.research.tab.research"), techResearcher);
        pane.addTab(LOCALE_MESSAGES.getMessage("game.research.tab.currentlyresearching"), researchProgressPanel);
        pane.addTab(LOCALE_MESSAGES.getMessage("game.research.tab.alreadyresearched"), techonologyViewer);
        pane.addTab(LOCALE_MESSAGES.getMessage("game.research.tab.fields"), fieldViewer);

        addPropertyChangeListener(this);

        pane.addChangeListener(a -> {
            update();
            techonologyViewer.update();
        });
    }

    public void update() {
        for (Technology t : civilization.currentlyResearchingTechonologys.keySet()) {
            if ((Technologies.estFinishTime(t) - civilization.civResearch.get(t) / civilization.currentlyResearchingTechonologys.get(t).getSkill()) > 0) {
                researchingTech.setText(t.getName());
                researcher.setText(LOCALE_MESSAGES.getMessage("game.research.researching.researcher", civilization.currentlyResearchingTechonologys.get(t).getName()));
                //720 is number of ticks in a month
                estTimeLeft.setText(LOCALE_MESSAGES.getMessage("game.research.researching.timeleft", ((Technologies.estFinishTime(t) - civilization.civResearch.get(t) / civilization.currentlyResearchingTechonologys.get(t).getSkill()) / 720)));

                //Get the text
                fieldListModel.clear();
                if (tech.getSelectedValue() != null) {
                    for (String s : tech.getSelectedValue().getFields()) {
                        fieldListModel.addElement(s);
                    }
                }
            } else {
                //Set everything to empty
                researchingTech.setText("");
                researcher.setText("");
                estTimeLeft.setText("");
            }
        }
        //Add all techs
        for (Technology t : civilization.civTechs.keySet()) {
            if (civilization.civTechs.get(t) == Technologies.REVEALED && !list.contains(t)) {
                list.addElement(t);
            }
        }

        //Update table
        int selectedTech = techTable.getSelectedRow();
        techTableModel.setRowCount(0);
        for (Technology t : civilization.currentlyResearchingTechonologys.keySet()) {
            techTableModel.addRow(new String[]{t.getName(), civilization.currentlyResearchingTechonologys.get(t).getName(), ((Technologies.estFinishTime(t) - civilization.civResearch.get(t) / civilization.currentlyResearchingTechonologys.get(t).getSkill()) / 720) + " months"});
        }

        if (selectedTech > -1 && techTable.getRowCount() < selectedTech) {
            techTable.setRowSelectionInterval(selectedTech, selectedTech);
        }

        int selected = personComboBox.getSelectedIndex();
        if (!personComboBox.isPopupVisible()) {
            personComboBoxModel.removeAllElements();
            for (Integer id : civilization.people) {
                Person p = gameState.getObject(id, Person.class);
                if (p instanceof Scientist) {
                    personComboBoxModel.addElement(p);
                }
            }
            personComboBox.setSelectedIndex(selected);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (tech.getModel().getSize() > 0 && tech.getSelectedValue() != null) {
            Technology selected = tech.getSelectedValue();
            techName.setText(selected.getName());
            techdifficulity.setText(LOCALE_MESSAGES.getMessage("game.research.difficulty", selected.getDifficulty()));
            techEstTime.setText(LOCALE_MESSAGES.getMessage("game.research.estcompletiontime", (Technologies.estFinishTime(selected) / 720)));

            //Get the text
            fieldListModel.clear();
            for (String s : tech.getSelectedValue().getFields()) {
                fieldListModel.addElement(s);
            }

            //check for tech points
            if (civilization.getTechPoints() > 0) {
                //Set value
                techPointCount.setText(LOCALE_MESSAGES.getMessage("game.research.techpointsworth", selected.getDifficulty()));
            } else {
                //Remove
                techInfoPanel.remove(techPointLabel);
                techInfoPanel.remove(techPointCount);
                techInfoPanel.remove(instantReshButton);
            }
            techInfoPanel.setVisible(true);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        update();
    }
}
