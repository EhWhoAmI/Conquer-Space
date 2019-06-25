package ConquerSpace.gui.game;

import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.universe.civilization.Civilization;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author zyunl
 */
public class PersonWindow extends JPanel {

    private JList<Person> personList;
    private DefaultListModel<Person> listModel;

    private JPanel container;

    private JLabel name;
    private JLabel personAgeLabel;
    private JLabel personJobLabel;
    private JLabel skillLabel;

    private int previouslySelected = 0;
    private Civilization c;

    public PersonWindow(Civilization c) {
        this.c = c;
        setLayout(new GridLayout(1, 2));
        listModel = new DefaultListModel<>();
        for (Person p : c.people) {
            listModel.addElement(p);
        }
        personList = new JList<>(listModel);
        personList.setSelectedIndex(0);

        personList.addListSelectionListener(l -> {
            //Change text
            if (personList.getSelectedIndex() > -1) {
                name.setText("Name: " + personList.getSelectedValue().getName());
                personAgeLabel.setText("Age: " + personList.getSelectedValue().getAge());
                personJobLabel.setText("Job: " + personList.getSelectedValue().getJobName());
                if(personList.getSelectedValue() instanceof Scientist) {
                    skillLabel.setText("Skill: " + ((Scientist)personList.getSelectedValue()).getSkill());
                }
            }
        });

        JScrollPane pane = new JScrollPane(personList);

        container = new JPanel();
        container.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP));
        //Set UI

        name = new JLabel("Name: " + personList.getSelectedValue().getName());
        personAgeLabel = new JLabel("Age: " + personList.getSelectedValue().getAge());

        personJobLabel = new JLabel("Job: " + personList.getSelectedValue().getJobName());
        
        skillLabel = new JLabel("Skill: ");

        container.add(name);
        container.add(personAgeLabel);
        container.add(personJobLabel);
        container.add(skillLabel);
        add(pane);
        add(container);
    }

    public void update() {
        previouslySelected = personList.getSelectedIndex();
        listModel.clear();
        for (Person p : c.people) {
            listModel.addElement(p);
        }
        personList.setSelectedIndex(previouslySelected);
    }
}
