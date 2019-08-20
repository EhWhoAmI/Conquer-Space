package ConquerSpace.gui.game;

import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.civilization.Civilization;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
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
 * @author Zyun
 */
public class ResearchViewer extends JPanel implements ListSelectionListener {

    private JTabbedPane pane;

    private JLabel techPointLabel;
    private JPanel techResearcher;
    private JList<Technology> tech;
    private DefaultListModel<Technology> list;
    private JPanel techInfoPanel;
    private JLabel techName;
    private JLabel techdifficulity;
    private JLabel techEstTime;
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
    private Civilization c;

    public ResearchViewer(Civilization c) {
        this.c = c;
        init();

        update();

        add(pane, BorderLayout.CENTER);
    }

    public void init() {
        setLayout(new BorderLayout());
        
        techPointLabel = new JLabel("Tech Points: " + c.getTechPoints());
        add(techPointLabel, BorderLayout.NORTH);
        pane = new JTabbedPane();
        techResearcher = new JPanel();
        techResearcher.setLayout(new GridLayout(1, 2));

        //Get the list of researched tech
        list = new DefaultListModel<>();
        //list.addElement(element);
        for (Technology t : c.civTechs.keySet()) {
            if (c.civTechs.get(t) == Technologies.REVEALED) {
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

        personComboBoxModel = new DefaultComboBoxModel<>();
        for (Person p : c.people) {
            if (p instanceof Scientist) {
                personComboBoxModel.addElement(p);
            }
        }
        techInfoPanel.add(new JLabel("Researcher:"));
        personComboBox = new JComboBox<>(personComboBoxModel);
        techInfoPanel.add(personComboBox);

        researchButton = new JButton("Research");
        researchButton.setFocusable(false);
        researchButton.addActionListener((e) -> {
            //Get first researcher to research
            if (!tech.isSelectionEmpty()) {
                c.assignResearch(tech.getSelectedValue(), (Scientist) personComboBox.getSelectedItem());
                list.removeElement(tech.getSelectedValue());
                //Set everything empty
                techName.setText("");
                techdifficulity.setText("");
                techEstTime.setText("");
            }
        });
        techInfoPanel.add(researchButton);

        researchProgressPanel = new JPanel();
        researchProgressPanel.setLayout(new VerticalFlowLayout());

        techTableModel = new DefaultTableModel(new String[]{"Tech", "Researcher", "Time left"}, 0);
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
            for (Technology t : c.currentlyResearchingTechonologys.keySet()) {
                //researchingTech.setText(t.getName());
                //researcher.setText("Researcher: " + c.currentlyResearchingTechonologys.get(t).getName());
                //estTimeLeft.setText("Estimated time left: " + ((Technologies.estFinishTime(t) - c.civResearch.get(t) / c.currentlyResearchingTechonologys.get(t).getSkill()) / 720) + " months");
                techTableModel.addRow(new String[]{t.getName(), c.currentlyResearchingTechonologys.get(t).getName(), ((Technologies.estFinishTime(t) - c.civResearch.get(t) / c.currentlyResearchingTechonologys.get(t).getSkill()) / 720) + " months"});
            }
            personComboBoxModel.removeAllElements();
            for (Person p : c.people) {
                if (p instanceof Scientist) {
                    personComboBoxModel.addElement(p);
                }
            }
        });
        techonologyViewer = new TechonologyViewer(c);

        fieldViewer = new FieldViewer(c);
        
        pane.addTab("Research", techResearcher);
        pane.addTab("Researching", researchProgressPanel);
        pane.addTab("Researched Techs", techonologyViewer);
        pane.addTab("Fields", fieldViewer);

        pane.addChangeListener(a -> {
            update();
            techonologyViewer.update();
        });
    }

    public void update() {
        for (Technology t : c.currentlyResearchingTechonologys.keySet()) {
            if ((Technologies.estFinishTime(t) - c.civResearch.get(t) / c.currentlyResearchingTechonologys.get(t).getSkill()) > 0) {
                researchingTech.setText(t.getName());
                researcher.setText("Researcher: " + c.currentlyResearchingTechonologys.get(t).getName());
                //720 is number of ticks in a month
                estTimeLeft.setText("Estimated time left: " + ((Technologies.estFinishTime(t) - c.civResearch.get(t) / c.currentlyResearchingTechonologys.get(t).getSkill()) / 720) + " months");
            } else {
                //Set everything to empty
                researchingTech.setText("");
                researcher.setText("");
                estTimeLeft.setText("");
            }
        }
        //Add all techs
        for (Technology t : c.civTechs.keySet()) {
            if (c.civTechs.get(t) == Technologies.REVEALED && !list.contains(t)) {
                list.addElement(t);
            }
        }

        //Update table
        int selectedTech = techTable.getSelectedRow();
        techTableModel.setRowCount(0);
        for (Technology t : c.currentlyResearchingTechonologys.keySet()) {
            techTableModel.addRow(new String[]{t.getName(), c.currentlyResearchingTechonologys.get(t).getName(), ((Technologies.estFinishTime(t) - c.civResearch.get(t) / c.currentlyResearchingTechonologys.get(t).getSkill()) / 720) + " months"});
        }
                
        if(selectedTech > -1) {
            techTable.setRowSelectionInterval(selectedTech, selectedTech);
        }
        
        int selected = personComboBox.getSelectedIndex();
        if (!personComboBox.isPopupVisible()) {
            personComboBoxModel.removeAllElements();
            for (Person p : c.people) {
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
            techdifficulity.setText("Difficulty: " + selected.getDifficulty());
            techEstTime.setText("Estimated time to completion: " + (Technologies.estFinishTime(selected) / 720) + " months");
        }
    }
}
