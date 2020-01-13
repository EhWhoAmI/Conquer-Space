package ConquerSpace.gui.game;

import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
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
    private PersonListModel personListModel;
    private JLabel name;
    private JLabel personAgeLabel;
    private JLabel personJobLabel;
    private JLabel skillLabel;
    private JLabel positionLabel;
    private DefaultListModel<PersonalityTrait> personalityListModel;
    private JList<PersonalityTrait> personPersonalityList;

    private JButton recruitButton;
    private JPanel container;

    private Civilization c;

    public RecruitingPerson(Civilization c, Universe u) {
        this.c = c;
        setLayout(new GridLayout(1, 2));
        //Generate people and things like that
        personListModel = new PersonListModel(c.unrecruitedPeople);

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

                positionLabel.setText("Location: " + probablePersonList.getSelectedValue().getPosition().getName() + ", " + u.getSpaceObject(probablePersonList.getSelectedValue().getPosition().getUniversePath()));
                container.add(positionLabel);

                personalityListModel.clear();
                for (PersonalityTrait pt : probablePersonList.getSelectedValue().traits) {
                    personalityListModel.addElement(pt);
                }
                container.add(new JScrollPane(personPersonalityList));
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
        positionLabel = new JLabel("Location: ");
        recruitButton.addActionListener(a -> {
            if (probablePersonList.getSelectedIndex() > -1) {
                Person p = probablePersonList.getSelectedValue();
                c.unrecruitedPeople.remove(p);
                c.people.add(p);
            }
        });
        personalityListModel = new DefaultListModel<>();
        personPersonalityList = new JList<>(personalityListModel);

        container.add(name);
        container.add(personAgeLabel);
        container.add(personJobLabel);

        if (probablePersonList.getSelectedValue() instanceof Scientist) {
            container.add(skillLabel);
        }
        container.add(positionLabel);
        container.add(new JScrollPane(personPersonalityList));

        container.add(recruitButton);
        add(listScrollPane);
        add(container);
    }

    public void update() {
        probablePersonList.updateUI();
    }

    private class PersonListModel extends AbstractListModel<Person> {

        ArrayList<Person> person;
        long before = 0, after = System.currentTimeMillis();

        public PersonListModel(ArrayList<Person> person) {
            this.person = person;
        }

        @Override
        public int getSize() {
            return person.size();
        }

        @Override
        public Person getElementAt(int index) {
            if (person.size() > 0) {
                return person.get(index);
            }
            return null;
        }

    }
}
