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
import ConquerSpace.common.game.characters.Person;
import ConquerSpace.common.game.characters.PersonalityTrait;
import ConquerSpace.common.game.characters.Scientist;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.util.ResourceLoader;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author EhWhoAmI
 */
public class PersonWindow extends JPanel {

    private JList<Person> personList;
    private DefaultListModel<Person> listModel;

    private JPanel container;

    private JLabel name;
    private JLabel personAgeLabel;
    private JLabel personJobLabel;
    private JLabel personRoleLabel;
    private JLabel skillLabel;
    private JLabel positionLabel;
    private JLabel isDeadLabel;
    private JLabel personImageLabel;
    private DefaultListModel<PersonalityTrait> personalityListModel;
    private JList<PersonalityTrait> personPersonalityList;

    private int previouslySelected = 0;
    private Civilization c;

    private GameState gameState;
    
    public PersonWindow(GameState gameState, Civilization c) {
        this.c = c;
        this.gameState = gameState;
        setLayout(new HorizontalFlowLayout());
        listModel = new DefaultListModel<>();
        for (Integer id : c.people) {
            Person person = gameState.getObject(id, Person.class);
            listModel.addElement(person);
        }
        personList = new JList<>(listModel);
        personList.setSelectedIndex(0);

        personList.addListSelectionListener(l -> {
            //Change text
            if (personList.getSelectedIndex() > -1) {
                name.setText("Name: " + personList.getSelectedValue().getName());
                personAgeLabel.setText("Age: " + personList.getSelectedValue().getAge());
                personJobLabel.setText("Job: " + personList.getSelectedValue().getJobName());
                personRoleLabel.setText("Currently Doing: " + personList.getSelectedValue().roleText());
                if (personList.getSelectedValue() instanceof Scientist) {
                    skillLabel.setText("Skill: " + ((Scientist) personList.getSelectedValue()).getSkill());
                }
                String position = "Unknown";
                if (personList.getSelectedValue().getPosition() != null) {
                    position = personList.getSelectedValue().getPosition().getName();
                }

                positionLabel.setText("Location: " + position); // + u.getSpaceObject(personList.getSelectedValue().getPosition().getUniversePath())
                if (personList.getSelectedValue().isDead()) {
                    isDeadLabel.setText("Ded - F");
                } else {
                    isDeadLabel.setText("");
                }

                personalityListModel.clear();
                for (PersonalityTrait pt : personList.getSelectedValue().traits) {
                    personalityListModel.addElement(pt);
                }
            }
        });

        JScrollPane pane = new JScrollPane(personList);

        container = new JPanel();
        container.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP));
        //Set UI

        name = new JLabel("Name: " + personList.getSelectedValue().getName());

        ImageIcon icon = ResourceLoader.getIcon("portrait.person.icon");
        personImageLabel = new JLabel(icon);

        personAgeLabel = new JLabel("Age: " + personList.getSelectedValue().getAge());

        personJobLabel = new JLabel("Job: " + personList.getSelectedValue().getJobName());
        personRoleLabel = new JLabel("Currently Doing: " + personList.getSelectedValue().roleText());

        skillLabel = new JLabel("Skill: ");
        positionLabel = new JLabel("Location: ");

        isDeadLabel = new JLabel();

        personalityListModel = new DefaultListModel<>();
        personPersonalityList = new JList<>(personalityListModel);

        JPanel personImageContainer = new JPanel(new HorizontalFlowLayout());
        personImageContainer.add(name);
        personImageContainer.add(personImageLabel);
        container.add(personImageContainer);
        container.add(personAgeLabel);
        container.add(personJobLabel);
        container.add(personRoleLabel);
        container.add(skillLabel);
        container.add(positionLabel);
        container.add(isDeadLabel);
        container.add(new JScrollPane(personPersonalityList));
        add(pane);
        add(container);
    }

    public void update() {
        previouslySelected = personList.getSelectedIndex();
        listModel.clear();
        for (Integer id : c.people) {
            Person person = gameState.getObject(id, Person.class);
            listModel.addElement(person);
        }
        personList.setSelectedIndex(previouslySelected);
    }
}
