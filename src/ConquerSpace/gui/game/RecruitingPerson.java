package ConquerSpace.gui.game;

import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.universe.civilization.Civilization;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author zyunl
 */
public class RecruitingPerson extends JPanel {

    private JList<Person> probablePersonList;
    private DefaultListModel<Person> personListModel;
    private JLabel name;
    private JLabel personAgeLabel;
    private JLabel personJobLabel;
    private JLabel skillLabel;

    private JButton recruitButton;
    private JPanel container;

    private Civilization c;

    private int previouslySelected;

    public RecruitingPerson(Civilization c) {
        this.c = c;
        setLayout(new GridLayout(1, 2));
        //Generate people and things like that
        personListModel = new DefaultListModel<>();
        for (Person p : c.unrecruitedPeople) {
            personListModel.addElement(p);
        }

        probablePersonList = new JList<>(personListModel);
        probablePersonList.setSelectedIndex(1);

        probablePersonList.addListSelectionListener(l -> {
            //Change text
            container.removeAll();
            if (probablePersonList.getSelectedIndex() > -1) {
                name.setText("Name: " + probablePersonList.getSelectedValue().getName());
                personAgeLabel.setText("Age: " + probablePersonList.getSelectedValue().getAge());
                personJobLabel.setText("Job: " + probablePersonList.getSelectedValue().getJobName());
                container.add(name);
                container.add(personAgeLabel);
                container.add(personJobLabel);

                if (probablePersonList.getSelectedValue() instanceof Scientist) {
                    skillLabel.setText("Skill: " + ((Scientist) probablePersonList.getSelectedValue()).getSkill());
                    container.add(skillLabel);
                }
                container.add(recruitButton);

            }
        });

        JScrollPane listScrollPane = new JScrollPane(probablePersonList);

        container = new JPanel();
        container.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP));
        //Set UI

        name = new JLabel("Name: " + probablePersonList.getSelectedValue().getName());
        personAgeLabel = new JLabel("Age: " + probablePersonList.getSelectedValue().getAge());

        personJobLabel = new JLabel("Job: " + probablePersonList.getSelectedValue().getJobName());
        skillLabel = new JLabel("Skill: ");
        recruitButton = new JButton("Recruit!");
        recruitButton.addActionListener(a -> {
            if (probablePersonList.getSelectedIndex() > -1) {
                Person p = probablePersonList.getSelectedValue();
                personListModel.remove(probablePersonList.getSelectedIndex());
                c.unrecruitedPeople.remove(p);
                c.people.add(p);
            }
        });

        container.add(name);
        container.add(personAgeLabel);
        container.add(personJobLabel);

        if (probablePersonList.getSelectedValue() instanceof Scientist) {
            container.add(skillLabel);
        }
        container.add(recruitButton);
        add(listScrollPane);
        add(container);
    }

    public void update() {
        previouslySelected = probablePersonList.getSelectedIndex();
        personListModel.clear();
        for (Person p : c.unrecruitedPeople) {
            personListModel.addElement(p);
        }
        probablePersonList.setSelectedIndex(previouslySelected);
    }
}
