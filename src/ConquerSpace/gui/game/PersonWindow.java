package ConquerSpace.gui.game;

import ConquerSpace.game.people.Person;
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
public class PersonWindow extends JPanel{
    private JList<Person> personList;
    private DefaultListModel<Person> listModel;
    
    private JPanel container;
    
    public PersonWindow(Civilization c) {
        setLayout(new GridLayout(1, 2));
        listModel = new DefaultListModel<>();
        for(Person p : c.people) {
            listModel.addElement(p);
        }
        personList = new JList<>(listModel);
        personList.setSelectedIndex(0);
        JScrollPane pane = new JScrollPane(personList);
        
        container = new JPanel();
        container.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP));
        //Set UI
        
        JLabel name = new JLabel("Name: " + personList.getSelectedValue().getName());
        JLabel personAge = new JLabel("Age: " + personList.getSelectedValue().getAge());
        
        JLabel personJob = new JLabel("Job: " + personList.getSelectedValue().getJobName());
        
        container.add(name);
        container.add(personAge);
        container.add(personJob);
        add(pane);
        add(container);
    }
    
}
