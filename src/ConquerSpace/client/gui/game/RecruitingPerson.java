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

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.characters.Person;
import ConquerSpace.common.game.characters.PersonalityTrait;
import ConquerSpace.common.game.characters.Scientist;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
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
 * @author EhWhoAmI
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
    
    private GameState gameState;
    
    private Galaxy galaxy;

    public RecruitingPerson(GameState gameState, Civilization c) {
        this.gameState = gameState;
        this.galaxy = gameState.getUniverse();
        setLayout(new HorizontalFlowLayout());
        //Generate people and things like that
        personListModel = new PersonListModel(c.getUnrecruitedPeople());

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

                positionLabel.setText("Location: " + probablePersonList.getSelectedValue().getPosition().getName() + ", " + galaxy.getSpaceObject(probablePersonList.getSelectedValue().getPosition().getUniversePath()));
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
                int previousSelection = probablePersonList.getSelectedIndex();
                
                Person p = probablePersonList.getSelectedValue();
                c.getUnrecruitedPeople().remove(p.getReference());
                c.getPeople().add(p.getReference());
                //Set selected value
                probablePersonList.setSelectedIndex(previousSelection);
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

        ArrayList<ObjectReference> person;
        long before = 0, after = System.currentTimeMillis();

        public PersonListModel(ArrayList<ObjectReference> person) {
            this.person = person;
        }

        @Override
        public int getSize() {
            return person.size();
        }

        @Override
        public Person getElementAt(int index) {
            if (person.size() > 0) {
                return gameState.getObject(person.get(index), Person.class);
            }
            return null;
        }

    }
}
