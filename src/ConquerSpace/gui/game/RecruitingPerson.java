package ConquerSpace.gui.game;

import ConquerSpace.game.people.Person;
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
public class RecruitingPerson extends JPanel{
    private JList<Person> probablePersonList;
    private DefaultListModel<Person> personListModel;
    
    private JPanel container;
    public RecruitingPerson(Civilization c) {
        setLayout(new GridLayout(1, 2));
        //Generate people and things like that
        personListModel = new DefaultListModel<>();
        for(Person p : c.unrecruitedPeople){
            personListModel.addElement(p);
        }
        probablePersonList = new JList<>(personListModel);
        probablePersonList.setSelectedIndex(1);
        JScrollPane listScrollPane = new JScrollPane(probablePersonList);
        
        container = new JPanel();
        container.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP));
        //Set UI
        
        JLabel name = new JLabel("Name: " + probablePersonList.getSelectedValue().getName());
        JLabel personAge = new JLabel("Age: " + probablePersonList.getSelectedValue().getAge());
        
        JLabel personJob = new JLabel("Job: " + probablePersonList.getSelectedValue().getJobName());
        
        JButton recruit = new JButton("Recruit!");
        
        container.add(name);
        container.add(personAge);
        container.add(personJob);
        container.add(recruit);
        add(listScrollPane);
        add(container);
    }    
}