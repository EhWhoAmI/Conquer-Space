package ConquerSpace.gui.game;

import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.civilization.Civilization;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Zyun
 */
public class ResearchViewer extends JPanel implements ListSelectionListener {

    private JTabbedPane pane;

    private JPanel techResearcher;
    private JList<Technology> tech;
    private DefaultListModel<Technology> list;
    private JPanel techInfoPanel;
    private JLabel techName;
    private JLabel techdifficulity;
    private JLabel techEstTime;
    private JButton researchButton;

    private JPanel researchProgressPanel;
    private JLabel researchingTech;
    private JLabel researcher;
    private JLabel estTimeLeft;

    private Civilization c;

    public ResearchViewer(Civilization c) {
        this.c = c;
        init();

        //Timer researchingTechticker = new Timer(100, (e) -> {
            update();
        //});

        //researchingTechticker.setRepeats(true);
        //researchingTechticker.start();

        add(pane);
    }

    public void init() {
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

        researchButton = new JButton("Research");
        researchButton.setFocusable(false);
        researchButton.addActionListener((e) -> {
            //Get first researcher to research
            if (!tech.isSelectionEmpty()) {
                c.assignResearch(tech.getSelectedValue(), c.people.get(0));
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

        researchingTech = new JLabel("");
        researchProgressPanel.add(researchingTech);

        researcher = new JLabel("");
        researchProgressPanel.add(researcher);

        estTimeLeft = new JLabel("");
        researchProgressPanel.add(estTimeLeft);

        techResearcher.add(tech);
        techResearcher.add(techInfoPanel);

        pane.addChangeListener((e) -> {
            for (Technology t : c.currentlyResearchingTechonologys.keySet()) {
                researchingTech.setText(t.getName());
                researcher.setText("Researcher: " + c.currentlyResearchingTechonologys.get(t).getName());
                estTimeLeft.setText("Estimated time left: " + ((Technologies.estFinishTime(t) - c.civResearch.get(t) / c.currentlyResearchingTechonologys.get(t).getSkill()) / 720) + " months");
            }
        });
        pane.addTab("Research", techResearcher);
        pane.addTab("Researching", researchProgressPanel);
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
